package com.company.inventoryservice.service;

import com.company.inventoryservice.dao.InventoryDao;
import com.company.inventoryservice.dao.InventoryDaoJdbcTemplateImpl;
import com.company.inventoryservice.dto.Inventory;
import com.company.inventoryservice.exception.NotFoundException;
import com.company.inventoryservice.views.InventoryViewModel;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ServiceLayerTest {

    private ServiceLayer serviceLayer;
    private InventoryDao inventoryDao;

    @Before
    public void setUp() throws Exception {
        setUpInventoryDaoMock();
        serviceLayer = new ServiceLayer(inventoryDao);
    }

    @Test
    public void saveInventory() {
        //Input
        InventoryViewModel input = new InventoryViewModel();
        input.setProductId(10);
        input.setQuantity(100);

        //From Service
        InventoryViewModel fromService = serviceLayer.saveInventory(input);

        //expected
        input.setInventoryId(1);

        //Asserting
        assertEquals(input, fromService);
    }

    @Test
    public void findInventory() {
        //expected
        InventoryViewModel expectedOutput = new InventoryViewModel();
        expectedOutput.setInventoryId(1);
        expectedOutput.setProductId(10);
        expectedOutput.setQuantity(100);

        //Asserting
        assertEquals(expectedOutput, serviceLayer.findInventory(1));
    }

    @Test
    public void findAllInventorys() {
        //expected
        InventoryViewModel expectedOutput = new InventoryViewModel();
        expectedOutput.setInventoryId(1);
        expectedOutput.setProductId(10);
        expectedOutput.setQuantity(100);

        //Asserting
        assertEquals(1,serviceLayer.findAllInventories().size());
        assertEquals(expectedOutput, serviceLayer.findAllInventories().get(0));
    }

    @Test(expected = NotFoundException.class)
    public void updateInventory() {
        //Updating Invoice
        //expected & Input
        InventoryViewModel expectedOutput = new InventoryViewModel();
        expectedOutput.setInventoryId(1);
        expectedOutput.setProductId(10);
        expectedOutput.setQuantity(100);

        assertEquals(expectedOutput, serviceLayer.updateInventory(expectedOutput));

        //An Invoice that doesn't exist in DB
        InventoryViewModel fakeInventory = new InventoryViewModel();
        fakeInventory.setInventoryId(2);
        fakeInventory.setProductId(10);
        fakeInventory.setQuantity(100);

        serviceLayer.updateInventory(fakeInventory);
    }

    @Test(expected = NotFoundException.class)
    public void removeInventory() {
        assertEquals(serviceLayer.removeInventory(1), "Inventory [1] deleted successfully!");

        //A product that doesn't exist in DB
        serviceLayer.removeInventory(2);
    }

    private void setUpInventoryDaoMock() {

        inventoryDao = mock(InventoryDaoJdbcTemplateImpl.class);

        // Output
        Inventory output = new Inventory();
        output.setInventoryID(1);
        output.setProductID(10);
        output.setQuantity(100);

        // Input
        Inventory input = new Inventory();
        input.setProductID(10);
        input.setQuantity(100);

        // All Customers
        List<Inventory> inventories = new ArrayList<>();
        inventories.add(output);

        doReturn(output).when(inventoryDao).addInventory(input);
        doReturn(output).when(inventoryDao).getInventory(1);
        doReturn(inventories).when(inventoryDao).getAllInventory();
        doNothing().when(inventoryDao).updateInventory(input);
    }
}