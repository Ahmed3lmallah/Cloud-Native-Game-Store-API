package com.company.productservice.dao;

import com.company.productservice.dto.Product;

import java.util.List;

public interface ProductDao {

    Product addProduct(Product product);
    Product getProduct(int id);
    List<Product> getAllProducts();
    void updateProduct(Product product);
    void deleteProduct(int id);

}
