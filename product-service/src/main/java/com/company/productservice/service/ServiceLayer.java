package com.company.productservice.service;

import com.company.productservice.dao.ProductDao;
import com.company.productservice.dto.Product;
import com.company.productservice.exception.NotFoundException;
import com.company.productservice.views.ProductViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceLayer {

    private ProductDao dao;

    @Autowired
    public ServiceLayer(ProductDao dao) {
        this.dao = dao;
    }

    //
    // Service Layer Methods
    // --------------------- //
    public ProductViewModel saveProduct(ProductViewModel productViewModel){
        Product product = viewModelToModel(productViewModel);

        product = dao.addProduct(product);

        productViewModel.setProductId(product.getProductId());

        return productViewModel;
    }

    public ProductViewModel findProduct(int productId){
        Product product = dao.getProduct(productId);
        if(product==null){
            throw new NotFoundException("Product ID cannot be found in DB!");
        } else {
            return buildProductViewModel(product);
        }
    }

    public List<ProductViewModel> findAllProducts(){
        List<Product> products = dao.getAllProducts();
        List<ProductViewModel> productViewModels = new ArrayList<>();

        products.forEach(product -> productViewModels.add(buildProductViewModel(product)));

        return productViewModels;
    }

    @Transactional
    public ProductViewModel updateProduct(ProductViewModel productViewModel){
        //Checking if product exists
        findProduct(productViewModel.getProductId());

        //Updating
        dao.updateProduct(viewModelToModel(productViewModel));

        //Retrieving
        return findProduct(productViewModel.getProductId());
    }

    @Transactional
    public String removeProduct(int productId){
        //Checking if product exists
        findProduct(productId);

        //Deleting
        dao.deleteProduct(productId);

        return "Product ["+productId+"] deleted successfully!";
    }

    //
    // Helper Methods
    // --------------------- //
    private Product viewModelToModel(ProductViewModel pvm){
        Product product = new Product();
        product.setProductId(pvm.getProductId());
        product.setProductName(pvm.getProductName());
        product.setProductDescription(pvm.getProductDescription());
        product.setListPrice(pvm.getListPrice());
        product.setUnitCost(pvm.getUnitCost());
        return product;
    }

    private ProductViewModel buildProductViewModel(Product product){
        ProductViewModel pvm = new ProductViewModel();
        pvm.setProductId(product.getProductId());
        pvm.setProductName(product.getProductName());
        pvm.setProductDescription(product.getProductDescription());
        pvm.setListPrice(product.getListPrice());
        pvm.setUnitCost(product.getUnitCost());
        return pvm;
    }
}
