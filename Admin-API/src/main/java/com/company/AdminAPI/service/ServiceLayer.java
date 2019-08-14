package com.company.AdminAPI.service;

import com.company.AdminAPI.exception.NotFoundException;
import com.company.AdminAPI.util.feign.*;
import com.company.AdminAPI.views.*;
import com.company.AdminAPI.views.input.InventoryInputModel;
import com.company.AdminAPI.views.input.InvoiceInputModel;
import com.company.AdminAPI.views.input.LevelUpInputModel;
import com.company.AdminAPI.views.output.InventoryViewModel;
import com.company.AdminAPI.views.output.InvoiceItemViewModel;
import com.company.AdminAPI.views.output.InvoiceViewModel;
import com.company.AdminAPI.views.output.LevelUpViewModel;
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
    public CustomerViewModel saveCustomer(CustomerViewModel customerViewModel){
        System.out.println("Contacting Customer Service client to add customer...");
        return customerClient.createCustomer(customerViewModel);
    }

    public CustomerViewModel findCustomer(int customerId){
        System.out.println("Contacting Customer Service client to get customer...");
        return customerClient.getCustomer(customerId);
    }

    public List<CustomerViewModel> findAllCustomers(){
        System.out.println("Contacting Customer Service client to get all customers...");
        return customerClient.getAllCustomers();
    }

    public CustomerViewModel updateCustomer(CustomerViewModel customerViewModel){
        System.out.println("Contacting Customer Service client to update customer...");
        return customerClient.updateCustomer(customerViewModel, customerViewModel.getCustomerId());
    }

    public String removeCustomer(int customerId){
        System.out.println("Contacting Customer Service client to delete customer...");
        return customerClient.deleteCustomer(customerId);
    }
    //
    // Product Service Methods
    // --------------------- //
    public ProductViewModel saveProduct(ProductViewModel productViewModel){
        System.out.println("Contacting Product Service client to save product...");
        return productClient.createProduct(productViewModel);
    }

    public ProductViewModel findProduct(int productId){
        System.out.println("Contacting Product Service client to get product...");
        return productClient.getProduct(productId);
    }

    public List<ProductViewModel> findAllProducts(){
        System.out.println("Contacting Product Service client to get all products...");
        return productClient.getAllProducts();
    }

    public ProductViewModel updateProduct(ProductViewModel productViewModel){
        System.out.println("Contacting Product Service client to update product...");
        return productClient.updateProduct(productViewModel, productViewModel.getProductId());
    }

    public String removeProduct(int productId){
        System.out.println("Contacting Product Service client to remove product...");
        return productClient.deleteProduct(productId);
    }
    //
    // Level Up! Service Methods
    // --------------------- //
    public LevelUpViewModel saveLevelUp(LevelUpInputModel levelUpInputModel){
        // Checking if Customer exists - throws an exception if not
        checkForCustomer(levelUpInputModel.getCustomerId());

        // Creating Level Up!
        System.out.println("Contacting Level Up! Service client to save Level Up! entry...");
        return buildLevelUpViewModel(levelUpClient.createLevelUp(levelUpInputModel));
    }

    public LevelUpViewModel findLevelUp(int levelUpId){
        System.out.println("Contacting Level Up! Service client to get Level Up! entry...");
        return buildLevelUpViewModel(levelUpClient.getLevelUp(levelUpId));
    }

    public LevelUpViewModel findLevelUpByCustomerId(int customerId){
        System.out.println("Contacting Level Up! Service client to get Level Up! entry...");
        return buildLevelUpViewModel(levelUpClient.getLevelUpByCustomerId(customerId));
    }

    public List<LevelUpViewModel> findAllLevelUps(){
        // Getting inventory list
        System.out.println("Contacting Level Up! Service client to get all Level Up! entries...");
        List<LevelUpInputModel> fromLevelUpService = levelUpClient.getAllLevelUps();
        // Building ViewModels
        List<LevelUpViewModel> levelUpViewModels = new ArrayList<>();
        fromLevelUpService.forEach(levelUp -> levelUpViewModels.add(buildLevelUpViewModel(levelUp)));

        return levelUpViewModels;
    }

    public LevelUpViewModel updateLevelUp(LevelUpInputModel levelUpInputModel){
        // Checking if customer exists - throws an exception if not
        checkForCustomer(levelUpInputModel.getCustomerId());

        // Updating LevelUp!
        System.out.println("Contacting Level Up! Service client to update Level Up! entry...");
        return buildLevelUpViewModel(levelUpClient.updateLevelUp(levelUpInputModel, levelUpInputModel.getLevelUpId()));
    }

    public String removeLevelUp(int levelUpId){
        System.out.println("Contacting Level Up! Service client to remove Level Up! entry...");
        return levelUpClient.deleteLevelUp(levelUpId);
    }
    //
    // Inventory Service Methods
    // --------------------- //
    public InventoryViewModel saveInventory(InventoryInputModel inventoryInputModel){
        // Checking if product exists - throws an exception if not
        checkForProduct(inventoryInputModel.getProductId());

        // Creating Inventory
        System.out.println("Contacting Inventory Service client to create inventory...");
        return buildInventoryViewModel(inventoryClient.createInventory(inventoryInputModel));
    }

    public InventoryViewModel findInventory(int inventoryId){
        System.out.println("Contacting Inventory Service client to get inventory...");
        return buildInventoryViewModel(inventoryClient.getInventory(inventoryId));
    }

    public List<InventoryViewModel> findAllInventories(){
        // Getting inventory list
        System.out.println("Contacting Inventory Service client to get all inventories...");
        List<InventoryInputModel> fromInventoryService = inventoryClient.getAllInventories();
        // Building ViewModels
        List<InventoryViewModel> inventoryViewModels = new ArrayList<>();
        fromInventoryService.forEach(inventory -> inventoryViewModels.add(buildInventoryViewModel(inventory)));

        return inventoryViewModels;
    }

    public InventoryViewModel updateInventory(InventoryInputModel inventoryInputModel){
        // Checking if product exists - throws an exception if not
        checkForProduct(inventoryInputModel.getProductId());

        // Updating Inventory
        System.out.println("Contacting Inventory Service client to update inventory...");
        return buildInventoryViewModel(inventoryClient.updateInventory(inventoryInputModel, inventoryInputModel.getInventoryId()));
    }

    public String removeInventory(int inventoryId){
        System.out.println("Contacting Inventory Service client to delete inventory...");
        return inventoryClient.deleteInventory(inventoryId);
    }
    //
    // Invoice Service Methods
    // --------------------- //
    public InvoiceViewModel saveInvoice(InvoiceInputModel invoiceInputModel){

        // initializing...
        InvoiceViewModel invoiceViewModel = new InvoiceViewModel();
        List<InvoiceItemViewModel> invoiceItems = new ArrayList<>();
        int points;
        List<BigDecimal> totalPrice = new ArrayList<>();

        // Checking if customer exists - throws an exception if not
        checkForCustomer(invoiceInputModel.getCustomerId());

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
            InvoiceItemViewModel invoiceItemViewModel = new InvoiceItemViewModel();
            invoiceItemViewModel.setInvoiceId(invoiceItem.getInvoiceId());
            invoiceItemViewModel.setInvoiceItemId(invoiceItem.getInvoiceItemId());
            invoiceItemViewModel.setListPrice(invoiceItem.getListPrice());
            invoiceItemViewModel.setQuantity(invoiceItem.getQuantity());
            invoiceItemViewModel.setInventory(findInventory(invoiceItem.getInventoryId()));
            invoiceItems.add(invoiceItemViewModel);
        });
        invoiceViewModel.setInvoiceItems(invoiceItems);

        // LevelUp! points
        int result = totalPrice.stream().reduce(BigDecimal.ZERO, BigDecimal::add).intValue();
        points = (result/50)*10;

        LevelUpViewModel currentPoints = findLevelUpByCustomerId(invoiceViewModel.getCustomer().getCustomerId());
        currentPoints.setPoints(currentPoints.getPoints()+points);
        currentPoints = updateLevelUp(convertLevelUptoInputModel(currentPoints));

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

    public InvoiceViewModel updateInvoice(InvoiceInputModel invoiceInputModel){
        // Simply Updates Invoice without adjusting points
            //Member points adjustments should be done manually

        // Check for Customer
        checkForCustomer(invoiceInputModel.getCustomerId());

        // Check for Products
        invoiceInputModel.getInvoiceItems().forEach(invoiceItem -> {
            //Checking Inventory
            InventoryViewModel inventory = findInventory(invoiceItem.getInventoryId());

            //Checking Quantity
            if(invoiceItem.getQuantity()>inventory.getQuantity()){
                throw new IllegalArgumentException("Inventory Id ["+invoiceItem.getInventoryId()+
                        "]: Only "+inventory.getQuantity()+" items available at storage.");
            }
        });

        // Updating Invoice
        System.out.println("Contacting Invoice Service client to update invoice...");
        return buildInvoiceViewModel(invoiceClient.updateInvoice(invoiceInputModel, invoiceInputModel.getInvoiceId()));
    }

    public String removeInvoice(int invoiceId){
        System.out.println("Contacting Invoice Service client to delete invoice...");
        return invoiceClient.deleteInvoice(invoiceId);
    }

    //
    // HELPER METHODS
    private void checkForProduct(int productId){
        System.out.println("Checking if product exists...");
        try{
            findProduct(productId);
        } catch (FeignException e){
            System.out.println("...product wasn't found in DB!");
            throw new NotFoundException("Product doesn't exist! Create the product first using: [POST] 'uri=/products' endpoint.");
        }
        System.out.println("...product found in DB!");
    }

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
        List<InvoiceItemViewModel> invoiceItemsList = new ArrayList<>();

        //InvoiceItems
        invoice.getInvoiceItems().forEach(invoiceItem -> {
            InvoiceItemViewModel invoiceItemViewModel = new InvoiceItemViewModel();
            //Persisting
            invoiceItemViewModel.setInvoiceId(invoiceItem.getInvoiceId());
            invoiceItemViewModel.setInvoiceItemId(invoiceItem.getInvoiceItemId());
            invoiceItemViewModel.setQuantity(invoiceItem.getQuantity());
            invoiceItemViewModel.setListPrice(invoiceItem.getListPrice());
            invoiceItemViewModel.setInventory(findInventory(invoiceItem.getInventoryId()));
            //Adding to list
            invoiceItemsList.add(invoiceItemViewModel);
        });

        //persisting
        invoiceViewModel.setInvoiceId(invoice.getInvoiceId());
        invoiceViewModel.setPurchaseDate(invoice.getPurchaseDate());
        invoiceViewModel.setCustomer(findCustomer(invoice.getCustomerId()));
        invoiceViewModel.setMemberPoints(findLevelUpByCustomerId(invoice.getCustomerId()).getPoints());
        invoiceViewModel.setInvoiceItems(invoiceItemsList);

        return invoiceViewModel;
    }

    private LevelUpInputModel convertLevelUptoInputModel(LevelUpViewModel levelUpViewModel){
        LevelUpInputModel levelUpInputModel = new LevelUpInputModel();
        levelUpInputModel.setLevelUpId(levelUpViewModel.getLevelUpId());
        levelUpInputModel.setCustomerId(levelUpViewModel.getCustomer().getCustomerId());
        levelUpInputModel.setMemberDate(levelUpViewModel.getMemberDate());
        levelUpInputModel.setPoints(levelUpViewModel.getPoints());
        return levelUpInputModel;
    }
}
