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

@Entity
public class OrderItem {

    @Id
    @SequenceGenerator(name = "orderitem_seq", sequenceName = "orderitem_seq", allocationSize = 1, initialValue = 100)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orderitem_seq")
    private Long id;

    @NotNull
    @ManyToOne
    private Product product;

    @NotNull
    private Integer quantity = 1;

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
