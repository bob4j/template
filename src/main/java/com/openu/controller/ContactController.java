package com.openu.controller;

import java.io.Serializable;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.openu.model.Administrator;
import com.openu.repository.AdministratorRepository;
import com.openu.service.ContactEmailSender;

@Component
@Scope("view")
public class ContactController  implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Resource
    AdministratorRepository adminRepository;
    
    private String email;
    private String phoneNumber;
    private String name;
    private String message;

    @Resource
    ContactEmailSender contactEmailSender;

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

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public void apply() {
	Iterable<Administrator> allAdnins = adminRepository.findAll();
	for (Administrator administrator : allAdnins) {
	    String adminEmail = administrator.getEmail();
	    if (adminEmail != null) {
		contactEmailSender.sendContactMessage(adminEmail, getName(), getEmail(), getPhoneNumber(),
			getMessage());
	    }
	}
    }

    public String getMessage() {
	return message;
    }

    public void setMessage(String message) {
	this.message = message;
    }
}
