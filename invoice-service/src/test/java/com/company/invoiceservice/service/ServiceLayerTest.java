package com.company.invoiceservice.service;

import com.company.invoiceservice.dao.InvoiceDao;
import com.company.invoiceservice.dao.InvoiceDaoJdbcTemplateImpl;
import com.company.invoiceservice.dao.InvoiceItemDao;
import com.company.invoiceservice.dao.InvoiceItemJdbcTemplateImpl;
import com.company.invoiceservice.dto.Invoice;
import com.company.invoiceservice.dto.InvoiceItem;
import com.company.invoiceservice.exception.NotFoundException;
import com.company.invoiceservice.views.InvoiceViewModel;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ServiceLayerTest {

    private ServiceLayer serviceLayer;
    private InvoiceDao invoiceDao;
    private InvoiceItemDao invoiceItemDao;

    @Before
    public void setUp() throws Exception {
        setUpInvoiceDaoMock();
        setUpInvoiceItemDaoMock();
        serviceLayer = new ServiceLayer(invoiceDao, invoiceItemDao);
    }

    @Test
    public void saveInvoice() {
        //Input
            //Invoice Item
            InvoiceItem invoiceItem = new InvoiceItem();
            invoiceItem.setInventoryId(1);
            invoiceItem.setListPrice(new BigDecimal(15.25).setScale(2));
            invoiceItem.setQuantity(3);
            //List of Invoice Items
            List<InvoiceItem> invoiceItems = new ArrayList<>();
            invoiceItems.add(invoiceItem);
            //Invoice
            InvoiceViewModel input = new InvoiceViewModel();
            input.setCustomerId(1);
            input.setPurchaseDate(LocalDate.of(2019,10,05));
            input.setInvoiceItems(invoiceItems);

        //From Service
        InvoiceViewModel fromService = serviceLayer.saveInvoice(input);

        //expected
        input.setInvoiceId(1);
        invoiceItem.setInvoiceId(1);
        invoiceItem.setInvoiceItemId(2);

        //Asserting
        assertEquals(input, fromService);
    }

    @Test
    public void findInvoice() {
        //Expected
            //Invoice Item
            InvoiceItem expectedInvoiceItemOut = new InvoiceItem();
            expectedInvoiceItemOut.setInvoiceItemId(2);
            expectedInvoiceItemOut.setInvoiceId(1);
            expectedInvoiceItemOut.setInventoryId(1);
            expectedInvoiceItemOut.setListPrice(new BigDecimal(15.25).setScale(2));
            expectedInvoiceItemOut.setQuantity(3);
            List<InvoiceItem> invoiceItems = new ArrayList<>();
            invoiceItems.add(expectedInvoiceItemOut);
            //Invoice
            InvoiceViewModel expectedOutput = new InvoiceViewModel();
            expectedOutput.setInvoiceId(1);
            expectedOutput.setCustomerId(1);
            expectedOutput.setPurchaseDate(LocalDate.of(2019,10,05));
            expectedOutput.setInvoiceItems(invoiceItems);

        //From Service
        InvoiceViewModel fromService = serviceLayer.findInvoice(1);

        //Asserting
        assertEquals(expectedOutput, fromService);
    }

    @Test
    public void findAllInvoices() {
        //Expected
            //Invoice Item
            InvoiceItem expectedInvoiceItemOut = new InvoiceItem();
            expectedInvoiceItemOut.setInvoiceItemId(2);
            expectedInvoiceItemOut.setInvoiceId(1);
            expectedInvoiceItemOut.setInventoryId(1);
            expectedInvoiceItemOut.setListPrice(new BigDecimal(15.25).setScale(2));
            expectedInvoiceItemOut.setQuantity(3);
            List<InvoiceItem> invoiceItems = new ArrayList<>();
            invoiceItems.add(expectedInvoiceItemOut);
            //Invoice
            InvoiceViewModel expectedOutput = new InvoiceViewModel();
            expectedOutput.setInvoiceId(1);
            expectedOutput.setCustomerId(1);
            expectedOutput.setPurchaseDate(LocalDate.of(2019,10,05));
            expectedOutput.setInvoiceItems(invoiceItems);

        //Asserting
        assertEquals(1,serviceLayer.findAllInvoices().size());
        assertEquals(expectedOutput, serviceLayer.findAllInvoices().get(0));
    }

    @Test(expected = NotFoundException.class)
    public void updateInvoice() {
        //Updating Invoice
        //Expected
            //Invoice Item
            InvoiceItem expectedInvoiceItemOut = new InvoiceItem();
            expectedInvoiceItemOut.setInvoiceItemId(2);
            expectedInvoiceItemOut.setInvoiceId(1);
            expectedInvoiceItemOut.setInventoryId(1);
            expectedInvoiceItemOut.setListPrice(new BigDecimal(15.25).setScale(2));
            expectedInvoiceItemOut.setQuantity(3);
            List<InvoiceItem> invoiceItems = new ArrayList<>();
            invoiceItems.add(expectedInvoiceItemOut);
            //Invoice
            InvoiceViewModel expectedOutput = new InvoiceViewModel();
            expectedOutput.setInvoiceId(1);
            expectedOutput.setCustomerId(1);
            expectedOutput.setPurchaseDate(LocalDate.of(2019,10,05));
            expectedOutput.setInvoiceItems(invoiceItems);

        assertEquals(expectedOutput, serviceLayer.updateInvoice(expectedOutput));

        //A Product that doesn't exist in DB
        //Invoice
        InvoiceViewModel fakeInvoice = new InvoiceViewModel();
        fakeInvoice.setInvoiceId(2);
        fakeInvoice.setCustomerId(1);
        fakeInvoice.setPurchaseDate(LocalDate.of(2019,10,05));
        fakeInvoice.setInvoiceItems(invoiceItems);

        serviceLayer.updateInvoice(fakeInvoice);
    }

    @Test(expected = NotFoundException.class)
    public void removeInvoice() {
        assertEquals(serviceLayer.removeInvoice(1), "Invoice [1] deleted successfully!");

        //A product that doesn't exist in DB
        serviceLayer.removeInvoice(2);
    }

    private void setUpInvoiceDaoMock() {

        invoiceDao = mock(InvoiceDaoJdbcTemplateImpl.class);

        // Output
        Invoice output = new Invoice();
        output.setInvoiceId(1);
        output.setCustomerId(1);
        output.setPurchaseDate(LocalDate.of(2019,10,05));


        // Input
        Invoice input = new Invoice();
        input.setCustomerId(1);
        input.setPurchaseDate(LocalDate.of(2019,10,05));

        // All Invoices
        List<Invoice> invoices = new ArrayList<>();
        invoices.add(output);

        doReturn(output).when(invoiceDao).addInvoice(input);
        doReturn(output).when(invoiceDao).getInvoice(1);
        doReturn(invoices).when(invoiceDao).getAllInvoices();
        doNothing().when(invoiceDao).updateInvoice(input);
    }

    private void setUpInvoiceItemDaoMock() {

        invoiceItemDao = mock(InvoiceItemJdbcTemplateImpl.class);

        // Output
        InvoiceItem output = new InvoiceItem();
        output.setInvoiceItemId(2);
        output.setInvoiceId(1);
        output.setInventoryId(1);
        output.setListPrice(new BigDecimal(15.25).setScale(2));
        output.setQuantity(3);

        // Input
        InvoiceItem input = new InvoiceItem();
        input.setInvoiceId(1);
        input.setInventoryId(1);
        input.setListPrice(new BigDecimal(15.25).setScale(2));
        input.setQuantity(3);

        // All InvoiceItems
        List<InvoiceItem> invoiceItems = new ArrayList<>();
        invoiceItems.add(output);

        doReturn(output).when(invoiceItemDao).addInvoiceItem(input);
        doReturn(invoiceItems).when(invoiceItemDao).getInvoiceItemsByInvoiceId(1);
    }
}