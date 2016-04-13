package com.openu.controller;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.servlet.http.Part;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.openu.model.Image;
import com.openu.model.ProductCategory;
import com.openu.repository.ProductCategoryRepository;

@ManagedBean
@RequestScoped
@Component
public class ProductCategoryController implements Serializable {

    private static final long serialVersionUID = 1L;

    private String categoryName;

    private Part image;

    private ProductCategory category;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    public Iterable<ProductCategory> getCategories() {
        return productCategoryRepository.findAll();
    }

    public void load(ProductCategory category) {
        this.category = category;
    }

    public void save() throws Exception {
        ProductCategory cat = new ProductCategory();
        cat.setName(categoryName);
        cat.setImage(new Image(IOUtils.toByteArray(image.getInputStream())));
        productCategoryRepository.save(cat);
    }

    public void setImage(Part image) {
        this.image = image;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Part getImage() {
        return image;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

}
