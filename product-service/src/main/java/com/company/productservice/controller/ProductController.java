package com.company.productservice.controller;

import com.company.productservice.service.ServiceLayer;
import com.company.productservice.views.ProductViewModel;
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
@CacheConfig(cacheNames = {"products"})
@RequestMapping(value = "/products")
public class ProductController {

    @Autowired
    ServiceLayer serviceLayer;

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<ProductViewModel> getAllProducts(){
        return serviceLayer.findAllProducts();
    }

    @CachePut(key = "#result.getProductId()")
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public ProductViewModel createProduct(@RequestBody @Valid ProductViewModel productViewModel){
        return serviceLayer.saveProduct(productViewModel);
    }

    @Cacheable
    @GetMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public ProductViewModel getProduct(@PathVariable int id) {
        System.out.println("fetching from DB...");
        return serviceLayer.findProduct(id);
    }

    @CacheEvict(key = "#id")
    @PutMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public ProductViewModel updateProduct(@RequestBody @Valid ProductViewModel productViewModel, @PathVariable int id) {
        if(id!=productViewModel.getProductId()){
            throw new IllegalArgumentException("Product ID in path must match with request body!");
        }
        return serviceLayer.updateProduct(productViewModel);
    }

    @CacheEvict
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public String deleteProduct(@PathVariable int id) {
        return serviceLayer.removeProduct(id);
    }
}