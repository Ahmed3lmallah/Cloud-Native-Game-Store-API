package com.company.AdminAPI.service;

import com.company.AdminAPI.util.feign.CustomerClient;
import com.company.AdminAPI.util.feign.ProductClient;
import com.company.AdminAPI.views.CustomerViewModel;
import com.company.AdminAPI.views.ProductViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceLayer {

    private CustomerClient customerClient;
    private ProductClient productClient;

    @Autowired
    public ServiceLayer(CustomerClient customerClient, ProductClient productClient) {
        this.customerClient = customerClient;
        this.productClient = productClient;
    }

    //
    // Customer Service Methods
    // --------------------- //
    public CustomerViewModel saveCustomer(CustomerViewModel customerViewModel){
        System.out.println("Contacting Customer Service client...");
        return customerClient.createCustomer(customerViewModel);
    }

    public CustomerViewModel findCustomer(int customerId){
        System.out.println("Contacting Customer Service client...");
        return customerClient.getCustomer(customerId);
    }

    public List<CustomerViewModel> findAllCustomers(){
        System.out.println("Contacting Customer Service client...");
        return customerClient.getAllCustomers();
    }

    public CustomerViewModel updateCustomer(CustomerViewModel customerViewModel){
        System.out.println("Contacting Customer Service client...");
        return customerClient.updateCustomer(customerViewModel, customerViewModel.getCustomerId());
    }

    public String removeCustomer(int customerId){
        System.out.println("Contacting Customer Service client...");
        return customerClient.deleteCustomer(customerId);
    }
    //
    // Product Service Methods
    // --------------------- //
    public ProductViewModel saveProduct(ProductViewModel productViewModel){
        System.out.println("Contacting Product Service client...");
        return productClient.createProduct(productViewModel);
    }

    public ProductViewModel findProduct(int productId){
        System.out.println("Contacting Product Service client...");
        return productClient.getProduct(productId);
    }

    public List<ProductViewModel> findAllProducts(){
        System.out.println("Contacting Product Service client...");
        return productClient.getAllProducts();
    }

    public ProductViewModel updateProduct(ProductViewModel productViewModel){
        System.out.println("Contacting Product Service client...");
        return productClient.updateProduct(productViewModel, productViewModel.getProductId());
    }

    public String removeProduct(int productId){
        System.out.println("Contacting Product Service client...");
        return productClient.deleteProduct(productId);
    }
}
