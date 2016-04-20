package com.openu.controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.openu.model.Customer;
import com.openu.repository.CustomerRepository;

@ManagedBean
@Component
@SessionScoped
public class SignupController {

    @Autowired
    private CustomerRepository repository;

    private String name;
    private String username;
    private String password;
    private String passwordAgain;
    private String email;
    private String phoneNumber;

    public String signup() {
        if (!password.equals(passwordAgain)) {
            throw new RuntimeException("Passwords do not match...");
        }
        Customer customer = new Customer();
        customer.setName(name);
        customer.setUsername(username);
        customer.setPassword(password);
        customer.setEmail(email);
        customer.setPhoneNumber(phoneNumber);
        repository.save(customer);
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordAgain() {
        return passwordAgain;
    }

    public void setPasswordAgain(String passwordAgain) {
        this.passwordAgain = passwordAgain;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}