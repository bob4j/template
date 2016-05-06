package com.openu.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.openu.model.Customer;
import com.openu.model.Order;
import com.openu.model.OrderStatus;
import com.openu.repository.CustomerRepository;
import com.openu.repository.OrderRepository;

@Component
@Scope("view")
public class OrderController implements Serializable {

    private static final long serialVersionUID = 1L;

    @Resource
    private CustomerRepository customerRepository;
    @Resource
    private OrderRepository orderRepository;
    @Resource
    private SessionBean sessionBean;

    public List<Order> getCustomerOrders() {
        Customer customer = sessionBean.loadCustomer();
        return orderRepository.findAll((root, query, cb) -> cb.and(cb.equal(root.get("customer"), customer),
                cb.notEqual(root.get("status"), OrderStatus.OPEN)));
    }

}