package com.openu.controller;

import java.util.Optional;

import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.openu.model.Customer;
import com.openu.model.Order;
import com.openu.model.OrderStatus;
import com.openu.repository.CustomerRepository;
import com.openu.repository.ProductRepository;

@ManagedBean
@RequestScoped
@Component
public class CustomerController {

    @Resource
    private SessionBean sessionBean;

    @Resource
    private CustomerRepository customerRepository;

    @Resource
    private ProductRepository productRepository;

    @Transactional
    public int getNumberOfShoppingItems() {
        Customer customer = sessionBean.getCustomer();
        if (customer == null) {
            return 0;
        }
        return Optional.ofNullable(customerRepository.findOrder(customer, OrderStatus.OPEN)).map(o -> o.getItems().size()).orElse(0);
    }

    @Transactional
    public Order getShoppingCart() {
        Customer customer = sessionBean.getCustomer();
        if (customer == null) {
            return null;
        }
        Order order = Optional.ofNullable(customerRepository.findOrder(customer, OrderStatus.OPEN)).orElse(new Order());
        order.getItems().size();
        return order;
    }
}