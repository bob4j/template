package com.openu.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

@Entity
public class StockItem {

    @Id
    @SequenceGenerator(name = "stockitem_seq", sequenceName = "stockitem_seq", allocationSize = 1, initialValue = 100)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stockitem_seq")
    private Long id;

    @ManyToOne
    private Product product;

    @Enumerated(EnumType.STRING)
    private ProductSize size;

    @Enumerated(EnumType.STRING)
    private ProductColor color;

    private Integer quantity;

    public StockItem() {

    }

    public StockItem(Product product, ProductSize size, ProductColor color, Integer quantity) {
        this.product = product;
        this.size = size;
        this.color = color;
        this.quantity = quantity;
    }

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

    public ProductSize getSize() {
        return size;
    }

    public void setSize(ProductSize size) {
        this.size = size;
    }

    public ProductColor getColor() {
        return color;
    }

    public void setColor(ProductColor color) {
        this.color = color;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
	return "StockItem [id=" + id + ", product=" + product.getId() + ", size=" + size + ", color=" + color + ", quantity="
		+ quantity + "]";
    }

}
