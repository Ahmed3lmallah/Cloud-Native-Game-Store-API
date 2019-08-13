package com.company.invoiceservice.dao;

import com.company.invoiceservice.dto.Invoice;
import com.company.invoiceservice.dto.InvoiceItem;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class InvoiceItemDaoTest {

    @Autowired
    InvoiceDao invoiceDao;

    @Autowired InvoiceItemDao invoiceItemDao;

    @Before
    public void setUp() throws Exception {
        List<Invoice> invoices = invoiceDao.getAllInvoices();
        invoices.forEach(invoice -> invoiceDao.getInvoice(invoice.getInvoiceId()));

        List<InvoiceItem> invoiceItems = invoiceItemDao.getAllInvoiceItems();
        invoiceItems.forEach(invoiceItem -> invoiceItemDao.deleteInvoiceItem(invoiceItem.getInvoiceItemId()));
    }

    @Test
    public void addGetInvoiceItem() {
        //Arranging
        //Invoice
        Invoice invoice = new Invoice();
        invoice.setCustomerId(1);
        invoice.setPurchaseDate(LocalDate.of(2019,10,05));
        invoice = invoiceDao.addInvoice(invoice);

        //InvoiceItem
        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.setInvoiceId(invoice.getInvoiceId());
        invoiceItem.setInventoryId(1);
        invoiceItem.setListPrice(new BigDecimal(250.00).setScale(2));
        invoiceItem.setQuantity(5);

        //Adding InvoiceItem
        InvoiceItem fromAdd = invoiceItemDao.addInvoiceItem(invoiceItem);

        //Getting InvoiceItem
        InvoiceItem fromGet = invoiceItemDao.getInvoiceItem(fromAdd.getInvoiceItemId());

        //Asserting
        assertEquals(fromAdd, fromGet);
    }

    @Test
    public void getAllInvoiceItemsAndByInvoiceId() {
        //Arranging
        //Invoice 1
        Invoice invoice = new Invoice();
        invoice.setCustomerId(1);
        invoice.setPurchaseDate(LocalDate.of(2019,10,05));
        invoice = invoiceDao.addInvoice(invoice);

        //InvoiceItem for Invoice 1
        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.setInvoiceId(invoice.getInvoiceId());
        invoiceItem.setInventoryId(1);
        invoiceItem.setListPrice(new BigDecimal(250.00).setScale(2));
        invoiceItem.setQuantity(5);
        invoiceItem = invoiceItemDao.addInvoiceItem(invoiceItem);

        //Invoice 2
        Invoice invoice2 = new Invoice();
        invoice2.setCustomerId(1);
        invoice2.setPurchaseDate(LocalDate.of(2019,10,05));
        invoice2 = invoiceDao.addInvoice(invoice2);

        //InvoiceItem for Invoice 2
        InvoiceItem invoiceItem2 = new InvoiceItem();
        invoiceItem2.setInvoiceId(invoice2.getInvoiceId());
        invoiceItem2.setInventoryId(1);
        invoiceItem2.setListPrice(new BigDecimal(250.00).setScale(2));
        invoiceItem2.setQuantity(5);
        invoiceItem2 = invoiceItemDao.addInvoiceItem(invoiceItem2);

        //Asserting
        assertEquals(invoiceItemDao.getAllInvoiceItems().size(),2);
        assertEquals(invoiceItemDao.getAllInvoiceItems().get(0),invoiceItem);

        assertEquals(invoiceItemDao.getInvoiceItemsByInvoiceId(invoice2.getInvoiceId()).size(),1);
        assertEquals(invoiceItemDao.getInvoiceItemsByInvoiceId(invoice2.getInvoiceId()).get(0),invoiceItem2);
    }

    @Test
    public void updateInvoiceItem() {
        //Arranging
        //Invoice
        Invoice invoice = new Invoice();
        invoice.setCustomerId(1);
        invoice.setPurchaseDate(LocalDate.of(2019,10,05));
        invoice = invoiceDao.addInvoice(invoice);

        //InvoiceItem for Invoice
        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.setInvoiceId(invoice.getInvoiceId());
        invoiceItem.setInventoryId(1);
        invoiceItem.setListPrice(new BigDecimal(250.00).setScale(2));
        invoiceItem.setQuantity(5);
        invoiceItem = invoiceItemDao.addInvoiceItem(invoiceItem);

        //Updating InvoiceItem
        invoiceItem.setQuantity(10);
        invoiceItemDao.updateInvoiceItem(invoiceItem);

        //Asserting
        assertEquals(invoiceItemDao.getInvoiceItem(invoiceItem.getInvoiceItemId()),invoiceItem);
    }

    @Test
    public void deleteInvoiceItem() {
        //Arranging
        //Invoice
        Invoice invoice = new Invoice();
        invoice.setCustomerId(1);
        invoice.setPurchaseDate(LocalDate.of(2019,10,05));
        invoice = invoiceDao.addInvoice(invoice);

        //InvoiceItem for Invoice
        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.setInvoiceId(invoice.getInvoiceId());
        invoiceItem.setInventoryId(1);
        invoiceItem.setListPrice(new BigDecimal(250.00).setScale(2));
        invoiceItem.setQuantity(5);
        invoiceItem = invoiceItemDao.addInvoiceItem(invoiceItem);

        //Deleting
        invoiceItemDao.deleteInvoiceItem(invoiceItem.getInvoiceItemId());

        //Asserting
        assertNull(invoiceItemDao.getInvoiceItem(invoiceItem.getInvoiceItemId()));
    }
}