package com.openu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.openu.model.Customer;
import com.openu.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    // @Query("from order_ where customer = :customer and state != 'OPEN'")
    List<Order> findByCustomer(Customer customer);

}
