package com.company.RetailAPI.util.feign;

import com.company.RetailAPI.views.input.InventoryInputModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name = "inventory-service")
public interface InventoryClient {

    @RequestMapping(value = "/inventory", method = RequestMethod.GET)
    List<InventoryInputModel> getAllInventories();

    @RequestMapping(value = "/inventory/{id}", method = RequestMethod.GET)
    InventoryInputModel getInventory(@PathVariable int id);

}
