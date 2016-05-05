package com.openu.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.openu.model.Customer;
import com.openu.model.Order;

public interface OrderRepository extends PagingAndSortingRepository<Order, Long> {

    List<Order> findByCustomer(Customer customer);

}
