package com.openu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.openu.model.Product;

public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {

    @Query("from Product where name like %:term% or description like %:term%")
    List<Product> search(@Param("term") String term);

    // select * from product where id in (
    // select product_id from orderitem group by product_id
    // order by sum(quantity) desc
    // limit 10
    // );

    // @Query("select p1 from Product p1 where p1 in (select oi.product from OrderItem oi group by oi.product order by sum(oi.quantity) desc limit 5)")
    // List<Product> getMostOrdered();

}
