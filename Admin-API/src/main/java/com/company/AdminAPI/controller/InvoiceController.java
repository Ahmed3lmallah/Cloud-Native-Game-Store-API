package com.company.AdminAPI.controller;

import com.company.AdminAPI.service.ServiceLayer;
import com.company.AdminAPI.views.input.InvoiceInputModel;
import com.company.AdminAPI.views.output.InvoiceViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RefreshScope
@RequestMapping(value = "/invoices")
public class InvoiceController {

    @Autowired
    ServiceLayer serviceLayer;

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<InvoiceViewModel> getAllInvoices(){
        return serviceLayer.findAllInvoices();
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public InvoiceViewModel createInvoice(@RequestBody @Valid InvoiceInputModel invoiceInputModel){
        return serviceLayer.saveInvoice(invoiceInputModel);
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public InvoiceViewModel getInvoice(@PathVariable int id) {
        return serviceLayer.findInvoice(id);
    }

    @GetMapping(value = "/customer/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public List<InvoiceViewModel> getInvoicesByCustomerId(@PathVariable int id) {
        return serviceLayer.findInvoicesByCustomer(id);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public InvoiceViewModel updateInvoice(@RequestBody @Valid InvoiceInputModel invoiceInputModel, @PathVariable int id) {
        if(id!= invoiceInputModel.getInvoiceId()){
            throw new IllegalArgumentException("Invoice ID in path must match with request body!");
        }
        return serviceLayer.updateInvoice(invoiceInputModel);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public String deleteInvoice(@PathVariable int id) {
        return serviceLayer.removeInvoice(id);
    }
}