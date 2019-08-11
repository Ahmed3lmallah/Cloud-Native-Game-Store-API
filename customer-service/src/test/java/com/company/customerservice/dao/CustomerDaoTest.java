package com.company.customerservice.dao;

import com.company.customerservice.dto.Customer;
import com.netflix.discovery.converters.Auto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class CustomerDaoTest {

    @Autowired
    private CustomerDao dao;

    @Before
    public void setUp() throws Exception {
        List<Customer> customerList = dao.getAllCustomers();
        customerList.forEach(customer -> dao.deleteCustomer(customer.getCustomerId()));
    }

    @Test
    public void addGetCustomer() {
        //Arranging
        Customer customer = new Customer();
        customer.setFirstName("Ahmed");
        customer.setLastName("ElMallah");
        customer.setStreet("161 Newkirk St.");
        customer.setCity("Jersey City");
        customer.setZip("07100");
        customer.setEmail("ahmed@elmallah.com");
        customer.setPhone("201-200-3000");

        //Adding Customer
        Customer fromAdd = dao.addCustomer(customer);
        //Getting Customer
        Customer fromGet = dao.getCustomer(fromAdd.getCustomerId());

        //Asserting
        assertEquals(fromAdd, fromGet);
    }

    @Test
    public void getAllCustomers() {
        //Arranging
        Customer customer = new Customer();
        customer.setFirstName("Ahmed");
        customer.setLastName("ElMallah");
        customer.setStreet("161 Newkirk St.");
        customer.setCity("Jersey City");
        customer.setZip("07100");
        customer.setEmail("ahmed@elmallah.com");
        customer.setPhone("201-200-3000");
        customer = dao.addCustomer(customer);

        //Asserting
        assertEquals(dao.getAllCustomers().size(),1);
        assertEquals(dao.getAllCustomers().get(0),customer);
    }

    @Test
    public void updateCustomer() {
        //Arranging
        Customer customer = new Customer();
        customer.setFirstName("Ahmed");
        customer.setLastName("ElMallah");
        customer.setStreet("161 Newkirk St.");
        customer.setCity("Jersey City");
        customer.setZip("07100");
        customer.setEmail("ahmed@el.com");
        customer.setPhone("201-200-3000");
        customer = dao.addCustomer(customer);

        //Updating Customer
        customer.setEmail("ahmed@elmallah.com");
        dao.updateCustomer(customer);

        //Asserting
        assertEquals(dao.getCustomer(customer.getCustomerId()),customer);


    }

    @Test
    public void deleteCustomer() {
        //Arranging
        Customer customer = new Customer();
        customer.setFirstName("Ahmed");
        customer.setLastName("ElMallah");
        customer.setStreet("161 Newkirk St.");
        customer.setCity("Jersey City");
        customer.setZip("07100");
        customer.setEmail("ahmed@el.com");
        customer.setPhone("201-200-3000");
        customer = dao.addCustomer(customer);

        //Deleting
        dao.deleteCustomer(customer.getCustomerId());

        //Asserting
        assertNull(dao.getCustomer(customer.getCustomerId()));

    }
}