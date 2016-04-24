package com.openu.controller;

import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;

import com.openu.model.Address;
import com.openu.model.CreditCardInfo;
import com.openu.model.Customer;
import com.openu.repository.CustomerRepository;

@ManagedBean
@RequestScoped
@Component
public class AdminCustomerController extends AbstractCrudController<Customer> {

    @Resource
    private CustomerRepository customerRepository;

    private Customer newCustomer;

    private Address newAddress;

    private CreditCardInfo newCcInfo;

    @Override
    protected PagingAndSortingRepository<Customer, Long> getRepository() {
        return customerRepository;
    }

    @Override
    protected Customer createEntity() throws Exception {
        newCustomer.setAddress(newAddress);
        newCustomer.setCreditCardInfo(newCcInfo);
        return newCustomer;
    }

}
