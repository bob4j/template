package com.openu.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

import com.openu.model.Product;
import com.openu.model.ProductColor;
import com.openu.model.ProductSize;
import com.openu.repository.ProductRepository;
import com.openu.repository.StockItemRepository;
import com.openu.util.Utils;

@ManagedBean
@ViewScoped
@Component
public class ProductController implements Serializable {

    private static final long serialVersionUID = 1L;

    @Resource
    private ProductRepository productRepository;

    @Resource
    private StockItemRepository stockItemRepository;

    private ProductColor selectedColor;

    private ProductSize selectedSize;

    private Product currentProduct;

    /**
     * Called from the homepage
     *
     * @return 6 newest products
     */
    public Iterable<Product> getNewProducts() {
        return productRepository.findAll(new PageRequest(0, 6, new Sort(new Sort.Order(Direction.DESC, "id")))).getContent();
    }

    /**
     * Loads the product by the request parameters - category.jsf?category_id=<ID>
     */
    public Product getProduct() {
        String productId = Utils.getRequest().getParameter("product_id");
        if (productId != null) {
            currentProduct = productRepository.findOne(Long.valueOf(productId));
        }
        return currentProduct;
    }

    /**
     * Gets all colors of the product which are in stock
     */
    public List<ProductColor> getColors() {
        return stockItemRepository.findDistinctColorByProduct(currentProduct);
    }

    /**
     * Gets all sizes of the product (and color) which are in stock
     */
    public List<ProductSize> getSizes() {
        return stockItemRepository.findDistinctSizeByProductAndColor(currentProduct, selectedColor);
    }

    /**
     * Might be invoked by ajax components
     */
    public void listener(AjaxBehaviorEvent event) {
    }

    public ProductColor getSelectedColor() {
        return selectedColor;
    }

    public void setSelectedColor(ProductColor selectedColor) {
        this.selectedColor = selectedColor;
    }

    public ProductSize getSelectedSize() {
        return selectedSize;
    }

    public void setSelectedSize(ProductSize selectedSize) {
        this.selectedSize = selectedSize;
    }
}
