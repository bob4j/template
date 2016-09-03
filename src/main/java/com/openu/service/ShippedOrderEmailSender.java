package com.openu.service;

import org.springframework.stereotype.Service;

import com.openu.model.Order;

@Service
public class ShippedOrderEmailSender extends AbstractOrderEmailSender {

    private static final String ORDER_SHIPPED_HEADER = "<p>Dear %1$s %2$s</p><p>Your order has been shipped</p>";

    @Override
    protected String getOrderMessage(Order order) {
        return getOrderHeader(order) + getOrderInformation(order) + getItemsTable(order) + getBillingAndShipping(order);
    }

    @Override
    public void sendOrderEmail(Order order) {
        send(order.getCustomer().getEmail(), YOUR_ORDER_STATUS_WAS_CHANGED + order.getStatus().getNameForUI(), getOrderMessage(order));
    }

    @Override
    protected String getOrderHeader(Order order) {
        return String.format(ORDER_SHIPPED_HEADER, order.getCustomer().getFirstName(), order.getCustomer().getLastName());
    }
}
