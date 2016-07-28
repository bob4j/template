package com.openu.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Sort;
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
                cb.notEqual(root.get("status"), OrderStatus.OPEN)), new Sort("id"));
    }

    
    public void cancelOrder(Long id) {
        Customer customer = sessionBean.loadCustomer();
        Order order = orderRepository.findOne(id);
        if (order == null) {
            throw new IllegalArgumentException("order with id " + id + " has not been found");
        }
        if (!order.getCustomer().getId().equals(customer.getId())) {
            throw new IllegalArgumentException("the order is not assosiated with the current customer");
        }
        if (order.getStatus() != OrderStatus.OPEN && order.getStatus() != OrderStatus.PLACED) {
            throw new IllegalArgumentException("cannot cancel order with status " + order.getStatus());
        }
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }
}