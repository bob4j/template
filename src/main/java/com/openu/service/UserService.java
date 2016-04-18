package com.openu.service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.openu.model.Administrator;
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

    @PostConstruct
    public void init() {
        initDefaultAdmin();
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
