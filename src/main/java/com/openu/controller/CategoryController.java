package com.openu.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.openu.model.Category;
import com.openu.service.CategoryService;

@ManagedBean
@RequestScoped
@Component
public class CategoryController implements Serializable {

    private static final long serialVersionUID = 1L;

    @Autowired
    private CategoryService categoryService;

    public List<Category> getCategories() {
        return categoryService.findAllCategories();
    }

    public Category getCategory() {
        FacesContext context = FacesContext.getCurrentInstance();
        String catId = context.getExternalContext().getRequestParameterMap().get("cat_id");
        return new Category(Integer.parseInt(catId), null);
    }

}