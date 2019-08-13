package com.company.AdminAPI.service;

import com.company.AdminAPI.exception.NotFoundException;
import com.company.AdminAPI.util.feign.CustomerClient;
import com.company.AdminAPI.util.feign.InventoryClient;
import com.company.AdminAPI.util.feign.ProductClient;
import com.company.AdminAPI.util.messages.Inventory;
import com.company.AdminAPI.views.CustomerViewModel;
import com.company.AdminAPI.views.InventoryViewModel;
import com.company.AdminAPI.views.ProductViewModel;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceLayer {

    private CustomerClient customerClient;
    private ProductClient productClient;
    private InventoryClient inventoryClient;

    @Autowired
    public ServiceLayer(CustomerClient customerClient, ProductClient productClient, InventoryClient inventoryClient) {
        this.customerClient = customerClient;
        this.productClient = productClient;
        this.inventoryClient = inventoryClient;
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
    // Inventory Service Methods
    // --------------------- //
    public InventoryViewModel saveInventory(InventoryViewModel inventoryViewModel){
        // Checking if product exists - throws an exception if not
        checkForProduct(inventoryViewModel.getProduct().getProductId());

        // Persisting Inventory Item
        Inventory inventory = new Inventory();
        inventory.setInventoryID(inventoryViewModel.getInventoryID());
        inventory.setProductID(inventoryViewModel.getProduct().getProductId());
        inventory.setQuantity(inventoryViewModel.getQuantity());

        // Creating Inventory
        System.out.println("Contacting Inventory Service client to create inventory...");
        return buildInventoryViewModel(inventoryClient.createInventory(inventory));
    }

    public InventoryViewModel findInventory(int inventoryId){
        System.out.println("Contacting Inventory Service client to get inventory...");
        return buildInventoryViewModel(inventoryClient.getInventory(inventoryId));
    }

    public List<InventoryViewModel> findAllInventories(){
        // Getting inventory list
        System.out.println("Contacting Inventory Service client to get all inventories...");
        List<Inventory> fromInventoryService = inventoryClient.getAllInventories();
        // Building ViewModels
        List<InventoryViewModel> inventoryViewModels = new ArrayList<>();
        fromInventoryService.forEach(inventory -> inventoryViewModels.add(buildInventoryViewModel(inventory)));

        return inventoryViewModels;
    }

    public InventoryViewModel updateInventory(InventoryViewModel inventoryViewModel){
        // Checking if product exists - throws an exception if not
        checkForProduct(inventoryViewModel.getProduct().getProductId());

        // Persisting Inventory Item
        Inventory inventory = new Inventory();
        inventory.setInventoryID(inventoryViewModel.getInventoryID());
        inventory.setProductID(inventoryViewModel.getProduct().getProductId());
        inventory.setQuantity(inventoryViewModel.getQuantity());

        // Updating Inventory
        System.out.println("Contacting Inventory Service client to update inventory...");
        return buildInventoryViewModel(inventoryClient.updateInventory(inventory, inventory.getInventoryID()));
    }

    public String removeInventory(int inventoryId){
        System.out.println("Contacting Inventory Service client to delete inventory...");
        return inventoryClient.deleteInventory(inventoryId);
    }

    //
    // HELPER METHOD
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

    private InventoryViewModel buildInventoryViewModel(Inventory inventory){
        // Persisting Inventory
        InventoryViewModel inventoryViewModel = new InventoryViewModel();
        inventoryViewModel.setInventoryID(inventory.getInventoryID());
        inventoryViewModel.setQuantity(inventory.getQuantity());
        // Getting Product
        inventoryViewModel.setProduct(findProduct(inventory.getProductID()));
        return inventoryViewModel;
    }
}
