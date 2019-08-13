package com.company.invoiceservice.service;

import com.company.invoiceservice.dao.InvoiceDao;
import com.company.invoiceservice.dao.InvoiceItemDao;
import com.company.invoiceservice.dto.Invoice;
import com.company.invoiceservice.dto.InvoiceItem;
import com.company.invoiceservice.exception.NotFoundException;
import com.company.invoiceservice.views.InvoiceViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceLayer {

    private InvoiceDao invoiceDao;
    private InvoiceItemDao invoiceItemDao;

    @Autowired
    public ServiceLayer(InvoiceDao invoiceDao, InvoiceItemDao invoiceItemDao) {
        this.invoiceDao = invoiceDao;
        this.invoiceItemDao = invoiceItemDao;
    }

    //
    // Service Layer Methods
    // --------------------- //
    @Transactional
    public InvoiceViewModel saveInvoice(InvoiceViewModel invoiceViewModel){
        Invoice invoice = viewModelToModel(invoiceViewModel);

        invoice = invoiceDao.addInvoice(invoice);

        invoiceViewModel.setInvoiceId(invoice.getInvoiceId());

        invoiceViewModel.getInvoiceItems().forEach(invoiceItem -> {
            invoiceItem.setInvoiceId(invoiceViewModel.getInvoiceId());
            invoiceItemDao.addInvoiceItem(invoiceItem);
        });

        return invoiceViewModel;
    }

    @Transactional
    public InvoiceViewModel findInvoice(int invoiceId){
        Invoice invoice = invoiceDao.getInvoice(invoiceId);
        if(invoice==null){
            throw new NotFoundException("Invoice ID cannot be found in DB!");
        } else {
            return buildInvoiceViewModel(invoice);
        }
    }

    @Transactional
    public List<InvoiceViewModel> findAllInvoices(){
        List<Invoice> invoices = invoiceDao.getAllInvoices();
        List<InvoiceViewModel> invoiceViewModels = new ArrayList<>();

        invoices.forEach(invoice -> invoiceViewModels.add(buildInvoiceViewModel(invoice)));

        return invoiceViewModels;
    }

    @Transactional
    public InvoiceViewModel updateInvoice(InvoiceViewModel invoiceViewModel){
        //Checking if Inventory exists
        findInvoice(invoiceViewModel.getInvoiceId());

        //Updating Invoice
        invoiceDao.updateInvoice(viewModelToModel(invoiceViewModel));

        //Updating Invoice Items
            //Deleting All InvoiceItems
            List<InvoiceItem> invoiceItems = invoiceItemDao.getInvoiceItemsByInvoiceId(invoiceViewModel.getInvoiceId());
            invoiceItems.forEach(invoiceItem -> invoiceItemDao.deleteInvoiceItem(invoiceItem.getInvoiceItemId()));

            //Adding InvoiceItems
            invoiceViewModel.getInvoiceItems().forEach(invoiceItem -> {
                invoiceItem.setInvoiceId(invoiceViewModel.getInvoiceId());
                invoiceItemDao.addInvoiceItem(invoiceItem);
            });

        //Retrieving
        return findInvoice(invoiceViewModel.getInvoiceId());
    }

    @Transactional
    public String removeInvoice(int invoiceId){
        //Checking if inventory exists
        findInvoice(invoiceId);

        //Deleting InvoiceItems
        List<InvoiceItem> invoiceItems = invoiceItemDao.getInvoiceItemsByInvoiceId(invoiceId);
        invoiceItems.forEach(invoiceItem -> invoiceItemDao.deleteInvoiceItem(invoiceItem.getInvoiceItemId()));

        //Deleting
        invoiceDao.deleteInvoice(invoiceId);

        return "Invoice ["+invoiceId+"] deleted successfully!";
    }

    //
    // Helper Methods
    // --------------------- //
    private Invoice viewModelToModel(InvoiceViewModel ivm){
        Invoice invoice = new Invoice();
        invoice.setInvoiceId(ivm.getInvoiceId());
        invoice.setCustomerId(ivm.getCustomerId());
        invoice.setPurchaseDate(ivm.getPurchaseDate());
        return invoice;
    }

    private InvoiceViewModel buildInvoiceViewModel(Invoice invoice){
        InvoiceViewModel ivm = new InvoiceViewModel();
        ivm.setInvoiceId(invoice.getInvoiceId());
        ivm.setCustomerId(invoice.getCustomerId());
        ivm.setPurchaseDate(invoice.getPurchaseDate());
        ivm.setInvoiceItems(invoiceItemDao.getInvoiceItemsByInvoiceId(invoice.getInvoiceId()));
        return ivm;
    }


}
