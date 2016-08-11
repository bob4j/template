package com.openu.model;

public enum ProductSize {

    _1("1"), _2("2"), _3("3"), _4("4"), _5("5"), _6("6"), _7("7"), _8("8"), _9("9"), _10("10"), _11("11"), _12("12");

    private String label;

    ProductSize(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
    
    public static ProductSize getSizeByLable(String label) {
   	for (ProductSize size : ProductSize.values()) {
   	    if (size.label.equalsIgnoreCase(label)) {
   		return size;
   	    }
   	}
   	return null;
       }

}
