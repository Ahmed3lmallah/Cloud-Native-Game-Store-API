package com.company.RetailAPI.util.feign;

import com.company.RetailAPI.views.input.InvoiceInputModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@FeignClient(name = "invoice-service")
public interface InvoiceClient {

    @RequestMapping(value = "/invoices", method = RequestMethod.GET)
    List<InvoiceInputModel> getAllInvoices();

    @RequestMapping(value = "/invoices", method = RequestMethod.POST)
    InvoiceInputModel createInvoice(@RequestBody @Valid InvoiceInputModel invoiceInputModel);

    @RequestMapping(value = "/invoices/{id}", method = RequestMethod.GET)
    InvoiceInputModel getInvoice(@PathVariable int id);

    @RequestMapping(value = "/invoices/customer/{customerId}", method = RequestMethod.GET)
    List<InvoiceInputModel> getInvoicesByCustomer(@PathVariable int customerId);

}
