package com.company.invoiceservice.controller;

import com.company.invoiceservice.service.ServiceLayer;
import com.company.invoiceservice.views.InvoiceViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RefreshScope
@CacheConfig(cacheNames = {"invoices"})
@RequestMapping(value = "/invoices")
public class InvoiceController {

    @Autowired
    ServiceLayer serviceLayer;

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<InvoiceViewModel> getAllInvoices(){
        return serviceLayer.findAllInvoices();
    }

    @CachePut(key = "#result.getInvoiceId()")
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public InvoiceViewModel createInvoice(@RequestBody @Valid InvoiceViewModel invoiceViewModel){
        return serviceLayer.saveInvoice(invoiceViewModel);
    }

    @Cacheable
    @GetMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public InvoiceViewModel getInvoice(@PathVariable int id) {
        System.out.println("fetching from DB...");
        return serviceLayer.findInvoice(id);
    }

    @GetMapping(value = "/customer/{customerId}")
    @ResponseStatus(value = HttpStatus.OK)
    public List<InvoiceViewModel> getInvoicesByCustomer(@PathVariable int customerId){
        return serviceLayer.findInvoicesByCustomer(customerId);
    }

    @CacheEvict(key = "#id")
    @PutMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public InvoiceViewModel updateInvoice(@RequestBody @Valid InvoiceViewModel invoiceViewModel, @PathVariable int id) {
        if(id!=invoiceViewModel.getInvoiceId()){
            throw new IllegalArgumentException("Invoice ID in path must match with request body!");
        }
        return serviceLayer.updateInvoice(invoiceViewModel);
    }

    @CacheEvict
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public String deleteInvoice(@PathVariable int id) {
        return serviceLayer.removeInvoice(id);
    }
}