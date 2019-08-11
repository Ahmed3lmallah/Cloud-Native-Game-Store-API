package com.company.customerservice.dao;

import com.company.customerservice.dto.Customer;

import java.util.List;

public interface CustomerDao {

    Customer addCustomer(Customer customer);
    Customer getCustomer(int customerId);
    List<Customer> getAllCustomers();
    void updateCustomer(Customer customer);
    void deleteCustomer(int customerId);

}
