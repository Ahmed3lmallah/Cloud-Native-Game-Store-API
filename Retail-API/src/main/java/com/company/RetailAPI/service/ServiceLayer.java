package com.company.RetailAPI.service;

import com.company.RetailAPI.util.feign.*;
import com.company.RetailAPI.util.messages.LevelUpMsg;
import com.company.RetailAPI.views.CustomerViewModel;
import com.company.RetailAPI.views.ProductViewModel;
import com.company.RetailAPI.views.input.InventoryInputModel;
import com.company.RetailAPI.views.input.InvoiceInputModel;
import com.company.RetailAPI.views.input.LevelUpInputModel;
import com.company.RetailAPI.views.output.InventoryViewModel;
import com.company.RetailAPI.views.output.InvoiceViewModel;
import com.company.RetailAPI.views.output.LevelUpViewModel;
import com.company.RetailAPI.views.products.ProductFromInventory;
import com.company.RetailAPI.views.products.ProductFromInvoice;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceLayer {

    //
    // Queue Info
    // ------------------------- //
    private static final String EXCHANGE = "level-up-exchange";
    private static final String ROUTING_KEY = "level-up.update.Retail";

    //
    // Properties
    // ------------------------- //
    private RabbitTemplate rabbitTemplate;
    private CustomerClient customerClient;
    private ProductClient productClient;
    private InventoryClient inventoryClient;
    private LevelUpClient levelUpClient;
    private InvoiceClient invoiceClient;

    @Autowired
    public ServiceLayer(RabbitTemplate rabbitTemplate, CustomerClient customerClient, ProductClient productClient, InventoryClient inventoryClient, LevelUpClient levelUpClient, InvoiceClient invoiceClient) {
        this.rabbitTemplate = rabbitTemplate;
        this.customerClient = customerClient;
        this.productClient = productClient;
        this.inventoryClient = inventoryClient;
        this.levelUpClient = levelUpClient;
        this.invoiceClient = invoiceClient;
    }

    //
    // Level Up! Service Methods
    // --------------------- //
    @Transactional
    public LevelUpViewModel findLevelUpByCustomerId(int customerId){
        System.out.println("Contacting Level Up! Service client to get Level Up! entry...");
        return buildLevelUpViewModel(levelUpClient.getLevelUpByCustomerId(customerId));
    }

    //
    // Inventory Service Methods
    // --------------------- //
    @Transactional
    public ProductFromInventory getProductFromInventory(int id){
        InventoryViewModel inventoryItem = findInventory(id);

        ProductFromInventory product = new ProductFromInventory();
        product.setInventoryId(inventoryItem.getInventoryId());
        product.setListPrice(inventoryItem.getProduct().getListPrice());
        product.setProductName(inventoryItem.getProduct().getProductName());
        product.setProductDescription(inventoryItem.getProduct().getProductDescription());
        product.setQuantity(inventoryItem.getQuantity());

        return product;
    }

    @Transactional
    public List<ProductFromInventory> getAllProductsFromInventory(){
        List<InventoryViewModel> inventoryItems = findAllInventories();
        List<ProductFromInventory> products = new ArrayList<>();

        inventoryItems.forEach(inventoryItem -> {
            ProductFromInventory product = new ProductFromInventory();
            product.setInventoryId(inventoryItem.getInventoryId());
            product.setListPrice(inventoryItem.getProduct().getListPrice());
            product.setProductName(inventoryItem.getProduct().getProductName());
            product.setProductDescription(inventoryItem.getProduct().getProductDescription());
            product.setQuantity(inventoryItem.getQuantity());
            products.add(product);
        });

        return products;
    }

    private InventoryViewModel findInventory(int inventoryId){
        System.out.println("Contacting Inventory Service client to get inventory...");
        return buildInventoryViewModel(inventoryClient.getInventory(inventoryId));
    }

    private List<InventoryViewModel> findAllInventories(){
        // Getting inventory list
        System.out.println("Contacting Inventory Service client to get all inventories...");
        List<InventoryInputModel> fromInventoryService = inventoryClient.getAllInventories();
        // Building ViewModels
        List<InventoryViewModel> inventoryViewModels = new ArrayList<>();
        fromInventoryService.forEach(inventory -> inventoryViewModels.add(buildInventoryViewModel(inventory)));

        return inventoryViewModels;
    }

    private InventoryViewModel updateInventory(InventoryInputModel inventoryInputModel){
        // Checking if product exists - throws an exception if not
        findProduct(inventoryInputModel.getProductId());

        // Updating Inventory
        System.out.println("Contacting Inventory Service client to update inventory...");
        return buildInventoryViewModel(inventoryClient.updateInventory(inventoryInputModel, inventoryInputModel.getInventoryId()));
    }

    //
    // Invoice Service Methods
    // --------------------- //
    @Transactional
    public InvoiceViewModel saveInvoice(InvoiceInputModel invoiceInputModel){

        // initializing...
        InvoiceViewModel invoiceViewModel = new InvoiceViewModel();
        List<ProductFromInvoice> invoiceItems = new ArrayList<>();
        int points;
        List<BigDecimal> totalPrice = new ArrayList<>();

        // Assign Customer
        invoiceViewModel.setCustomer(findCustomer(invoiceInputModel.getCustomerId()));

        // Checking Quantity and if Inventory exists
        invoiceInputModel.getInvoiceItems().forEach(invoiceItem -> {
            //Checking Inventory
            InventoryViewModel inventory = findInventory(invoiceItem.getInventoryId());

            //Checking Quantity
            if(invoiceItem.getQuantity()>inventory.getQuantity()){
                throw new IllegalArgumentException("Inventory Id ["+invoiceItem.getInventoryId()+
                        "]: Only "+inventory.getQuantity()+" items available at storage.");
            }

            //Calculating Price
            invoiceItem.setListPrice(inventory.getProduct().getListPrice());
            BigDecimal itemPrice = BigDecimal.valueOf( invoiceItem.getQuantity() ).multiply( inventory.getProduct().getListPrice() ).setScale(2, RoundingMode.HALF_UP);
            totalPrice.add(itemPrice);
        });

        // Saving invoice
        System.out.println("Contacting Invoice Service client to create invoice...");
        invoiceInputModel = invoiceClient.createInvoice(invoiceInputModel);

        // LevelUp! points
        int result = totalPrice.stream().reduce(BigDecimal.ZERO, BigDecimal::add).intValue();
        points = (result/50)*10;
        LevelUpInputModel currentPoints = levelUpClient.getLevelUpByCustomerId(invoiceViewModel.getCustomer().getCustomerId());
        currentPoints.setPoints(currentPoints.getPoints()+points);

        // Assigning Points to ViewModel
        if(currentPoints.getLevelUpId()==0){
            currentPoints.setCustomerId(invoiceViewModel.getCustomer().getCustomerId());
            // Adding points to InvoiceViewModel
            invoiceViewModel.setMemberPoints("Level Up! Service currently unavailable! No worries... Your new points were saved :)");
        } else {
            // Adding points to InvoiceViewModel
            invoiceViewModel.setMemberPoints(String.valueOf(currentPoints.getPoints()));
        }

        // Sending to Points to Queue
        System.out.println("Updating Level Up! account for customerId: "+currentPoints.getCustomerId());
        rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, (convertLevelUpToInputModel(currentPoints)));
        System.out.println("Msg Sent to Queue to update...");

        //Building ViewModel...
        invoiceViewModel.setInvoiceId(invoiceInputModel.getInvoiceId());
        invoiceViewModel.setPurchaseDate(invoiceInputModel.getPurchaseDate());
        invoiceInputModel.getInvoiceItems().forEach(invoiceItem -> {

            //Getting Inventory Info
            InventoryViewModel inventory = findInventory(invoiceItem.getInventoryId());

            //Updating Quantity in Inventory
            inventory.setQuantity(inventory.getQuantity()-invoiceItem.getQuantity());
            inventory = updateInventory(convertInventoryToInputModel(inventory));

            //Persisting InvoiceItems
            ProductFromInvoice product = new ProductFromInvoice();
            product.setInvoiceId(invoiceItem.getInvoiceId());
            product.setInvoiceItemId(invoiceItem.getInvoiceItemId());
            product.setInventoryId(inventory.getInventoryId());
            product.setProductName(inventory.getProduct().getProductName());
            product.setProductDescription(inventory.getProduct().getProductDescription());
            product.setUnitPrice(invoiceItem.getListPrice());
            product.setQuantity(invoiceItem.getQuantity());
            invoiceItems.add(product); });
        invoiceViewModel.setInvoiceItems(invoiceItems);

        return invoiceViewModel;
    }

    @Transactional
    public InvoiceViewModel findInvoice(int invoiceId){
        System.out.println("Contacting Invoice Service client to get invoice...");
        return buildInvoiceViewModel(invoiceClient.getInvoice(invoiceId));
    }

    @Transactional
    public List<InvoiceViewModel> findAllInvoices(){
        // Getting invoices list
        System.out.println("Contacting Invoice Service client to get all invoices...");
        List<InvoiceInputModel> fromInvoiceService = invoiceClient.getAllInvoices();

        // Building ViewModels
        List<InvoiceViewModel> invoiceViewModels = new ArrayList<>();
        fromInvoiceService.forEach(invoice -> invoiceViewModels.add(buildInvoiceViewModel(invoice)));

        return invoiceViewModels;
    }

    @Transactional
    public List<InvoiceViewModel> findInvoicesByCustomer(int customerId){
        // Getting invoices list
        System.out.println("Contacting Invoice Service client to get all invoices for customer...");
        List<InvoiceInputModel> fromInvoiceService = invoiceClient.getInvoicesByCustomer(customerId);

        // Building ViewModels
        List<InvoiceViewModel> invoiceViewModels = new ArrayList<>();
        fromInvoiceService.forEach(invoice -> invoiceViewModels.add(buildInvoiceViewModel(invoice)));

        return invoiceViewModels;
    }

    //
    //
    // Customer Service Methods
    // --------------------- //
    private CustomerViewModel findCustomer(int customerId){
        System.out.println("Contacting Customer Service client to get customer...");
        return customerClient.getCustomer(customerId);
    }

    //
    // Product Service Methods
    // --------------------- //
    private ProductViewModel findProduct(int productId){
        System.out.println("Contacting Product Service client to get product...");
        return productClient.getProduct(productId);
    }

    //
    // Helper Methods
    // --------------------- //
    private InventoryViewModel buildInventoryViewModel(InventoryInputModel inventoryInputModel){
        // Persisting Inventory
        InventoryViewModel inventoryViewModel = new InventoryViewModel();
        inventoryViewModel.setInventoryId(inventoryInputModel.getInventoryId());
        inventoryViewModel.setQuantity(inventoryInputModel.getQuantity());
        // Getting Product
        inventoryViewModel.setProduct(findProduct(inventoryInputModel.getProductId()));
        return inventoryViewModel;
    }

    private LevelUpViewModel buildLevelUpViewModel(LevelUpInputModel levelUpInputModel){
        // Persisting LevelUp!
        LevelUpViewModel levelUpViewModel = new LevelUpViewModel();
        levelUpViewModel.setLevelUpId(levelUpInputModel.getLevelUpId());
        levelUpViewModel.setMemberDate(levelUpInputModel.getMemberDate());
        levelUpViewModel.setPoints(levelUpInputModel.getPoints());
        // Getting Customer
        levelUpViewModel.setCustomer(findCustomer(levelUpInputModel.getCustomerId()));
        return levelUpViewModel;
    }

    private InvoiceViewModel buildInvoiceViewModel(InvoiceInputModel invoice){
        //initializing...
        InvoiceViewModel invoiceViewModel = new InvoiceViewModel();
        List<ProductFromInvoice> invoiceItemsList = new ArrayList<>();

        //InvoiceItems
        invoice.getInvoiceItems().forEach(invoiceItem -> {

            ProductFromInvoice product = new ProductFromInvoice();

            //Getting Inventory Info
            InventoryViewModel inventory = findInventory(invoiceItem.getInventoryId());

            //Persisting
            product.setInvoiceId(invoiceItem.getInvoiceId());
            product.setInvoiceItemId(invoiceItem.getInvoiceItemId());
            product.setInventoryId(inventory.getInventoryId());
            product.setProductName(inventory.getProduct().getProductName());
            product.setProductDescription(inventory.getProduct().getProductDescription());
            product.setUnitPrice(invoiceItem.getListPrice());
            product.setQuantity(invoiceItem.getQuantity());
            //Adding to list
            invoiceItemsList.add(product);
        });

        //Getting Membership info
        LevelUpViewModel levelUp = findLevelUpByCustomerId(invoice.getCustomerId());

        //persisting
        invoiceViewModel.setInvoiceId(invoice.getInvoiceId());
        invoiceViewModel.setPurchaseDate(invoice.getPurchaseDate());
        invoiceViewModel.setCustomer(levelUp.getCustomer());
        invoiceViewModel.setMemberPoints(String.valueOf(levelUp.getPoints()));
        invoiceViewModel.setInvoiceItems(invoiceItemsList);

        return invoiceViewModel;
    }

    private LevelUpMsg convertLevelUpToInputModel(LevelUpInputModel levelUpInputModel){
        LevelUpMsg msg = new LevelUpMsg();
        msg.setLevelUpId(levelUpInputModel.getLevelUpId());
        msg.setCustomerId(levelUpInputModel.getCustomerId());
        msg.setMemberDate(levelUpInputModel.getMemberDate());
        msg.setPoints(levelUpInputModel.getPoints());
        return msg;
    }

    private InventoryInputModel convertInventoryToInputModel (InventoryViewModel ivm){
        InventoryInputModel inventoryInputModel = new InventoryInputModel();
        inventoryInputModel.setInventoryId(ivm.getInventoryId());
        inventoryInputModel.setProductId(ivm.getProduct().getProductId());
        inventoryInputModel.setQuantity(ivm.getQuantity());
        return inventoryInputModel;
    }
}
