package com.company.inventoryservice.dao;

import com.company.inventoryservice.dto.Inventory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class InventoryDaoTest {

    @Autowired
    private InventoryDao dao;

    @Before
    public void setUp() throws Exception {
        List<Inventory> inventories = dao.getAllInventory();
        inventories.forEach(inventory -> dao.deleteInventory(inventory.getInventoryID()));
    }

    @Test
    public void addGetInventory() {
        //Arranging
        Inventory inventory = new Inventory();
        inventory.setInventoryID(1);
        inventory.setProductID(1);
        inventory.setQuantity(100);

        //Adding Inventory
        Inventory fromAdd = dao.addInventory(inventory);
        //Getting Inventory
        Inventory fromGet = dao.getInventory(fromAdd.getInventoryID());

        //Asserting
        assertEquals(fromAdd, fromGet);
    }

    @Test
    public void getAllInventory() {
        //Arranging
        Inventory inventory = new Inventory();
        inventory.setInventoryID(1);
        inventory.setProductID(1);
        inventory.setQuantity(100);
        inventory = dao.addInventory(inventory);

        //Asserting
        assertEquals(dao.getAllInventory().size(),1);
        assertEquals(dao.getAllInventory().get(0),inventory);
    }

    @Test
    public void updateInventory() {
        //Arranging
        Inventory inventory = new Inventory();
        inventory.setInventoryID(1);
        inventory.setProductID(1);
        inventory.setQuantity(100);
        inventory = dao.addInventory(inventory);

        //Updating Inventory
        inventory.setQuantity(95);
        dao.updateInventory(inventory);

        //Asserting
        assertEquals(dao.getInventory(inventory.getInventoryID()),inventory);
    }

    @Test
    public void deleteInventory() {
        //Arranging
        Inventory inventory = new Inventory();
        inventory.setInventoryID(1);
        inventory.setProductID(1);
        inventory.setQuantity(100);
        inventory = dao.addInventory(inventory);

        //Deleting
        dao.deleteInventory(inventory.getInventoryID());

        //Asserting
        assertNull(dao.getInventory(inventory.getInventoryID()));
    }
}