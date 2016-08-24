package com.openu.model;

public enum ProductColor {

    BLACK("black"), BROWN("brown"), RED("red"), BLUE("blue");
    private String colorName;

    private ProductColor(String colorName) {
        this.colorName = colorName;
    }

    public String getName() {
        return colorName;
    }

    public static ProductColor getColorByName(String colorName) {
        for (ProductColor color : ProductColor.values()) {
            if (color.colorName.equalsIgnoreCase(colorName)) {
                return color;
            }
        }
        return null;
    }
}
