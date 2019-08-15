package com.company.RetailAPI.util.feign;

import com.company.RetailAPI.views.ProductViewModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "product-service")
public interface ProductClient {

    @RequestMapping(value = "/products/{id}", method = RequestMethod.GET)
    ProductViewModel getProduct(@PathVariable int id);


}
