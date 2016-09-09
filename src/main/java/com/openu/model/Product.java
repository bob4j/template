package com.openu.model;

import java.util.ArrayList;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.openu.util.Constants;

@Entity
public class Product {

    private static final String CATEGORY_ID = Constants.CATEGORY_ID;

    private static final String ID = Constants.ID;

    private static final String PRODUCT_ID = Constants.PRODUCT_ID;

    private static final String PRODUCT_SEQ = "product_seq";

    @Id
    @SequenceGenerator(name = PRODUCT_SEQ, sequenceName = PRODUCT_SEQ, allocationSize = 1, initialValue = 100)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = PRODUCT_SEQ)
    private Long id;

    private String brand;

    private String name;

    private String description;

    @ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE }, fetch = FetchType.EAGER)
    private Image image;

    @ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE }, fetch = FetchType.EAGER)
    private Image imageSmall;

    @ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE }, fetch = FetchType.EAGER)
    private Image imageLarge;

    private Double price;

    @JoinTable(name = "category_product", joinColumns = { @JoinColumn(name = PRODUCT_ID, referencedColumnName = ID) }, inverseJoinColumns = { @JoinColumn(name = CATEGORY_ID, referencedColumnName = ID) })
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Category> categories;

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER, orphanRemoval = true, cascade = { CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REMOVE })
    @Fetch(FetchMode.SELECT)
    private List<StockItem> stockItems = new ArrayList<>();

    public Image getImageSmall() {
        return imageSmall;
    }

    public void setImageSmall(Image imageSmall) {
        this.imageSmall = imageSmall;
    }

    public Image getImageLarge() {
        return imageLarge;
    }

    public void setImageLarge(Image imageLarge) {
        this.imageLarge = imageLarge;
    }

    public List<StockItem> getStockItems() {
        return stockItems;
    }

    public void setStockItems(List<StockItem> stockItems) {
        this.stockItems = stockItems;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

}
