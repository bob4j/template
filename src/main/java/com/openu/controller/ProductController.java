package com.openu.controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

import com.openu.model.Product;
import com.openu.repository.ProductRepository;
import com.openu.util.Utils;

@ManagedBean
@RequestScoped
@Component
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    public Iterable<Product> getNewProducts() {
        return productRepository.findAll(new PageRequest(0, 6, new Sort(new Sort.Order(Direction.DESC, "id")))).getContent();
    }

    public Product getProduct() {
        String productId = Utils.getRequest().getParameter("product_id");
        return productRepository.findOne(Long.valueOf(productId));
    }
}
