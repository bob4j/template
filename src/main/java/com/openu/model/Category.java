package com.openu.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.UniqueConstraint;

import com.openu.util.Constants;


@Entity
public class Category {

    private static final String CATEGORY_SEQUENCE_NAME = "category_seq";

    @Id
    @SequenceGenerator(name = CATEGORY_SEQUENCE_NAME, sequenceName = CATEGORY_SEQUENCE_NAME, allocationSize = 1, initialValue = 100)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = CATEGORY_SEQUENCE_NAME)
    private Long id;

    private String name;

    private String description;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Image image;

    @ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @JoinTable(name = Constants.CATEGORY_PRODUCT, inverseJoinColumns = { @JoinColumn(name = Constants.PRODUCT_ID, referencedColumnName = Constants.ID) }, joinColumns = { @JoinColumn(name = Constants.CATEGORY_ID, referencedColumnName = Constants.ID) }, uniqueConstraints = { @UniqueConstraint(columnNames = {
	    Constants.PRODUCT_ID, Constants.CATEGORY_ID }) })
    private List<Product> products;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
