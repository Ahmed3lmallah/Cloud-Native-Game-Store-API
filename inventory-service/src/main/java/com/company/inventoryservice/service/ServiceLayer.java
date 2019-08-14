package com.company.inventoryservice.service;

import com.company.inventoryservice.dao.InventoryDao;
import com.company.inventoryservice.dto.Inventory;
import com.company.inventoryservice.exception.NotFoundException;
import com.company.inventoryservice.views.InventoryViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceLayer {
    private InventoryDao dao;

    @Autowired
    public ServiceLayer(InventoryDao dao) {
        this.dao = dao;
    }

    //
    // Service Layer Methods
    // --------------------- //
    public InventoryViewModel saveInventory(InventoryViewModel inventoryViewModel){
        Inventory inventory = viewModelToModel(inventoryViewModel);

        inventory = dao.addInventory(inventory);

        inventoryViewModel.setInventoryId(inventory.getInventoryID());

        return inventoryViewModel;
    }

    public InventoryViewModel findInventory(int inventoryId){
        Inventory inventory = dao.getInventory(inventoryId);
        if(inventory==null){
            throw new NotFoundException("Inventory ID cannot be found in DB!");
        } else {
            return buildInventoryViewModel(inventory);
        }
    }

    public List<InventoryViewModel> findAllInventories(){
        List<Inventory> inventories = dao.getAllInventory();
        List<InventoryViewModel> inventoryViewModels = new ArrayList<>();

        inventories.forEach(inventory -> inventoryViewModels.add(buildInventoryViewModel(inventory)));

        return inventoryViewModels;
    }

    @Transactional
    public InventoryViewModel updateInventory(InventoryViewModel inventoryViewModel){
        //Checking if Inventory exists
        findInventory(inventoryViewModel.getInventoryId());

        //Updating
        dao.updateInventory(viewModelToModel(inventoryViewModel));

        //Retrieving
        return findInventory(inventoryViewModel.getInventoryId());
    }

    @Transactional
    public String removeInventory(int inventoryId){
        //Checking if inventory exists
        findInventory(inventoryId);

        //Deleting
        dao.deleteInventory(inventoryId);

        return "Inventory ["+inventoryId+"] deleted successfully!";
    }

    //
    // Helper Methods
    // --------------------- //
    private Inventory viewModelToModel(InventoryViewModel ivm){
        Inventory inventory = new Inventory();
        inventory.setInventoryID(ivm.getInventoryId());
        inventory.setProductID(ivm.getProductId());
        inventory.setQuantity(ivm.getQuantity());
        return inventory;
    }

    private InventoryViewModel buildInventoryViewModel(Inventory inventory){
        InventoryViewModel ivm = new InventoryViewModel();
        ivm.setInventoryId(inventory.getInventoryID());
        ivm.setProductId(inventory.getProductID());
        ivm.setQuantity(inventory.getQuantity());
        return ivm;
    }
}
