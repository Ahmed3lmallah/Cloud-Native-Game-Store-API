package com.company.inventoryservice.dao;

import com.company.inventoryservice.dto.Inventory;

import java.util.List;

public interface InventoryDao {

    Inventory addInventory(Inventory inventory);
    Inventory getInventory(int id);
    List<Inventory> getAllInventory();
    void updateInventory(Inventory inventory);
    void deleteInventory(int id);
}
