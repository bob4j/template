package com.openu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.openu.model.Product;

public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {

    @Query("from Product where name like %:term% or description like %:term%")
    List<Product> search(@Param("term") String term);

}
