package com.company.AdminAPI.util.feign;

import com.company.AdminAPI.views.ProductViewModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.List;

@FeignClient(name = "product-service")
public interface ProductClient {

    @RequestMapping(value = "/products", method = RequestMethod.GET)
    List<ProductViewModel> getAllProducts();

    @RequestMapping(value = "/products", method = RequestMethod.POST)
    ProductViewModel createProduct(@RequestBody @Valid ProductViewModel productViewModel);

    @RequestMapping(value = "/products/{id}", method = RequestMethod.GET)
    ProductViewModel getProduct(@PathVariable int id);

    @RequestMapping(value = "/products/{id}", method = RequestMethod.PUT)
    ProductViewModel updateProduct(@RequestBody @Valid ProductViewModel productViewModel, @PathVariable int id);

    @RequestMapping(value = "/products/{id}", method = RequestMethod.DELETE)
    String deleteProduct(@PathVariable int id);

}
