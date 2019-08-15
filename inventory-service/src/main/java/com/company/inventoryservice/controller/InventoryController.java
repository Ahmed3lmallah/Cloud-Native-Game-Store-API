package com.company.inventoryservice.controller;

import com.company.inventoryservice.service.ServiceLayer;
import com.company.inventoryservice.views.InventoryViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RefreshScope
@CacheConfig(cacheNames = {"inventory"})
@RequestMapping(value = "/inventory")
public class InventoryController {

    @Autowired
    ServiceLayer serviceLayer;

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<InventoryViewModel> getAllInventories(){
        return serviceLayer.findAllInventories();
    }

    @CachePut(key = "#result.getInventoryId()")
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public InventoryViewModel createInventory(@RequestBody @Valid InventoryViewModel inventoryViewModel){
        return serviceLayer.saveInventory(inventoryViewModel);
    }

    @Cacheable
    @GetMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public InventoryViewModel getInventory(@PathVariable int id) {
        System.out.println("fetching from DB...");
        return serviceLayer.findInventory(id);
    }

    @CacheEvict(key = "#id")
    @PutMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public InventoryViewModel updateInventory(@RequestBody @Valid InventoryViewModel inventoryViewModel, @PathVariable int id) {
        if(id!=inventoryViewModel.getInventoryId()){
            throw new IllegalArgumentException("Inventory ID in path must match with request body!");
        }
        return serviceLayer.updateInventory(inventoryViewModel);
    }

    @CacheEvict
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public String deleteInventory(@PathVariable int id) {
        return serviceLayer.removeInventory(id);
    }
}