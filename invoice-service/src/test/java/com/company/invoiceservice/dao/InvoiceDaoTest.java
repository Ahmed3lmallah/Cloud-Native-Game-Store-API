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
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class InvoiceDaoTest {

    @Autowired
    InvoiceDao invoiceDao;

    @Autowired
    InvoiceItemDao invoiceItemDao;

    @Before
    public void setUp() throws Exception {
        List<InvoiceItem> invoiceItems = invoiceItemDao.getAllInvoiceItems();
        invoiceItems.forEach(invoiceItem -> invoiceItemDao.deleteInvoiceItem(invoiceItem.getInvoiceItemId()));

        List<Invoice> invoices = invoiceDao.getAllInvoices();
        invoices.forEach(invoice -> invoiceDao.deleteInvoice(invoice.getInvoiceId()));
    }

    @Test
    public void addGetInvoice() {
        //Arranging
        Invoice invoice = new Invoice();
        invoice.setCustomerId(1);
        invoice.setPurchaseDate(LocalDate.of(2019,10,05));
        //Adding Invoice
        Invoice fromAdd = invoiceDao.addInvoice(invoice);
        //Getting Invoice
        Invoice fromGet = invoiceDao.getInvoice(fromAdd.getInvoiceId());

        //Asserting
        assertEquals(fromAdd, fromGet);
    }

    @Test
    public void getAllInvoicesAndByCustomerId() {
        //Arranging
        Invoice invoice = new Invoice();
        invoice.setCustomerId(1);
        invoice.setPurchaseDate(LocalDate.of(2019,10,05));
        invoice = invoiceDao.addInvoice(invoice);

        //Asserting
        assertEquals(invoiceDao.getAllInvoices().size(),1);
        assertEquals(invoiceDao.getAllInvoices().get(0),invoice);

        assertEquals(invoiceDao.getInvoicesByCustomerId(1).size(),1);
        assertEquals(invoiceDao.getInvoicesByCustomerId(1).get(0),invoice);

    }

    @Test
    public void updateInvoice() {
        //Arranging
        Invoice invoice = new Invoice();
        invoice.setCustomerId(1);
        invoice.setPurchaseDate(LocalDate.of(2019,10,05));
        invoice = invoiceDao.addInvoice(invoice);

        //Updating Invoice
        invoice.setCustomerId(2);
        invoiceDao.updateInvoice(invoice);

        //Asserting
        assertEquals(invoiceDao.getInvoice(invoice.getInvoiceId()),invoice);
    }

    @Test
    public void deleteInvoice() {
        //Arranging
        Invoice invoice = new Invoice();
        invoice.setCustomerId(1);
        invoice.setPurchaseDate(LocalDate.of(2019,10,05));
        invoice = invoiceDao.addInvoice(invoice);

        //Deleting
        invoiceDao.deleteInvoice(invoice.getInvoiceId());

        //Asserting
        assertNull(invoiceDao.getInvoice(invoice.getInvoiceId()));
    }
}