package com.openu.controller;

import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.openu.model.Administrator;
import com.openu.model.Customer;
import com.openu.model.User;
import com.openu.repository.CustomerRepository;

@ManagedBean
@Component
@SessionScoped
public class SessionBean {

    @Resource
    private CustomerRepository customerRepository;

    public User getUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User) {
            return (User) principal;
        }
        return null;
    }

    public Customer loadCustomer() {
        Customer customer = getCustomer();
        if (customer == null) {
            return null;
        }
        return customerRepository.findOne(customer.getId());
    }

    public Customer getCustomer() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof Customer) {
            return (Customer) principal;
        }
        return null;
    }

    public Administrator getAdmin() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof Administrator) {
            return (Administrator) principal;
        }
        return null;
    }

}
