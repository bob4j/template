package com.openu.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.openu.model.Customer;
import com.openu.model.Order;
import com.openu.model.OrderStatus;

public interface CustomerRepository extends PagingAndSortingRepository<Customer, Long> {

    Customer findByUsername(String username);

    @Query("select o from order_ o left join o.customer c where c = :customer and o.status = :status")
    Order findOrder(@Param("customer") Customer customer, @Param("status") OrderStatus status);

}
