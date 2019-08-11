package com.company.AdminAPI.service;

import com.company.AdminAPI.util.feign.CustomerClient;
import com.company.AdminAPI.views.CustomerViewModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceLayer {

    private CustomerClient customerClient;

    public ServiceLayer(CustomerClient customerClient) {
        this.customerClient = customerClient;
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
}
