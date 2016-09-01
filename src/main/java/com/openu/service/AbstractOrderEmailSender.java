package com.openu.service;

import java.util.List;

import com.openu.model.Order;
import com.openu.model.OrderItem;
import com.openu.util.Constants;

public abstract class AbstractOrderEmailSender extends EmailSender {
    private static final String ITEM_TABLE_BODY = "<tr>" + "<td>%1$s</td>" + "<td>%2$s</td>"+ "<td>%3$s</td>" + "<td>%4$.2f</td>" + "<td>%5$d</td>" + "</tr>";

    protected static final String YOUR_ORDER_STATUS_WAS_CHANGED = "Your Order Status was changed to ";

    private static final String ITEM_TABLE = 
	    "<table style=\"height: 40px;\" width=\"424\">" + 
		    "<tbody>" +
		    	"<tr>"
		    	    + "<td><strong>Product Name&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </strong></td>"
                	    + "<td><strong>Color&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </strong></td>"
                	    + "<td><strong>Size&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </strong></td>"
                	    + "<td><strong>Price&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </strong></td>"
                	    + "<td><strong>Product ID</strong></td>" + 
                	 "</tr>"
                	    + "%1$s  </tbody>" + "</table>" + "<p>&nbsp;</p>"
	    + "<p>&nbsp;</p>" + "<p>&nbsp;</p>" + "<p>&nbsp;</p>";

    private static final String BILLING_AND_SHIPPING = "<p><strong style=\"border-bottom: 2px solid rgba(69,159,237, .3); color: rgba(69,159,237, 1);\">Billing and Shipping</strong></p>"
	    + "<p>&nbsp;%1$s %2$s</p>" + "<p>%3$s</p>" + "<p>%4$s</p>";

    private static final String ORDER_INFORMATION = "<p><strong style=\"border-bottom: 2px solid rgba(69,159,237, .3); color: rgba(69,159,237, 1);\">Order information</strong></p>"
	    + "<p>Order id: %1$d</p>" + "<p>Total price:%2$.2f</p>" + "<p>&nbsp;</p>";

   
    
    abstract  String getOrderHeader(Order order) ;
    abstract void sendOrderEmail(Order order);
    abstract String getOrderMessage(Order order);
    
    protected String getOrderInformation(Order order) {
	return String.format(ORDER_INFORMATION, order.getId(), order.getTotalPrice());
    }
    

    protected String getBillingAndShipping(Order order) {
	if (order.getShippingAddress() != null ){
	    return String.format(BILLING_AND_SHIPPING, order.getCustomer().getFirstName(),
		    order.getCustomer().getLastName(), order.getShippingAddress().getAddress(),
		    order.getShippingAddress().getCity().getName());
	}
	return String.format(BILLING_AND_SHIPPING, order.getCustomer().getFirstName(),
		    order.getCustomer().getLastName(), Constants.NOT_FOUND,Constants.NOT_FOUND);
    }

    private String getOrderItemsBodyTable(Order order){
	List<OrderItem> items = order.getItems();
	StringBuilder orderItemsBodyTable = new StringBuilder();
	if (items != null){
	    for (OrderItem orderItem : items) {
		String OrderItemRow = String.format(ITEM_TABLE_BODY,orderItem.getProduct().getName(), orderItem.getColor().name(), orderItem.getSize().getLabel() ,orderItem.getPrice(), orderItem.getId());
		orderItemsBodyTable.append(OrderItemRow);
	    }
	}
	return orderItemsBodyTable.toString();
    }
    
    protected String getItemsTable(Order order){
	return String.format(ITEM_TABLE, getOrderItemsBodyTable( order));
    }

    

}
