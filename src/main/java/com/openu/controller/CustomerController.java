package com.openu.controller;

import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.springframework.stereotype.Component;

import com.openu.repository.CustomerRepository;
import com.openu.repository.ProductRepository;

@ManagedBean
@RequestScoped
@Component
public class CustomerController {

    @Resource
    private SessionBean sessionBean;

    @Resource
    private CustomerRepository customerRepository;
    @Resource
    private ProductRepository productRepository;

    public String addProductToCart(long productId) {
        // Customer customer = sessionBean.getCustomer();
        // if (customer == null) {
        // return "login";
        // }
        // Product product = productRepository.findOne(productId);
        // ShoppingCartItem item = new ShoppingCartItem();
        // item.setProduct(product);
        // if (customer.getShoppingCart() == null) {
        // customer.setShoppingCart(new ShoppingCart());
        // }
        // customer.getShoppingCart().getItems().add(item);
        // customerRepository.save(customer);
        return null;
    }

}
