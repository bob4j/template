package com.openu.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.openu.model.ProductCategory;

public interface ProductCategoryRepository extends PagingAndSortingRepository<ProductCategory, Long> {

}
