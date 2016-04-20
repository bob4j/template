package com.openu.controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.openu.model.Category;
import com.openu.repository.CategoryRepository;

@ManagedBean
@RequestScoped
@Component
public class CategoryHandler {

    // private Category category;

    @Autowired
    private CategoryRepository categoryRepository;

    public Category getCategory() {
        // if (category != null) {
        // return category;
        // }
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        String categoryId = ((ServletRequest) context.getRequest()).getParameter("category_id");
        return categoryRepository.findOne(Long.valueOf(categoryId));
        // return category;
    }

    // public void setCategory(Category category) {
    // this.category = category;
    // }

}
