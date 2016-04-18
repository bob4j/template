package com.openu.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.openu.model.Customer;

public interface CustomerRepository extends PagingAndSortingRepository<Customer, Long> {

    Customer findByUsername(String username);

}
