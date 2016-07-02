package com.openu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.openu.model.Category;
import com.openu.repository.CategoryRepository;
import com.openu.util.Utils;

@Component
@Scope("view")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    public Category getCategory() {
        String categoryId = Utils.getRequest().getParameter("category_id");
        if (categoryId != null) {
            return categoryRepository.findOne(Long.valueOf(categoryId));
        }
        return null;
    }

    public Iterable<Category> getAll() {
        return categoryRepository.findAll();
    }

}
