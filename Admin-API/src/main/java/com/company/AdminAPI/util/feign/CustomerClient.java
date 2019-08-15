package com.company.AdminAPI.util.feign;

import com.company.AdminAPI.views.CustomerViewModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@FeignClient(name = "customer-service")
public interface CustomerClient {

    @RequestMapping(value = "/customers", method = RequestMethod.GET)
    List<CustomerViewModel> getAllCustomers();

    @RequestMapping(value = "/customers", method = RequestMethod.POST)
    CustomerViewModel createCustomer(@RequestBody @Valid CustomerViewModel customerViewModel);

    @RequestMapping(value = "/customers/{id}", method = RequestMethod.GET)
    CustomerViewModel getCustomer(@PathVariable int id);

    @RequestMapping(value = "/customers/{id}", method = RequestMethod.PUT)
    CustomerViewModel updateCustomer(@RequestBody @Valid CustomerViewModel customerViewModel, @PathVariable int id);

    @RequestMapping(value = "/customers/{id}", method = RequestMethod.DELETE)
    String deleteCustomer(@PathVariable int id);
}
