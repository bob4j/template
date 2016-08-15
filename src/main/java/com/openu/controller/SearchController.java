package com.openu.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;

import com.openu.model.Product;

@Component
@Scope("view")
public class SearchController extends AbstractSearchController {

    @Override
    protected PagingAndSortingRepository<Product, Long> getRepository() {
        return productRepository;
    }

    @Override
    protected Product createEntity() throws Exception {
        return null;
    }

}
