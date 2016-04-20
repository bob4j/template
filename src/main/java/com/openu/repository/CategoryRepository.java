package com.openu.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.openu.model.Category;

public interface CategoryRepository extends PagingAndSortingRepository<Category, Long> {

}
