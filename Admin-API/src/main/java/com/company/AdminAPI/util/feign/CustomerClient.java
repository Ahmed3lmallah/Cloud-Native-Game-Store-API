package com.company.AdminAPI.util.feign;

import com.company.AdminAPI.views.CustomerViewModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@FeignClient(name = "customer-service")
public interface CustomerClient {

    @RequestMapping(value = "/customers", method = RequestMethod.GET)
    public List<CustomerViewModel> getAllCustomers();

    @RequestMapping(value = "/customers", method = RequestMethod.POST)
    public CustomerViewModel createCustomer(@RequestBody @Valid CustomerViewModel customerViewModel);

    @RequestMapping(value = "/customers/{id}", method = RequestMethod.GET)
    public CustomerViewModel getCustomer(@PathVariable int id);

    @RequestMapping(value = "/customers/{id}", method = RequestMethod.PUT)
    public CustomerViewModel updateCustomer(@RequestBody @Valid CustomerViewModel customerViewModel, @PathVariable int id);

    @RequestMapping(value = "/customers/{id}", method = RequestMethod.DELETE)
    public String deleteCustomer(@PathVariable int id);
}
