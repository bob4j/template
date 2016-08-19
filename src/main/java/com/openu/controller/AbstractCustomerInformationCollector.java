package com.openu.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import com.openu.model.Address;
import com.openu.model.City;
import com.openu.model.Customer;
import com.openu.repository.CityRepository;
import com.openu.repository.CustomerRepository;

public abstract class AbstractCustomerInformationCollector extends AbstractInformationCollector<Customer> implements Serializable {
   
    private static final long serialVersionUID = -7205464267381850135L;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String addressLine;
    private Long cityId;
    
    @Resource
    private CustomerRepository repository;
    
    @Resource
    private CityRepository cityRepository;

    protected Customer customer = new Customer();

    public CustomerRepository getRepository() {
	return repository;
    }

    protected void addFieldsToUser() {
	customer.setFirstName(getFirstName());
        customer.setLastName(getLastName());
        customer.setUsername(getUsername());
        customer.setPassword(getPassword());
        customer.setEmail(getEmail());
        customer.setPhoneNumber(getPhoneNumber());
        customer.setAddress(getCustomerAddress());
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
         List<City> a = (List<City>) cityRepository.findAll();
         return a;
    }

    public Long getCityId() {
	return cityId;
    }

    public void setCityId(Long cityId) {
	this.cityId = cityId;
    }
    
    public Address getCustomerAddress() {
	return new Address(cityRepository.findOne(cityId),getAddressLine());
    }

    public String getAddressLine() {
	return addressLine;
    }

    public void setAddressLine(String addressLine) {
	this.addressLine = addressLine;
    }

    @Override
    Customer getUser() {
       return customer;
    }
    
}
