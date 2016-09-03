package com.openu.controller;

import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.openu.model.Administrator;
import com.openu.model.Customer;
import com.openu.model.User;
import com.openu.repository.AdministratorRepository;
import com.openu.repository.CustomerRepository;

/**
 * This class is for get and load the current logged in User.
 */
@ManagedBean
@Component
@SessionScoped
public class SessionBean {

    @Resource
    private CustomerRepository customerRepository;
    @Resource
    private AdministratorRepository adminRepository;

    /**
     * @return the {@link User} that logged in to the system. null if no logged user was found
     */
    public User getUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User) {
            return (User) principal;
        }
        return null;
    }

    /**
     * @return Reference to Customer in {@link CustomerRepository} that is logged in the system
     */
    public Customer loadCustomer() {
        Customer customer = getCustomer();
        if (customer == null) {
            return null;
        }
        return customerRepository.findOne(customer.getId());
    }

    /**
     * @return Reference to Admin in {@link AdministratorRepository} that is logged in the system
     */
    public Administrator loadAdmin() {
        Administrator admin = getAdmin();
        if (admin == null) {
            return null;
        }
        return adminRepository.findOne(admin.getId());
    }

    /**
     * @return the {@link Customer} that logged in to the system. null if no logged in Customer was found
     */
    public Customer getCustomer() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof Customer) {
            return (Customer) principal;
        }
        return null;
    }

    /**
     * @return the {@link Administrator} that logged in to the system. null if no logged in Administrator was found
     */
    public Administrator getAdmin() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof Administrator) {
            return (Administrator) principal;
        }
        return null;
    }

}
