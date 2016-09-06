package com.openu.service;

import org.springframework.stereotype.Service;

import com.openu.model.Order;

@Service
public class CancelOrderEmailSender extends AbstractOrderEmailSender {

    private static final String ORDER_CANCELLED_HEADER = "<p>Dear %1$s %2$s</p><p>Your order has been Cancelled</p>"
            + "<p>For more information please contact us via the contact details on the website<p>";

    @Override
    protected String getOrderMessage(Order order) {
        return getOrderHeader(order) + getOrderInformation(order) + getItemsTable(order);
    }

    @Override
    public void sendOrderEmail(Order order) {
        send(order.getCustomer().getEmail(), YOUR_ORDER_STATUS_WAS_CHANGED + order.getStatus().getNameForUI(), getOrderMessage(order));
    }

    @Override
    protected String getOrderHeader(Order order) {
        return String.format(ORDER_CANCELLED_HEADER, order.getCustomer().getFirstName(), order.getCustomer().getLastName());
    }

}
