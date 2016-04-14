package com.openu.controller;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.openu.model.User;
import com.openu.repository.UserRepository;

@ManagedBean
@RequestScoped
@Component
public class UserController implements Serializable {

    private static final long serialVersionUID = 1L;

    @Autowired
    private UserRepository repository;

    private User user;

    public Iterable<User> getUsers() {
        return repository.findAll();
    }

    public String load(User user) {
        this.user = user;
        return "admin/user";
    }

}