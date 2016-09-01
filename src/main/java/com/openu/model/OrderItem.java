package com.openu.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;

import com.openu.util.Constants;


@Entity
public class OrderItem {

    private static final String ORDERITEM_SEQ = "orderitem_seq";

    @Id
    @SequenceGenerator(name = ORDERITEM_SEQ, sequenceName = ORDERITEM_SEQ, allocationSize = 1, initialValue = 100)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ORDERITEM_SEQ)
    private Long id;

    @NotNull
    @ManyToOne
    private Product product;

    @NotNull
    private Integer quantity = Constants.DEFAULT_ITEM_QUANTITY;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ProductColor color;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ProductSize size;

    private Double price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public ProductColor getColor() {
        return color;
    }

    public void setColor(ProductColor color) {
        this.color = color;
    }

    public ProductSize getSize() {
        return size;
    }

    public void setSize(ProductSize size) {
        this.size = size;
    }

    public Double getPrice() {
        return price;
    }

    public Double getTotalPrice() {
        return price * quantity;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

}
