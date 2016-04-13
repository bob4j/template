package com.openu.service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.openu.model.City;
import com.openu.repository.CityRepository;

@Service
public class CityService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private CityRepository repository;

    @PostConstruct
    public void init() {
        logger.info("CityService constructed");
    }

    public Iterable<City> findAllCities() {
        return repository.findAll();
    }

}