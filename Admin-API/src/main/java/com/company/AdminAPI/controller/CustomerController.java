package com.company.AdminAPI.controller;

import com.company.AdminAPI.service.ServiceLayer;
import com.company.AdminAPI.views.CustomerViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RefreshScope
@RequestMapping(value = "/customers")
public class CustomerController {

    @Autowired
    ServiceLayer serviceLayer;

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<CustomerViewModel> getAllCustomers(){
        return serviceLayer.findAllCustomers();
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public CustomerViewModel createCustomer(@RequestBody @Valid CustomerViewModel customerViewModel){
        return serviceLayer.saveCustomer(customerViewModel);
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public CustomerViewModel getCustomer(@PathVariable int id) {
        return serviceLayer.findCustomer(id);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public CustomerViewModel updateCustomer(@RequestBody @Valid CustomerViewModel customerViewModel, @PathVariable int id) {
        if(id!=customerViewModel.getCustomerId()){
            throw new IllegalArgumentException("Customer ID in path must match with request body!");
        }
        return serviceLayer.updateCustomer(customerViewModel);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public String deleteCustomer(@PathVariable int id) {
        return serviceLayer.removeCustomer(id);
    }
}
