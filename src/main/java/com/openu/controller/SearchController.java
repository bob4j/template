package com.openu.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.openu.model.Product;
import com.openu.repository.CategoryRepository;
import com.openu.repository.ProductRepository;
import com.openu.util.Utils;

@Component
@Scope("view")
public class SearchController {

    @Resource
    private ProductRepository productRepository;
    @Resource
    private CategoryRepository categoryRepository;

    /**
     * TODO - enrich the search
     */
    public List<Product> getResults() {
        String searchTerm = Utils.getRequest().getParameter("search");
        if (searchTerm == null) {
            return null;
        }
        return productRepository.search(searchTerm);
    }

}
