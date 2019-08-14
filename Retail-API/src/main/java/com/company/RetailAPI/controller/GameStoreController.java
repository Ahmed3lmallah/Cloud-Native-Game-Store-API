package com.company.RetailAPI.controller;

import com.company.RetailAPI.service.ServiceLayer;
import com.company.RetailAPI.views.input.InvoiceInputModel;
import com.company.RetailAPI.views.output.InvoiceViewModel;
import com.company.RetailAPI.views.products.ProductFromInventory;
import com.company.RetailAPI.views.products.ProductFromInvoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GameStoreController {

    @Autowired
    private ServiceLayer serviceLayer;

    //
    // Invoice Features
    // ------------------ //
    @RequestMapping(value = "/invoices", method = RequestMethod.POST)
    public InvoiceViewModel submitInvoice(@RequestBody InvoiceInputModel invoice) {
        return serviceLayer.saveInvoice(invoice);
    }

    @RequestMapping(value = "/invoices/{id}", method = RequestMethod.GET)
    public InvoiceViewModel getInvoiceById(@PathVariable int id) {
        return serviceLayer.findInvoice(id);
    }

    @RequestMapping(value = "/invoices", method = RequestMethod.GET)
    public List<InvoiceViewModel> getAllInvoices() {
        return serviceLayer.findAllInvoices();
    }

    @RequestMapping(value = "/invoices/customer/{id}", method = RequestMethod.GET)
    public List<InvoiceViewModel> getInvoicesByCustomerId(@PathVariable int id) {
        return serviceLayer.findInvoicesByCustomer(id);
    }

    //
    // Product Features
    // ------------------ //
    // From Inventory
    @RequestMapping(value = "/products/inventory", method = RequestMethod.GET)
    public List<ProductFromInventory> getProductsInInventory() {
        return serviceLayer.getAllProductsFromInventory();
    }
    // From Inventory
    @RequestMapping(value = "/products/{id}", method = RequestMethod.GET)
    public ProductFromInventory getProductById(@PathVariable int id) {
        return serviceLayer.getProductFromInventory(id);
}
    // From Invoice
    @RequestMapping(value = "/products/invoice/{id}", method = RequestMethod.GET)
    public List<ProductFromInvoice> getProductByInvoiceId(@PathVariable int id) {
        return serviceLayer.findInvoice(id).getInvoiceItems();
    }

    //
    // LevelUp! Features
    // ------------------ //
    @RequestMapping(value = "/levelup/customer/{id}", method = RequestMethod.GET)
    public int getLevelUpPointsByCustomerId(int id) {
        return serviceLayer.findLevelUpByCustomerId(id).getCustomer().getCustomerId();
    }

}
