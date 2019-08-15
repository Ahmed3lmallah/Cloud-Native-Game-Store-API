package com.company.AdminAPI.util.feign;

import com.company.AdminAPI.views.input.InventoryInputModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@FeignClient(name = "inventory-service")
public interface InventoryClient {

    @RequestMapping(value = "/inventory", method = RequestMethod.GET)
    List<InventoryInputModel> getAllInventories();

    @RequestMapping(value = "/inventory", method = RequestMethod.POST)
    InventoryInputModel createInventory(@RequestBody @Valid InventoryInputModel inventoryInputModel);

    @RequestMapping(value = "/inventory/{id}", method = RequestMethod.GET)
    InventoryInputModel getInventory(@PathVariable int id);

    @RequestMapping(value = "/inventory/{id}", method = RequestMethod.PUT)
    InventoryInputModel updateInventory(@RequestBody @Valid InventoryInputModel inventoryInputModel, @PathVariable int id);

    @RequestMapping(value = "/inventory/{id}", method = RequestMethod.DELETE)
    String deleteInventory(@PathVariable int id);
}
