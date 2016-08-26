package com.openu.service;

import org.springframework.stereotype.Service;

import com.openu.model.Order;

@Service
public class ApprovedOrderEmailSender extends AbstractOrderEmailSender {
    
    private static final String ORDER_APPROVED_HEADER = "<p>Dear %1$s %2$s</p>" + "<p>Your order has been placed</p>"
	    + "<p>your order should be&nbsp;dispatched soon , so please review the details below, and make sure your shipping address and and items are correct</p>"
	    + "<p>&nbsp;</p>";
    
    @Override
    protected String getOrderMessage(Order order) {
   	return   getOrderHeader(order)
   		+ getOrderInformation(order)
   		+ getItemsTable(order)
   		+ getBillingAndShipping(order)
   		;
       }
    
    @Override
    public void sendOrderEmail(Order order) {
   	send(order.getCustomer().getEmail(), YOUR_ORDER_STATUS_WAS_CHANGED + order.getStatus().getNameForUI(),
   		getOrderMessage(order));
       }
    @Override
    protected String getOrderHeader(Order order) {
	return String.format(ORDER_APPROVED_HEADER,order.getCustomer().getFirstName(), order.getCustomer().getLastName());
    }
}
