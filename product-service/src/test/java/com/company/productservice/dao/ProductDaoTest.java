package com.company.productservice.dao;

import com.company.productservice.dto.Product;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ProductDaoTest {

    @Autowired
    ProductDao dao;

    @Before
    public void setUp() throws Exception {
        List<Product> products = dao.getAllProducts();
        products.forEach(product -> dao.deleteProduct(product.getProductId()));
    }

    @Test
    public void addGetProduct() {

        //Arranging
        Product product = new Product();
        product.setProductName("XBOX ONE");
        product.setProductDescription("Console from Microsoft");
        product.setListPrice(new BigDecimal("299.99").setScale(2));
        product.setUnitCost(new BigDecimal("200.00").setScale(2));

        //Adding Product
        Product fromAdd = dao.addProduct(product);
        //Getting Product
        Product fromGet = dao.getProduct(fromAdd.getProductId());

        //Asserting
        assertEquals(fromAdd, fromGet);

    }

    @Test
    public void getAllProducts() {
        //Arranging
        Product product = new Product();
        product.setProductName("XBOX ONE");
        product.setProductDescription("Console from Microsoft");
        product.setListPrice(new BigDecimal("299.99").setScale(2));
        product.setUnitCost(new BigDecimal("200.00").setScale(2));
        product = dao.addProduct(product);

        //Asserting
        assertEquals(dao.getAllProducts().size(),1);
        assertEquals(dao.getAllProducts().get(0),product);
    }

    @Test
    public void updateProduct() {
        //Arranging
        Product product = new Product();
        product.setProductName("XBOX ONE");
        product.setProductDescription("Console from Microsoft");
        product.setListPrice(new BigDecimal("299.99").setScale(2));
        product.setUnitCost(new BigDecimal("200.00").setScale(2));
        product = dao.addProduct(product);

        //Updating Product
        product.setProductName("XBOX ONE PRO");
        dao.updateProduct(product);

        //Asserting
        assertEquals(dao.getProduct(product.getProductId()),product);
    }

    @Test
    public void deleteProduct() {
        //Arranging
        Product product = new Product();
        product.setProductName("XBOX ONE");
        product.setProductDescription("Console from Microsoft");
        product.setListPrice(new BigDecimal("299.99").setScale(2));
        product.setUnitCost(new BigDecimal("200.00").setScale(2));
        product = dao.addProduct(product);

        //Deleting
        dao.deleteProduct(product.getProductId());

        //Asserting
        assertNull(dao.getProduct(product.getProductId()));
    }
}