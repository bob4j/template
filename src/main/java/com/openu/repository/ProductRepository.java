package com.openu.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.openu.model.Product;

public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {

}
