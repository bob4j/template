package com.openu.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

@Entity
public class ShoppingCart {

    @Id
    @SequenceGenerator(name = "shopping_cart_seq", sequenceName = "shopping_cart_seq", allocationSize = 1, initialValue = 100)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "shopping_cart_seq")
    private Long id;

    private Long created;

    private Long modified;

    @OneToMany
    private List<ShoppingCartItem> item;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public Long getModified() {
        return modified;
    }

    public void setModified(Long modified) {
        this.modified = modified;
    }

    public List<ShoppingCartItem> getItem() {
        return item;
    }

    public void setItem(List<ShoppingCartItem> item) {
        this.item = item;
    }

    // private Customer customer;

}
