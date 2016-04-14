package com.openu.service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.openu.model.Role;
import com.openu.model.User;
import com.openu.repository.UserRepository;

@Service
public class UserService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private UserRepository userRepository;

    private static final String ADMIN = "admin";

    @PostConstruct
    public void init() {
        initDefaultUser();
    }

    private void initDefaultUser() {
        if (userRepository.findByUsername(ADMIN) != null) {
            return;
        }
        logger.info("going to create default user '{}'", ADMIN);
        User user = new User();
        user.setUsername(ADMIN);
        user.setPassword(ADMIN);
        user.setRoles(Lists.newArrayList(Role.ADMIN, Role.CUSTOMER));
        userRepository.save(user);
    }
}
