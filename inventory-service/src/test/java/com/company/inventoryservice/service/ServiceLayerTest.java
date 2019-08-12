package com.company.inventoryservice.service;

import com.company.inventoryservice.dao.InventoryDao;
import com.company.inventoryservice.dao.InventoryDaoJdbcTemplateImpl;
import com.company.inventoryservice.dto.Inventory;
import com.company.inventoryservice.exception.NotFoundException;
import com.company.inventoryservice.views.InventoryViewModel;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
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
        input.setProductID(10);
        input.setQuantity(100);

        //From Service
        InventoryViewModel fromService = serviceLayer.saveInventory(input);

        //expected
        input.setInventoryID(1);

        //Asserting
        assertEquals(input, fromService);
    }

    @Test
    public void findInventory() {
        //expected
        InventoryViewModel expectedOutput = new InventoryViewModel();
        expectedOutput.setInventoryID(1);
        expectedOutput.setProductID(10);
        expectedOutput.setQuantity(100);

        //Asserting
        assertEquals(expectedOutput, serviceLayer.findInventory(1));
    }

    @Test
    public void findAllInventorys() {
        //expected
        InventoryViewModel expectedOutput = new InventoryViewModel();
        expectedOutput.setInventoryID(1);
        expectedOutput.setProductID(10);
        expectedOutput.setQuantity(100);

        //Asserting
        assertEquals(1,serviceLayer.findAllInventorys().size());
        assertEquals(expectedOutput, serviceLayer.findAllInventorys().get(0));
    }

    @Test(expected = NotFoundException.class)
    public void updateInventory() {
        //Updating Inventory
        //expected & Input
        InventoryViewModel expectedOutput = new InventoryViewModel();
        expectedOutput.setInventoryID(1);
        expectedOutput.setProductID(10);
        expectedOutput.setQuantity(100);

        assertEquals(expectedOutput, serviceLayer.updateInventory(expectedOutput));

        //A Product that doesn't exist in DB
        InventoryViewModel fakeInventory = new InventoryViewModel();
        fakeInventory.setInventoryID(2);
        fakeInventory.setProductID(10);
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