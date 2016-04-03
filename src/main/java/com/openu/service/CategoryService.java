package com.openu.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.openu.model.Category;

@Service
public class CategoryService {

    public List<Category> findAllCategories() {
        return Lists.newArrayList(new Category(1, "Milk"), new Category(2, "Water"), new Category(3, "Bread"));
    }
}
