package com.openu.controller.admin;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.openu.controller.AbstractCrudController;
import com.openu.model.Order;
import com.openu.model.OrderStatus;
import com.openu.repository.OrderRepository;
import com.openu.util.Utils;

@Component
@Scope("view")
public class AdminOrderController extends AbstractCrudController<Order> {

    @Resource
    private OrderRepository orderRepository;

    public Order getOrder() {
        String orderId = Utils.getRequest().getParameter("order_id");
        if (orderId != null) {
            return orderRepository.findOne(Long.valueOf(orderId));
        }
        return null;
    }

    @Transactional
    public void approve(long orderId) {
        Order order = orderRepository.findOne(orderId);
        if (order.getStatus() != OrderStatus.PLACED) {
            throw new IllegalArgumentException();
        }
        // TODO decrement stock inventory
        order.setStatus(OrderStatus.APPROVED);
        orderRepository.save(order);
    }

    @Transactional
    public void ship(long orderId) {
        Order order = orderRepository.findOne(orderId);
        if (order.getStatus() != OrderStatus.APPROVED) {
            throw new IllegalArgumentException();
        }
        order.setStatus(OrderStatus.SHIPPED);
        orderRepository.save(order);
    }

    public List<Order> getPlacedOrders() {
        return orderRepository.findAll((root, query, cb) -> cb.and(cb.notEqual(root.get("status"), OrderStatus.PLACED)));
    }

    @Override
    protected PagingAndSortingRepository<Order, Long> getRepository() {
        return orderRepository;
    }

    @Override
    protected Order createEntity() throws Exception {
        throw new UnsupportedOperationException();
    }
    
    public void statusUp() {
	setSortBy("status");
	setDirection(Direction.ASC);
    }

    public void statusDown() {
	setSortBy("status");
	setDirection(Direction.DESC);
    }
    
    public void modifiedUp() {
	setSortBy("modified");
	setDirection(Direction.ASC);
    }

    public void modifiedDown() {
	setSortBy("modified");
	setDirection(Direction.DESC);
    }
    
    public void customerUp() {
	setSortBy("customer");
	setDirection(Direction.ASC);
    }

    public void customerDown() {
	setSortBy("customer");
	setDirection(Direction.DESC);
    }

}
