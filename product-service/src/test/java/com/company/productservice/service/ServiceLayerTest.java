package com.company.productservice.service;

import com.company.productservice.dao.ProductDao;
import com.company.productservice.dao.ProductDaoJdbcTemplateImpl;
import com.company.productservice.dto.Product;
import com.company.productservice.exception.NotFoundException;
import com.company.productservice.views.ProductViewModel;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ServiceLayerTest {

    private ServiceLayer serviceLayer;
    private ProductDao productDao;

    @Before
    public void setUp() throws Exception {
        setUpProductDaoMock();
        serviceLayer = new ServiceLayer(productDao);
    }

    @Test
    public void saveProduct() {
        //Input
        ProductViewModel input = new ProductViewModel();
        input.setProductName("XBOX ONE");
        input.setProductDescription("Console from Microsoft");
        input.setListPrice(new BigDecimal("299.99").setScale(2));
        input.setUnitCost(new BigDecimal("200.00").setScale(2));

        //From Service
        ProductViewModel fromService = serviceLayer.saveProduct(input);

        //expected
        input.setProductId(1);

        //Asserting
        assertEquals(input, fromService);
    }

    @Test
    public void findProduct() {
        //expected
        ProductViewModel expectedOutput = new ProductViewModel();
        expectedOutput.setProductId(1);
        expectedOutput.setProductName("XBOX ONE");
        expectedOutput.setProductDescription("Console from Microsoft");
        expectedOutput.setListPrice(new BigDecimal("299.99").setScale(2));
        expectedOutput.setUnitCost(new BigDecimal("200.00").setScale(2));

        //Asserting
        assertEquals(expectedOutput, serviceLayer.findProduct(1));
    }

    @Test
    public void findAllProducts() {
        //expected
        ProductViewModel expectedOutput = new ProductViewModel();
        expectedOutput.setProductId(1);
        expectedOutput.setProductName("XBOX ONE");
        expectedOutput.setProductDescription("Console from Microsoft");
        expectedOutput.setListPrice(new BigDecimal("299.99").setScale(2));
        expectedOutput.setUnitCost(new BigDecimal("200.00").setScale(2));

        //Asserting
        assertEquals(1,serviceLayer.findAllProducts().size());
        assertEquals(expectedOutput, serviceLayer.findAllProducts().get(0));

    }

    @Test(expected = NotFoundException.class)
    public void updateProduct() {
        //Updating Product
        //expected & Input
        ProductViewModel expectedOutput = new ProductViewModel();
        expectedOutput.setProductId(1);
        expectedOutput.setProductName("XBOX ONE");
        expectedOutput.setProductDescription("Console from Microsoft");
        expectedOutput.setListPrice(new BigDecimal("299.99").setScale(2));
        expectedOutput.setUnitCost(new BigDecimal("200.00").setScale(2));

        assertEquals(expectedOutput, serviceLayer.updateProduct(expectedOutput));

        //A Product that doesn't exist in DB
        ProductViewModel fakeProduct = new ProductViewModel();
        fakeProduct.setProductId(2);
        fakeProduct.setProductName("XBOX ONE");
        fakeProduct.setProductDescription("Console from Microsoft");
        fakeProduct.setListPrice(new BigDecimal("299.99").setScale(2));
        fakeProduct.setUnitCost(new BigDecimal("200.00").setScale(2));

        serviceLayer.updateProduct(fakeProduct);
    }

    @Test(expected = NotFoundException.class)
    public void removeProduct() {
        assertEquals(serviceLayer.removeProduct(1), "Product [1] deleted successfully!");

        //A product that doesn't exist in DB
        serviceLayer.removeProduct(2);
    }

    private void setUpProductDaoMock() {

        productDao = mock(ProductDaoJdbcTemplateImpl.class);

        // Output
        Product output = new Product();
        output.setProductId(1);
        output.setProductName("XBOX ONE");
        output.setProductDescription("Console from Microsoft");
        output.setListPrice(new BigDecimal("299.99").setScale(2));
        output.setUnitCost(new BigDecimal("200.00").setScale(2));

        // Input
        Product input = new Product();
        input.setProductName("XBOX ONE");
        input.setProductDescription("Console from Microsoft");
        input.setListPrice(new BigDecimal("299.99").setScale(2));
        input.setUnitCost(new BigDecimal("200.00").setScale(2));

        // All Customers
        List<Product> products = new ArrayList<>();
        products.add(output);

        doReturn(output).when(productDao).addProduct(input);
        doReturn(output).when(productDao).getProduct(1);
        doReturn(products).when(productDao).getAllProducts();
        doNothing().when(productDao).updateProduct(input);
    }
}