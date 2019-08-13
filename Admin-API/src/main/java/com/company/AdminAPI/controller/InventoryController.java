package com.company.AdminAPI.controller;

import com.company.AdminAPI.service.ServiceLayer;
import com.company.AdminAPI.views.InventoryViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RefreshScope
@RequestMapping(value = "/inventory")
public class InventoryController {

    @Autowired
    ServiceLayer serviceLayer;

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<InventoryViewModel> getAllInventories(){
        return serviceLayer.findAllInventories();
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public InventoryViewModel createInventory(@RequestBody @Valid InventoryViewModel inventoryViewModel){
        return serviceLayer.saveInventory(inventoryViewModel);
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public InventoryViewModel getInventory(@PathVariable int id) {
        return serviceLayer.findInventory(id);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public InventoryViewModel updateInventory(@RequestBody @Valid InventoryViewModel inventoryViewModel, @PathVariable int id) {
        if(id!=inventoryViewModel.getInventoryID()){
            throw new IllegalArgumentException("Inventory ID in path must match with request body!");
        }
        return serviceLayer.updateInventory(inventoryViewModel);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public String deleteInventory(@PathVariable int id) {
        return serviceLayer.removeInventory(id);
    }
}
