package com.openu.model;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

@Embeddable
public class Address {

    @ManyToOne
    private City city;

    private String address;

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
