package com.company.RetailAPI.util.feign;

import com.company.RetailAPI.views.input.InventoryInputModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.List;

@FeignClient(name = "inventory-service")
public interface InventoryClient {

    @RequestMapping(value = "/inventory", method = RequestMethod.GET)
    public List<InventoryInputModel> getAllInventories();

    @RequestMapping(value = "/inventory", method = RequestMethod.POST)
    public InventoryInputModel createInventory(@RequestBody @Valid InventoryInputModel inventoryInputModel);

    @RequestMapping(value = "/inventory/{id}", method = RequestMethod.GET)
    public InventoryInputModel getInventory(@PathVariable int id);

    @RequestMapping(value = "/inventory/{id}", method = RequestMethod.PUT)
    public InventoryInputModel updateInventory(@RequestBody @Valid InventoryInputModel inventoryInputModel, @PathVariable int id);

    @RequestMapping(value = "/inventory/{id}", method = RequestMethod.DELETE)
    public String deleteInventory(@PathVariable int id);
}
