package com.openu.controller;

import java.io.Serializable;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

import com.openu.model.Administrator;
import com.openu.model.Customer;
import com.openu.repository.CustomerRepository;

public abstract class AbstractCustomerInformationCollector extends AbstractInformationCollector implements Serializable {
   
    private static final long serialVersionUID = -7205464267381850135L;

    @Resource
    private CustomerRepository repository;

    protected Customer customer = new Customer();

    public CustomerRepository getRepository() {
	return repository;
    }

    public String apply() {
        addFieldsToCustomer();
        getRepository().save(customer);
        return null;
    }

    protected void addFieldsToCustomer() {
	customer.setFirstName(getFirstName());
        customer.setLastName(getLastName());
        customer.setUsername(getUsername());
        customer.setPassword(getPassword());
        customer.setEmail(getEmail());
        customer.setPhoneNumber(getPhoneNumber());
        customer.setAddress(getCustomerAddress());
    }
    
    @Override
    protected Customer createEntity() throws Exception {
	return customer;
	
    }
    
}
