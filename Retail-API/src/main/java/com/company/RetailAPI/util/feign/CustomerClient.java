package com.company.RetailAPI.util.feign;

import com.company.RetailAPI.views.CustomerViewModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "customer-service")
public interface CustomerClient {

    @RequestMapping(value = "/customers/{id}", method = RequestMethod.GET)
    CustomerViewModel getCustomer(@PathVariable int id);

}
