package com.company.RetailAPI.service;

import com.company.RetailAPI.exception.NotFoundException;
import com.company.RetailAPI.util.feign.*;
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
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceLayer {

    private CustomerClient customerClient;
    private ProductClient productClient;
    private InventoryClient inventoryClient;
    private LevelUpClient levelUpClient;
    private InvoiceClient invoiceClient;

    @Autowired
    public ServiceLayer(CustomerClient customerClient, ProductClient productClient, InventoryClient inventoryClient, LevelUpClient levelUpClient, InvoiceClient invoiceClient) {
        this.customerClient = customerClient;
        this.productClient = productClient;
        this.inventoryClient = inventoryClient;
        this.levelUpClient = levelUpClient;
        this.invoiceClient = invoiceClient;
    }

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
    // Level Up! Service Methods
    // --------------------- //
    public LevelUpViewModel findLevelUpByCustomerId(int customerId){
        System.out.println("Contacting Level Up! Service client to get Level Up! entry...");
        return buildLevelUpViewModel(levelUpClient.getLevelUpByCustomerId(customerId));
    }

    private LevelUpViewModel updateLevelUp(LevelUpInputModel levelUpInputModel){
        // Checking if customer exists - throws an exception if not
        checkForCustomer(levelUpInputModel.getCustomerId());

        // Updating LevelUp!
        System.out.println("Contacting Level Up! Service client to update Level Up! entry...");
        return buildLevelUpViewModel(levelUpClient.updateLevelUp(levelUpInputModel, levelUpInputModel.getLevelUpId()));
    }

    //
    // Inventory Service Methods
    // --------------------- //
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

    //
    // Invoice Service Methods
    // --------------------- //
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

        //Building ViewModel...
        invoiceViewModel.setInvoiceId(invoiceInputModel.getInvoiceId());
        invoiceViewModel.setPurchaseDate(invoiceInputModel.getPurchaseDate());
        invoiceInputModel.getInvoiceItems().forEach(invoiceItem -> {

            //Persisting InvoiceItems
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
            invoiceItems.add(product);
        });

        invoiceViewModel.setInvoiceItems(invoiceItems);

        // LevelUp! points
        int result = totalPrice.stream().reduce(BigDecimal.ZERO, BigDecimal::add).intValue();
        points = (result/50)*10;

        LevelUpViewModel currentPoints = findLevelUpByCustomerId(invoiceViewModel.getCustomer().getCustomerId());
        currentPoints.setPoints(currentPoints.getPoints()+points);
        currentPoints = updateLevelUp((convertLevelUpToInputModel(currentPoints)));

        invoiceViewModel.setMemberPoints(currentPoints.getPoints());

        return invoiceViewModel;
    }

    public InvoiceViewModel findInvoice(int invoiceId){
        System.out.println("Contacting Invoice Service client to get invoice...");
        return buildInvoiceViewModel(invoiceClient.getInvoice(invoiceId));
    }

    public List<InvoiceViewModel> findAllInvoices(){
        // Getting invoices list
        System.out.println("Contacting Invoice Service client to get all invoices...");
        List<InvoiceInputModel> fromInvoiceService = invoiceClient.getAllInvoices();

        // Building ViewModels
        List<InvoiceViewModel> invoiceViewModels = new ArrayList<>();
        fromInvoiceService.forEach(invoice -> invoiceViewModels.add(buildInvoiceViewModel(invoice)));

        return invoiceViewModels;
    }

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
    // HELPER METHODS
    private void checkForCustomer(int customerId){
        System.out.println("Checking if customer exists...");
        try{
            findCustomer(customerId);
        } catch (FeignException e){
            System.out.println("...customer wasn't found in DB!");
            throw new NotFoundException("Customer doesn't exist! Create the customer first using: [POST] 'uri=/customers' endpoint.");
        }
        System.out.println("...customer found in DB!");
    }

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

        //persisting
        invoiceViewModel.setInvoiceId(invoice.getInvoiceId());
        invoiceViewModel.setPurchaseDate(invoice.getPurchaseDate());
        invoiceViewModel.setCustomer(findCustomer(invoice.getCustomerId()));
        invoiceViewModel.setMemberPoints(findLevelUpByCustomerId(invoice.getCustomerId()).getPoints());
        invoiceViewModel.setInvoiceItems(invoiceItemsList);

        return invoiceViewModel;
    }

    private LevelUpInputModel convertLevelUpToInputModel(LevelUpViewModel levelUpViewModel){
        LevelUpInputModel levelUpInputModel = new LevelUpInputModel();
        levelUpInputModel.setLevelUpId(levelUpViewModel.getLevelUpId());
        levelUpInputModel.setCustomerId(levelUpViewModel.getCustomer().getCustomerId());
        levelUpInputModel.setMemberDate(levelUpViewModel.getMemberDate());
        levelUpInputModel.setPoints(levelUpViewModel.getPoints());
        return levelUpInputModel;
    }
}
