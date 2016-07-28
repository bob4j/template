package com.openu.model;

public enum OrderStatus {

    OPEN("Open"), 
    PLACED("Placed"), 
    CANCELLED("Canceled"), 
    REJECTED("Rejected"), 
    APPROVED("Approved"),
    SHIPPED("Shipped") , 
    NOT_SLECTED("All");
    
    private String nameForUI;
    
    private OrderStatus(String nameForUI ){
	this.nameForUI = nameForUI;
    }
    
    public String getNameForUI(){
	return nameForUI;
    }
    
    public static OrderStatus getValueByNameForUI(String nameForUI){
	for (OrderStatus orderStatus : values()) {
	    if (orderStatus.getNameForUI().equals(nameForUI)){
		return orderStatus;
	    }
	}
	return null;
    }

}
