package com.openu.controller;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;

import com.openu.model.City;
import com.openu.repository.CityRepository;

@ManagedBean
@RequestScoped
@Component
public class CityController extends AbstractCrudController<City> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Autowired
    private CityRepository repository;

    private String cityName;

    @Override
    protected PagingAndSortingRepository<City, Long> getRepository() {
        return repository;
    }

    @Override
    protected City createEntity() throws Exception {
        City city = new City();
        city.setName(cityName);
        return city;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

}