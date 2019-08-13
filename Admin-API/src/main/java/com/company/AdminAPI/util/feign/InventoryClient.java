package com.company.AdminAPI.util.feign;

import com.company.AdminAPI.util.messages.Inventory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@FeignClient(name = "inventory-service")
public interface InventoryClient {

    @RequestMapping(value = "/inventory", method = RequestMethod.GET)
    public List<Inventory> getAllInventories();

    @RequestMapping(value = "/inventory", method = RequestMethod.POST)
    public Inventory createInventory(@RequestBody @Valid Inventory inventory);

    @RequestMapping(value = "/inventory/{id}", method = RequestMethod.GET)
    public Inventory getInventory(@PathVariable int id);

    @RequestMapping(value = "/inventory/{id}", method = RequestMethod.PUT)
    public Inventory updateInventory(@RequestBody @Valid Inventory inventory, @PathVariable int id);

    @RequestMapping(value = "/inventory/{id}", method = RequestMethod.DELETE)
    public String deleteInventory(@PathVariable int id);
}
