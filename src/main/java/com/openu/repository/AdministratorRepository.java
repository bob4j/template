package com.openu.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.openu.model.Administrator;

public interface AdministratorRepository extends PagingAndSortingRepository<Administrator, Long> {

    Administrator findByUsername(String username);

}
