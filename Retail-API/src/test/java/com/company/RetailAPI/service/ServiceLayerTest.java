package com.company.RetailAPI.service;

import com.company.RetailAPI.util.feign.*;
import com.company.RetailAPI.views.CustomerViewModel;
import com.company.RetailAPI.views.ProductViewModel;
import com.company.RetailAPI.views.input.InventoryInputModel;
import com.company.RetailAPI.views.input.InvoiceInputModel;
import com.company.RetailAPI.views.input.InvoiceItem;
import com.company.RetailAPI.views.input.LevelUpInputModel;
import com.company.RetailAPI.views.output.InvoiceViewModel;
import com.company.RetailAPI.views.output.LevelUpViewModel;
import com.company.RetailAPI.views.products.ProductFromInventory;
import com.company.RetailAPI.views.products.ProductFromInvoice;
import org.junit.Before;
import org.junit.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    private RabbitTemplate rabbitTemplate;

    @Before
    public void setUp() {
        setUpRabbitTemplateMock();
        setUpCustomerClientMock();
        setUpProductClientMock();
        setUpInventoryClientMock();
        setUpInvoiceClientMock();
        setUpLevelUpClientMock();

        serviceLayer = new ServiceLayer(rabbitTemplate, customerClient, productClient, inventoryClient, levelUpClient, invoiceClient);
    }

    @Test
    public void findLevelUpByCustomerId() {
        CustomerViewModel customer = new CustomerViewModel();
        customer.setCustomerId(1);
        customer.setFirstName("Ahmed");
        customer.setLastName("Elmallah");
        customer.setStreet("161 Newkirk");
        customer.setCity("Jersey City");
        customer.setZip("07100");
        customer.setPhone("201-100-2000");
        customer.setEmail("ahmed@elmalah.com");

        //Expected Output
        LevelUpViewModel expectedOutput = new LevelUpViewModel();
        expectedOutput.setLevelUpId(5);
        expectedOutput.setMemberDate(LocalDate.of(2005,5,5));
        expectedOutput.setPoints(1000);
        expectedOutput.setCustomer(customer);

        //From Service
        LevelUpViewModel fromService = serviceLayer.findLevelUpByCustomerId(1);

        //Assert
        assertEquals(expectedOutput, fromService);
    }

    @Test
    public void getProductFromInventoryAndAllInventory() {
        // Test 1 : getProductFromInventory
        // -------------------------------- //
        // Expected Output
        ProductFromInventory expectedOutput = new ProductFromInventory();
        expectedOutput.setInventoryId(21);
        expectedOutput.setProductName("XBOX");
        expectedOutput.setProductDescription("Console");
        expectedOutput.setListPrice(new BigDecimal(250.00));
        expectedOutput.setQuantity(2);

        //From Service
        ProductFromInventory fromService = serviceLayer.getProductFromInventory(21);

        //Asserting
        assertEquals(expectedOutput, fromService);

        // Test 2 : getAllProductsFromInventory
        // ------------------------------------ //
        //From Service
        List<ProductFromInventory> fromServiceList = serviceLayer.getAllProductsFromInventory();

        //Asserting
        assertEquals(fromServiceList.size(),1);
        assertEquals(fromServiceList.get(0),expectedOutput);
    }

    @Test
    public void findInvoice() {
        // Expected Output
        //// customer
        CustomerViewModel customer = new CustomerViewModel();
        customer.setCustomerId(1);
        customer.setFirstName("Ahmed");
        customer.setLastName("Elmallah");
        customer.setStreet("161 Newkirk");
        customer.setCity("Jersey City");
        customer.setZip("07100");
        customer.setPhone("201-100-2000");
        customer.setEmail("ahmed@elmalah.com");
        //// invoice items
        ProductFromInvoice product = new ProductFromInvoice();
        product.setInvoiceId(31);
        product.setInvoiceItemId(41);
        product.setInventoryId(21);
        product.setProductName("XBOX");
        product.setProductDescription("Console");
        product.setUnitPrice(new BigDecimal(250.00));
        product.setQuantity(1);
        List<ProductFromInvoice> products = new ArrayList<>();
        products.add(product);
        ////invoice
        InvoiceViewModel expectedOutput = new InvoiceViewModel();
        expectedOutput.setInvoiceId(31);
        expectedOutput.setPurchaseDate(LocalDate.of(2019,10,10));
        expectedOutput.setMemberPoints("1000");
        expectedOutput.setCustomer(customer);
        expectedOutput.setInvoiceItems(products);


        // Test 1 : findInvoice
        // ------------------------------------ //
        InvoiceViewModel fromFind = serviceLayer.findInvoice(31);
        assertEquals(expectedOutput, fromFind);

        // Test 2 : findAllInvoices
        // ------------------------------------ //
        List<InvoiceViewModel> allInvoices = serviceLayer.findAllInvoices();
        assertEquals(allInvoices.size(),1);
        assertEquals(allInvoices.get(0), expectedOutput);

        // Test 3 : findInvoicesByCustomer
        // ------------------------------------ //
        List<InvoiceViewModel> invoicesByCustomer = serviceLayer.findInvoicesByCustomer(1);
        assertEquals(invoicesByCustomer.size(),1);
        assertEquals(invoicesByCustomer.get(0), expectedOutput);
    }

    @Test
    public void saveInvoice() {
        // Test : saveInvoice
        // ------------------------------------ //
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
        //// customer
        CustomerViewModel customer = new CustomerViewModel();
        customer.setCustomerId(1);
        customer.setFirstName("Ahmed");
        customer.setLastName("Elmallah");
        customer.setStreet("161 Newkirk");
        customer.setCity("Jersey City");
        customer.setZip("07100");
        customer.setPhone("201-100-2000");
        customer.setEmail("ahmed@elmalah.com");
        //// invoice items
        ProductFromInvoice product = new ProductFromInvoice();
        product.setInvoiceId(31);
        product.setInvoiceItemId(41);
        product.setInventoryId(21);
        product.setProductName("XBOX");
        product.setProductDescription("Console");
        product.setUnitPrice(new BigDecimal(250.00));
        product.setQuantity(1);
        List<ProductFromInvoice> products = new ArrayList<>();
        products.add(product);
        ////invoice
        InvoiceViewModel expectedOutput = new InvoiceViewModel();
        expectedOutput.setInvoiceId(31);
        expectedOutput.setPurchaseDate(LocalDate.of(2019,10,10));
        expectedOutput.setMemberPoints("1050");
        expectedOutput.setCustomer(customer);
        expectedOutput.setInvoiceItems(products);

        // from Service
        InvoiceViewModel fromSave = serviceLayer.saveInvoice(input);

        // Asserting
        assertEquals(expectedOutput, fromSave);
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

        // updated
        InventoryInputModel updated = new InventoryInputModel();
        updated.setInventoryId(21);
        updated.setProductId(11);
        updated.setQuantity(1);

        // output
        InventoryInputModel output = new InventoryInputModel();
        output.setInventoryId(21);
        output.setProductId(11);
        output.setQuantity(2);

        //// List
        List<InventoryInputModel> inventoryList = new ArrayList<>();
        inventoryList.add(output);

        doReturn(output).when(inventoryClient).getInventory(21);
        doReturn(inventoryList).when(inventoryClient).getAllInventories();
        doReturn(updated).when(inventoryClient).updateInventory(updated, updated.getInventoryId());
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

        //// List
        List<InvoiceInputModel> outputList = new ArrayList<>();
        outputList.add(output);

        doReturn(output).when(invoiceClient).createInvoice(input);
        doReturn(output).when(invoiceClient).getInvoice(31);
        doReturn(outputList).when(invoiceClient).getInvoicesByCustomer(1);
        doReturn(outputList).when(invoiceClient).getAllInvoices();
    }

    private void setUpLevelUpClientMock(){
        levelUpClient = mock(LevelUpClient.class);

        // output
        LevelUpInputModel output = new LevelUpInputModel();
        output.setLevelUpId(5);
        output.setCustomerId(1);
        output.setMemberDate(LocalDate.of(2005,5,5));
        output.setPoints(1000);

        doReturn(output).when(levelUpClient).getLevelUpByCustomerId(1);
    }

    private void setUpRabbitTemplateMock(){
        rabbitTemplate = mock(RabbitTemplate.class);
    }
}