package com.company.AdminAPI.service;

import com.company.AdminAPI.util.feign.*;
import com.company.AdminAPI.views.CustomerViewModel;
import com.company.AdminAPI.views.ProductViewModel;
import com.company.AdminAPI.views.input.InventoryInputModel;
import com.company.AdminAPI.views.input.InvoiceInputModel;
import com.company.AdminAPI.views.input.InvoiceItem;
import com.company.AdminAPI.views.input.LevelUpInputModel;
import com.company.AdminAPI.views.output.InventoryViewModel;
import com.company.AdminAPI.views.output.InvoiceItemViewModel;
import com.company.AdminAPI.views.output.InvoiceViewModel;
import com.company.AdminAPI.views.output.LevelUpViewModel;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class ServiceLayerTest {

    private ServiceLayer serviceLayer;
    private ProductClient productClient;
    private InventoryClient inventoryClient;
    private CustomerClient customerClient;
    private InvoiceClient invoiceClient;
    private LevelUpClient levelUpClient;

    private CustomerViewModel customer;

    @Before
    public void setUp() {
        setUpCustomerClientMock();
        setUpProductClientMock();
        setUpInventoryClientMock();
        setUpInvoiceClientMock();
        setUpLevelUpClientMock();

        customer.setCity("Alexandria");

        serviceLayer = new ServiceLayer(customerClient, productClient, inventoryClient, levelUpClient, invoiceClient);
    }

    @Test
    public void testCustomer() {
        // No Extra Code to Test
        // Simply Using Feign to Contact Customer Backing Service
    }

    @Test
    public void testProduct() {
        // No Extra Code to Test
        // Simply Using Feign to Contact Product Backing Service
    }

    @Test
    public void testLevelUp() {
        // Test Setup
        // ---------- //
        // input
        LevelUpInputModel input = new LevelUpInputModel();
        input.setCustomerId(1);
        input.setMemberDate(LocalDate.of(2005,5,5));
        input.setPoints(1000);

        // customer
        CustomerViewModel customer = new CustomerViewModel();
        customer.setCustomerId(1);
        customer.setFirstName("Ahmed");
        customer.setLastName("Elmallah");
        customer.setStreet("161 Newkirk");
        customer.setCity("Jersey City");
        customer.setZip("07100");
        customer.setPhone("201-100-2000");
        customer.setEmail("ahmed@elmalah.com");

        // expected output
        LevelUpViewModel expectedOutput = new LevelUpViewModel();
        expectedOutput.setLevelUpId(5);
        expectedOutput.setCustomer(customer);
        expectedOutput.setMemberDate(LocalDate.of(2005,5,5));
        expectedOutput.setPoints(1000);

        // list
        List<LevelUpViewModel> expectedList = new ArrayList<>();
        expectedList.add(expectedOutput);

        // Test 1: saveLevelUp()
        // --------------------- //
        LevelUpViewModel fromService = serviceLayer.saveLevelUp(input);
        assertEquals(expectedOutput, fromService);

        // Test 2: saveLevelUp()
        // --------------------- //
        fromService = serviceLayer.findLevelUp(5);
        assertEquals(expectedOutput, fromService);

        // Test 3: findLevelUpByCustomerId()
        // --------------------------------- //
        fromService = serviceLayer.findLevelUpByCustomerId(1);
        assertEquals(expectedOutput, fromService);

        // Test 4: findAllLevelUps()
        // ------------------------- //
        List<LevelUpViewModel> fromServiceList = serviceLayer.findAllLevelUps();
        assertEquals(expectedList, fromServiceList);

        // Test 5: updateLevelUp()
        // ------------------------- //
        expectedOutput.setPoints(1050);
        input.setPoints(1050);
        input.setLevelUpId(5);
        fromService = serviceLayer.updateLevelUp(input);
        assertEquals(expectedOutput, fromService);

        // Test 6: updateLevelUp()
        // ------------------------- //
        assertEquals("LevelUp [5] deleted successfully!",serviceLayer.removeLevelUp(5));
    }

    @Test
    public void testInventory() {
        // Test Setup
        // ---------- //
        // input
        InventoryInputModel input = new InventoryInputModel();
        input.setProductId(11);
        input.setQuantity(2);

        // product
        ProductViewModel product = new ProductViewModel();
        product.setProductId(11);
        product.setProductName("XBOX");
        product.setProductDescription("Console");
        product.setListPrice(new BigDecimal(250.00));
        product.setUnitCost(new BigDecimal(200.00));

        // expected output
        InventoryViewModel expectedOutput = new InventoryViewModel();
        expectedOutput.setInventoryId(21);
        expectedOutput.setQuantity(2);
        expectedOutput.setProduct(product);

        // list
        List<InventoryViewModel> expectedList = new ArrayList<>();
        expectedList.add(expectedOutput);

        // Test 1: saveInventory()
        // --------------------- //
        InventoryViewModel fromService = serviceLayer.saveInventory(input);
        assertEquals(expectedOutput, fromService);

        // Test 2: findInventory()
        // --------------------- //
        fromService = serviceLayer.findInventory(21);
        assertEquals(expectedOutput, fromService);

        // Test 3: findAllInventories()
        // ------------------------- //
        List<InventoryViewModel> fromServiceList = serviceLayer.findAllInventories();
        assertEquals(expectedList, fromServiceList);

        // Test 4: updateInventory()
        // ------------------------- //
        expectedOutput.setQuantity(5);
        input.setQuantity(5);
        input.setInventoryId(21);
        fromService = serviceLayer.updateInventory(input);
        assertEquals(expectedOutput, fromService);

        // Test 5: removeInventory()
        // ------------------------- //
        assertEquals("Inventory [21] deleted successfully!",serviceLayer.removeInventory(21));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvoice() {
        // SetUp
        // ----- //
        // Input
        //// invoice items
        InvoiceItem invoiceItemIn = new InvoiceItem();
        invoiceItemIn.setInventoryId(21);
        invoiceItemIn.setListPrice(new BigDecimal(250.00));
        invoiceItemIn.setQuantity(1);
        List<InvoiceItem> invoiceItemsIn = new ArrayList<>();
        invoiceItemsIn.add(invoiceItemIn);
        //// invoice
        InvoiceInputModel input = new InvoiceInputModel();
        input.setCustomerId(1);
        input.setPurchaseDate(LocalDate.of(2019,10,10));
        input.setInvoiceItems(invoiceItemsIn);

        // Expected Output
        // customer
        CustomerViewModel customer = new CustomerViewModel();
        customer.setCustomerId(1);
        customer.setFirstName("Ahmed");
        customer.setLastName("Elmallah");
        customer.setStreet("161 Newkirk");
        customer.setCity("Jersey City");
        customer.setZip("07100");
        customer.setPhone("201-100-2000");
        customer.setEmail("ahmed@elmalah.com");
        // product
        ProductViewModel product = new ProductViewModel();
        product.setProductId(11);
        product.setProductName("XBOX");
        product.setProductDescription("Console");
        product.setListPrice(new BigDecimal(250.00));
        product.setUnitCost(new BigDecimal(200.00));
        // inventory
        InventoryViewModel inventory = new InventoryViewModel();
        inventory.setInventoryId(21);
        inventory.setQuantity(2);
        inventory.setProduct(product);
        // invoiceItem
        InvoiceItemViewModel invoiceItem = new InvoiceItemViewModel();
        invoiceItem.setInvoiceId(31);
        invoiceItem.setInvoiceItemId(41);
        invoiceItem.setInventory(inventory);
        invoiceItem.setListPrice(new BigDecimal(250.00));
        invoiceItem.setQuantity(1);
        List<InvoiceItemViewModel> invoiceItems = new ArrayList<>();
        invoiceItems.add(invoiceItem);
        //invoice
        InvoiceViewModel expectedOutput = new InvoiceViewModel();
        expectedOutput.setInvoiceId(31);
        expectedOutput.setPurchaseDate(LocalDate.of(2019,10,10));
        expectedOutput.setMemberPoints(1050);
        expectedOutput.setCustomer(customer);
        expectedOutput.setInvoiceItems(invoiceItems);

        // Test 1 : saveInvoice()
        // ---------------------- //
        InvoiceViewModel fromSave = serviceLayer.saveInvoice(input);
        assertEquals(expectedOutput, fromSave);

        // Test 2 : findInvoice()
        // ---------------------- //
        expectedOutput.setMemberPoints(1000);
        InvoiceViewModel fromFind = serviceLayer.findInvoice(31);
        assertEquals(expectedOutput, fromFind);

        // Test 3 : findAllInvoices()
        // -------------------------- //
        List<InvoiceViewModel> allInvoices = serviceLayer.findAllInvoices();
        assertEquals(allInvoices.size(),1);
        assertEquals(allInvoices.get(0), expectedOutput);

        // Test 4 : updateInvoice()
        // ------------------------ //
        expectedOutput.setPurchaseDate(LocalDate.of(2005,5,5));
        input.setPurchaseDate(LocalDate.of(2005,5,5));
        input.setInvoiceId(31);
        invoiceItemIn.setInvoiceId(31);
        invoiceItemIn.setInvoiceItemId(41);

        InvoiceViewModel fromUpdate = serviceLayer.updateInvoice(input);
        assertEquals(expectedOutput, fromUpdate);

        // Test 5 : removeInvoice()
        // ------------------------ //
        assertEquals("Invoice [31] deleted successfully!",serviceLayer.removeInvoice(31));

        // Test 6: Order Quantity larger than Inventory
        // Should throw an illegalArgumentException
        invoiceItemIn.setQuantity(100);
        serviceLayer.saveInvoice(input);
    }

    private void setUpCustomerClientMock(){
        customerClient = mock(CustomerClient.class);

        // output
        CustomerViewModel output = new CustomerViewModel();
        output.setCustomerId(1);
        output.setFirstName("Ahmed");
        output.setLastName("Elmallah");
        output.setStreet("161 Newkirk");
        output.setCity("Jersey City");
        output.setZip("07100");
        output.setPhone("201-100-2000");
        output.setEmail("ahmed@elmalah.com");

        doReturn(output).when(customerClient).getCustomer(1);
    }

    private void setUpProductClientMock(){
        productClient = mock(ProductClient.class);

        // output
        ProductViewModel output = new ProductViewModel();
        output.setProductId(11);
        output.setProductName("XBOX");
        output.setProductDescription("Console");
        output.setListPrice(new BigDecimal(250.00));
        output.setUnitCost(new BigDecimal(200.00));

        doReturn(output).when(productClient).getProduct(11);
    }

    private void setUpInventoryClientMock(){
        inventoryClient = mock(InventoryClient.class);

        // input
        InventoryInputModel input = new InventoryInputModel();
        input.setProductId(11);
        input.setQuantity(2);

        // output
        InventoryInputModel output = new InventoryInputModel();
        output.setInventoryId(21);
        output.setProductId(11);
        output.setQuantity(2);

        // updated
        InventoryInputModel updated = new InventoryInputModel();
        updated.setInventoryId(21);
        updated.setProductId(11);
        updated.setQuantity(5);

        //// List
        List<InventoryInputModel> inventoryList = new ArrayList<>();
        inventoryList.add(output);

        doReturn(output).when(inventoryClient).createInventory(input);
        doReturn(output).when(inventoryClient).getInventory(21);
        doReturn(inventoryList).when(inventoryClient).getAllInventories();
        doReturn(updated).when(inventoryClient).updateInventory(updated,updated.getInventoryId());
        doReturn("Inventory [21] deleted successfully!").when(inventoryClient).deleteInventory(21);
    }

    private void setUpInvoiceClientMock(){
        invoiceClient = mock(InvoiceClient.class);

        // input
        //// invoice items
        InvoiceItem invoiceItemIn = new InvoiceItem();
        invoiceItemIn.setInventoryId(21);
        invoiceItemIn.setListPrice(new BigDecimal(250.00));
        invoiceItemIn.setQuantity(1);
        List<InvoiceItem> invoiceItemsIn = new ArrayList<>();
        invoiceItemsIn.add(invoiceItemIn);
        //// invoice
        InvoiceInputModel input = new InvoiceInputModel();
        input.setCustomerId(1);
        input.setPurchaseDate(LocalDate.of(2019,10,10));
        input.setInvoiceItems(invoiceItemsIn);

        // output
        //// invoice items
        InvoiceItem invoiceItemOut = new InvoiceItem();
        invoiceItemOut.setInvoiceItemId(41);
        invoiceItemOut.setInvoiceId(31);
        invoiceItemOut.setInventoryId(21);
        invoiceItemOut.setListPrice(new BigDecimal(250.00));
        invoiceItemOut.setQuantity(1);
        List<InvoiceItem> invoiceItemsOut = new ArrayList<>();
        invoiceItemsOut.add(invoiceItemOut);
        //// invoice
        InvoiceInputModel output = new InvoiceInputModel();
        output.setInvoiceId(31);
        output.setCustomerId(1);
        output.setPurchaseDate(LocalDate.of(2019,10,10));
        output.setInvoiceItems(invoiceItemsOut);

        // List
        List<InvoiceInputModel> outputList = new ArrayList<>();
        outputList.add(output);

        // Updated
        InvoiceInputModel updated = new InvoiceInputModel();
        updated.setInvoiceId(31);
        updated.setCustomerId(1);
        updated.setPurchaseDate(LocalDate.of(2005,5,5));
        updated.setInvoiceItems(invoiceItemsOut);

        doReturn(output).when(invoiceClient).createInvoice(input);
        doReturn(output).when(invoiceClient).getInvoice(31);
        doReturn(outputList).when(invoiceClient).getInvoicesByCustomer(1);
        doReturn(outputList).when(invoiceClient).getAllInvoices();
        doReturn(updated).when(invoiceClient).updateInvoice(updated,updated.getInvoiceId());
        doReturn("Invoice [31] deleted successfully!").when(invoiceClient).deleteInvoice(31);
    }

    private void setUpLevelUpClientMock(){
        levelUpClient = mock(LevelUpClient.class);

        // input
        LevelUpInputModel input = new LevelUpInputModel();
        input.setCustomerId(1);
        input.setMemberDate(LocalDate.of(2005,5,5));
        input.setPoints(1000);

        // output
        LevelUpInputModel output = new LevelUpInputModel();
        output.setLevelUpId(5);
        output.setCustomerId(1);
        output.setMemberDate(LocalDate.of(2005,5,5));
        output.setPoints(1000);

        // all
        List<LevelUpInputModel> levelUps = new ArrayList<>();
        levelUps.add(output);

        // updated
        LevelUpInputModel updated = new LevelUpInputModel();
        updated.setLevelUpId(5);
        updated.setCustomerId(1);
        updated.setMemberDate(LocalDate.of(2005,5,5));
        updated.setPoints(1050);

        doReturn(output).when(levelUpClient).createLevelUp(input);
        doReturn(output).when(levelUpClient).getLevelUp(5);
        doReturn(output).when(levelUpClient).getLevelUpByCustomerId(1);
        doReturn(levelUps).when(levelUpClient).getAllLevelUps();
        doReturn(updated).when(levelUpClient).updateLevelUp(updated,updated.getLevelUpId());
        doReturn("LevelUp [5] deleted successfully!").when(levelUpClient).deleteLevelUp(5);
    }
}