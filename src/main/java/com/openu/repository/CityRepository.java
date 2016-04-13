package com.openu.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.openu.model.City;

public interface CityRepository extends PagingAndSortingRepository<City, Long> {

}
