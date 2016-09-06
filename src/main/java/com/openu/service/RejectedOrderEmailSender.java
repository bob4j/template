package com.openu.service;

import org.springframework.stereotype.Service;

import com.openu.model.Order;

@Service
public class RejectedOrderEmailSender extends AbstractOrderEmailSender {

    private static final String ORDER_REJECT_HEADER = "<p>Dear %1$s %2$s</p>"
            + "<p>Your order has been Rejected and&nbsp; submitted back to the store warehouse ."
            + "<br />We are sorry for your choice, and hope you will continue to buy in our store</p>";

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
        return String.format(ORDER_REJECT_HEADER, order.getCustomer().getFirstName(), order.getCustomer().getLastName());
    }
}
