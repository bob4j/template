package com.openu.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.google.common.collect.Lists;
import com.openu.util.Constants;

@Entity
public class Customer extends User implements Serializable {

   

    private static final String CUSTOMER = Constants.CUSTOMER;

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String firstName;

    private String lastName;


    @Embedded
    private Address address;

    private String phoneNumber;

    @ManyToOne
    private CreditCardInfo creditCardInfo;

    @OneToMany(mappedBy = CUSTOMER,fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE })
    private List<Order> orders = new ArrayList<>();

    public Customer() {
        super();
        roles = Lists.newArrayList(Role.CUSTOMER);
    }

    /**
     * utility method to retrieve the open order (shopping cart)
     */
    public Order getShoppingCart() {
        return orders.stream().filter(o -> o.getStatus() == OrderStatus.OPEN).findAny().orElse(null);
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public CreditCardInfo getCreditCardInfo() {
        return creditCardInfo;
    }

    public void setCreditCardInfo(CreditCardInfo creditCardInfo) {
        this.creditCardInfo = creditCardInfo;
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

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
    
    @Override
    public String toString() {
	return "Customer [firstName=" + firstName + ", lastName=" + lastName+"] " ;
    }
}
