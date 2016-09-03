package com.openu.service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.openu.model.Administrator;
import com.openu.model.Customer;
import com.openu.repository.AdministratorRepository;
import com.openu.repository.CustomerRepository;
import com.openu.util.Constants;

@Service
public class UserService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private CustomerRepository customerRepository;
    @Resource
    private AdministratorRepository administratorRepository;

    @PostConstruct
    public void init() {
        initDefaultAdmin();
        initDefaultCustomer();
    }

    private void initDefaultCustomer() {
        if (customerRepository.findByUsername(Constants.DEFAULT_CUSTOMER) != null) {
            return;
        }
        logger.info("going to create default customer '{}'", Constants.DEFAULT_CUSTOMER);
        Customer c = new Customer();
        c.setUsername(Constants.DEFAULT_CUSTOMER);
        c.setPassword(Constants.DEFAULT_CUSTOMER);
        c.setFirstName("John");
        c.setLastName("Smith");
        c.setEmail("customer@company.com");
        c.setPhoneNumber("123456789");
        customerRepository.save(c);
    }

    private void initDefaultAdmin() {
        if (administratorRepository.findByUsername(Constants.ADMIN) != null) {
            return;
        }
        logger.info("going to create default administrator '{}'", Constants.ADMIN);
        Administrator admin = new Administrator();
        admin.setUsername(Constants.ADMIN);
        admin.setPassword(Constants.ADMIN);
        admin.setName(Constants.ADMIN);
        admin.setEmail("shoestoreopenu@gmail.com");
        administratorRepository.save(admin);
    }

}
