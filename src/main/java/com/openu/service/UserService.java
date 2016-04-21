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

@Service
public class UserService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private CustomerRepository customerRepository;

    @Resource
    private AdministratorRepository administratorRepository;

    public static final String ADMIN = "admin";
    public static final String DEFAULT_CUSTOMER = "customer";

    @PostConstruct
    public void init() {
        initDefaultAdmin();
        initDefaultCustomer();
    }

    private void initDefaultCustomer() {
        if (customerRepository.findByUsername(DEFAULT_CUSTOMER) != null) {
            return;
        }
        logger.info("going to create default customer '{}'", DEFAULT_CUSTOMER);
        Customer c = new Customer();
        c.setUsername(DEFAULT_CUSTOMER);
        c.setPassword(DEFAULT_CUSTOMER);
        c.setFirstName("John");
        c.setLastName("Smith");
        c.setEmail("customer@company.com");
        c.setPhoneNumber("123456789");
        customerRepository.save(c);
    }

    private void initDefaultAdmin() {
        if (administratorRepository.findByUsername(ADMIN) != null) {
            return;
        }
        logger.info("going to create default administrator '{}'", ADMIN);
        Administrator admin = new Administrator();
        admin.setUsername(ADMIN);
        admin.setPassword(ADMIN);
        admin.setName(ADMIN);
        admin.setEmail("admin@company.com");
        administratorRepository.save(admin);
    }

}
