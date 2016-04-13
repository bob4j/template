package com.openu.controller;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.openu.model.City;
import com.openu.repository.CityRepository;
import com.openu.service.CityService;

@ManagedBean
@RequestScoped
@Component
public class CityController implements Serializable {

    private static final long serialVersionUID = 1L;

    @Autowired
    private CityService cityService;

    @Autowired
    private CityRepository repository;

    private City city;

    private String cityName;

    /**
     * Get all cities
     */
    public Iterable<City> getCities() {
        return repository.findAll();
    }

    public void addCity() {
        if (cityName == null) {
            return;
        }
        City city = new City();
        city.setName(cityName);
        repository.save(city);
    }

    public void load(City city) {
        this.city = city;
    }

    /*-- getters & setters --*/

    public City getCity() {
        return city;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

}