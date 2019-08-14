package com.company.AdminAPI.util.feign;

import com.company.AdminAPI.views.input.InvoiceInputModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.List;

@FeignClient(name = "invoice-service")
public interface InvoiceClient {

    @RequestMapping(value = "/invoice", method = RequestMethod.GET)
    public List<InvoiceInputModel> getAllInvoices();

    @RequestMapping(value = "/invoice", method = RequestMethod.POST)
    public InvoiceInputModel createInvoice(@RequestBody @Valid InvoiceInputModel invoiceInputModel);

    @RequestMapping(value = "/invoice/{id}", method = RequestMethod.GET)
    public InvoiceInputModel getInvoice(@PathVariable int id);

    @RequestMapping(value = "/invoice/{id}", method = RequestMethod.PUT)
    public InvoiceInputModel updateInvoice(@RequestBody @Valid InvoiceInputModel invoiceInputModel, @PathVariable int id);

    @RequestMapping(value = "/invoice/{id}", method = RequestMethod.DELETE)
    public String deleteInvoice(@PathVariable int id);

}
