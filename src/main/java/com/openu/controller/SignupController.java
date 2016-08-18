package com.openu.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIForm;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.openu.model.Address;
import com.openu.model.City;
import com.openu.model.Customer;
import com.openu.repository.CityRepository;
import com.openu.repository.CustomerRepository;
import com.openu.validators.EmailValidator;

@Component
@Scope("view")
public class SignupController implements Serializable {

   
    private static final long serialVersionUID = 5223230780931409024L;

    @Autowired
    private CustomerRepository repository;
    
    @Resource
    private CityRepository cityRepository;

    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String passwordAgain;
    private String email;
    private String phoneNumber;
    private String addressLine;
    private Long cityId;


    

    public void validate(ComponentSystemEvent e) {
        FacesContext fc = FacesContext.getCurrentInstance();
        if (!fc.getMessageList().isEmpty()) {
            return;
        }
        UIForm form = (UIForm) e.getComponent();
        if (!getField(e, "password").equals(getField(e, "passwordAgain"))) {
            fc.addMessage(form.getClientId(), new FacesMessage("Passwords do not match"));
        }
        if (repository.findByUsername(getField(e, "username")) != null) {
            fc.addMessage(form.getClientId(), new FacesMessage("Username is already taken"));
        }
        if (!EmailValidator.valid(getField(e, "email"))) {
            fc.addMessage(form.getClientId(), new FacesMessage("Invalid email"));
        }
        if (!fc.getMessageList().isEmpty()) {
            fc.renderResponse();
        }
    }

    private static <T> T getField(ComponentSystemEvent e, String fieldName) {
        UIForm form = (UIForm) e.getComponent();
        UIInput input = (UIInput) form.findComponent(fieldName);
        return (T) input.getValue();
    }

    public String signup() {
        if (repository.findByUsername(username) != null) {
            throw new RuntimeException("username is already taken");
        }
        Customer customer = new Customer();
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setUsername(username);
        customer.setPassword(password);
        customer.setEmail(email);
        customer.setPhoneNumber(phoneNumber);
        customer.setAddress(getCustomerAddress());
        repository.save(customer);
        return null;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public List<City> getCities() {
        return (List<City>) cityRepository.findAll();
    }

    public Long getCityId() {
	return cityId;
    }

    public void setCityId(Long cityId) {
	this.cityId = cityId;
    }
    
    private Address getCustomerAddress() {
	return new Address(cityRepository.findOne(cityId),getAddressLine());
    }

    public String getAddressLine() {
	return addressLine;
    }

    public void setAddressLine(String addressLine) {
	this.addressLine = addressLine;
    }

}
