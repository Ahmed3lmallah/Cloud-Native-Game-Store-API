package com.company.RetailAPI.util.feign;

import com.company.RetailAPI.views.input.InvoiceInputModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@FeignClient(name = "invoice-service")
public interface InvoiceClient {

    @RequestMapping(value = "/invoices", method = RequestMethod.GET)
    public List<InvoiceInputModel> getAllInvoices();

    @RequestMapping(value = "/invoices", method = RequestMethod.POST)
    public InvoiceInputModel createInvoice(@RequestBody @Valid InvoiceInputModel invoiceInputModel);

    @RequestMapping(value = "/invoices/{id}", method = RequestMethod.GET)
    public InvoiceInputModel getInvoice(@PathVariable int id);

    @RequestMapping(value = "/invoices/customer/{customerId}", method = RequestMethod.GET)
    public List<InvoiceInputModel> getInvoicesByCustomer(@PathVariable int customerId);

    @RequestMapping(value = "/invoices/{id}", method = RequestMethod.PUT)
    public InvoiceInputModel updateInvoice(@RequestBody @Valid InvoiceInputModel invoiceInputModel, @PathVariable int id);

    @RequestMapping(value = "/invoices/{id}", method = RequestMethod.DELETE)
    public String deleteInvoice(@PathVariable int id);

}
