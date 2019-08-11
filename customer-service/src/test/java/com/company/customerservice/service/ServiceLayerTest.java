package com.company.customerservice.service;

import com.company.customerservice.dao.CustomerDao;
import com.company.customerservice.dao.CustomerDaoJdbcTemplateImpl;
import com.company.customerservice.dto.Customer;
import com.company.customerservice.views.CustomerViewModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ServiceLayerTest {

    private ServiceLayer serviceLayer;
    private CustomerDao customerDao;

    @Before
    public void setUp() throws Exception {
        setUpCustomerDaoMock();
        serviceLayer = new ServiceLayer(customerDao);
    }

    @Test
    public void saveCustomer() {
        //Input
        CustomerViewModel input = new CustomerViewModel();
        input.setFirstName("Ahmed");
        input.setLastName("ElMallah");
        input.setStreet("161 Newkirk St.");
        input.setCity("Jersey City");
        input.setZip("07100");
        input.setEmail("ahmed@elmallah.com");
        input.setPhone("201-200-1000");

        //From Service
        CustomerViewModel fromService = serviceLayer.saveCustomer(input);

        //expected
        input.setCustomerId(1);

        //Asserting
        assertEquals(input, fromService);
    }

    @Test
    public void findCustomer() {
        //expected
        CustomerViewModel expectedOutput = new CustomerViewModel();
        expectedOutput.setCustomerId(1);
        expectedOutput.setFirstName("Ahmed");
        expectedOutput.setLastName("ElMallah");
        expectedOutput.setStreet("161 Newkirk St.");
        expectedOutput.setCity("Jersey City");
        expectedOutput.setZip("07100");
        expectedOutput.setEmail("ahmed@elmallah.com");
        expectedOutput.setPhone("201-200-1000");

        //Asserting
        assertEquals(expectedOutput, serviceLayer.findCustomer(1));
    }

    @Test
    public void findAllCustomers() {
        //expected
        CustomerViewModel expectedOutput = new CustomerViewModel();
        expectedOutput.setCustomerId(1);
        expectedOutput.setFirstName("Ahmed");
        expectedOutput.setLastName("ElMallah");
        expectedOutput.setStreet("161 Newkirk St.");
        expectedOutput.setCity("Jersey City");
        expectedOutput.setZip("07100");
        expectedOutput.setEmail("ahmed@elmallah.com");
        expectedOutput.setPhone("201-200-1000");

        //Asserting
        assertEquals(1,serviceLayer.findAllCustomers().size());
        assertEquals(expectedOutput, serviceLayer.findAllCustomers().get(0));

    }

    @Test(expected = com.trilogyed.post.exception.NotFoundException.class)
    public void updateCustomer() {
        //Updating Customer
        //expected & Input
        CustomerViewModel expectedOutput = new CustomerViewModel();
        expectedOutput.setCustomerId(1);
        expectedOutput.setFirstName("Ahmed");
        expectedOutput.setLastName("ElMallah");
        expectedOutput.setStreet("161 Newkirk St.");
        expectedOutput.setCity("Jersey City");
        expectedOutput.setZip("07100");
        expectedOutput.setEmail("ahmed@elmallah.com");
        expectedOutput.setPhone("201-200-1000");

        assertEquals(expectedOutput, serviceLayer.updateCustomer(expectedOutput));
        
        //A customer that doesn't exist in DB
        CustomerViewModel fakeCustomer = new CustomerViewModel();
        fakeCustomer.setCustomerId(2);
        fakeCustomer.setFirstName("Ahmed");
        fakeCustomer.setLastName("ElMallah");
        fakeCustomer.setStreet("161 Newkirk St.");
        fakeCustomer.setCity("Jersey City");
        fakeCustomer.setZip("07100");
        fakeCustomer.setEmail("ahmed@elmallah.com");
        fakeCustomer.setPhone("201-200-1000");

        serviceLayer.updateCustomer(fakeCustomer);
    }

    @Test(expected = com.trilogyed.post.exception.NotFoundException.class)
    public void removeCustomer() {
        assertEquals(serviceLayer.removeCustomer(1), "Customer [1] deleted successfully!");

        //A customer that doesn't exist in DB
        serviceLayer.removeCustomer(2);
    }

    private void setUpCustomerDaoMock() {

        customerDao = mock(CustomerDaoJdbcTemplateImpl.class);

        // Output
        Customer output = new Customer();
        output.setCustomerId(1);
        output.setFirstName("Ahmed");
        output.setLastName("ElMallah");
        output.setStreet("161 Newkirk St.");
        output.setCity("Jersey City");
        output.setZip("07100");
        output.setEmail("ahmed@elmallah.com");
        output.setPhone("201-200-1000");

        // Input
        Customer input = new Customer();
        input.setFirstName("Ahmed");
        input.setLastName("ElMallah");
        input.setStreet("161 Newkirk St.");
        input.setCity("Jersey City");
        input.setZip("07100");
        input.setEmail("ahmed@elmallah.com");
        input.setPhone("201-200-1000");

        // All Customers
        List<Customer> customers = new ArrayList<>();
        customers.add(output);

        doReturn(output).when(customerDao).addCustomer(input);
        doReturn(output).when(customerDao).getCustomer(1);
        doReturn(customers).when(customerDao).getAllCustomers();
        doNothing().when(customerDao).updateCustomer(input);
    }
}