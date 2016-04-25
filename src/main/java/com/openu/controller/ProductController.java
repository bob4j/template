package com.openu.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;
import javax.faces.event.AjaxBehaviorEvent;

import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.openu.model.Customer;
import com.openu.model.Order;
import com.openu.model.OrderItem;
import com.openu.model.OrderStatus;
import com.openu.model.Product;
import com.openu.model.ProductColor;
import com.openu.model.ProductSize;
import com.openu.repository.CustomerRepository;
import com.openu.repository.ProductRepository;
import com.openu.repository.StockItemRepository;
import com.openu.util.Utils;

@Component
@Scope("view")
public class ProductController implements Serializable {

    private static final long serialVersionUID = 1L;

    @Resource
    private ProductRepository productRepository;

    @Resource
    private StockItemRepository stockItemRepository;

    @Resource
    private CustomerRepository customerRepository;

    @Resource
    private SessionBean sessionBean;

    private ProductColor selectedColor;

    private ProductSize selectedSize;

    private Product currentProduct;

    private Integer selectedQuantity = 1;

    @Transactional
    public String removeFromShoppingCart(long orderId) {
        Customer loggedInCustomer = sessionBean.getCustomer();
        if (loggedInCustomer == null) {
            return "login";
        }
        Customer customer = customerRepository.findByUsername(loggedInCustomer.getUsername());
        Order shoppingCart = customer.getShoppingCart();
        shoppingCart.getItems().removeIf(i -> i.getId().equals(orderId));
        customerRepository.save(customer);
        return null;
    }

    @Transactional
    public String addToShoppingCart() {
        if (selectedColor == null || selectedSize == null) {
            return null;
        }
        Customer loggedInCustomer = sessionBean.getCustomer();
        if (loggedInCustomer == null) {
            return "login";
        }
        Customer customer = customerRepository.findByUsername(loggedInCustomer.getUsername());
        Order shoppingCart = customer.getShoppingCart();
        if (shoppingCart == null) {
            shoppingCart = new Order();
            shoppingCart.setStatus(OrderStatus.OPEN);
            shoppingCart.setCustomer(customer);
            customer.getOrders().add(shoppingCart);
        }
        OrderItem item = new OrderItem();
        item.setProduct(currentProduct);
        item.setColor(selectedColor);
        item.setSize(selectedSize);
        item.setPrice(currentProduct.getPrice());
        item.setQuantity(selectedQuantity);
        shoppingCart.getItems().add(item);
        customerRepository.save(customer);
        return null;
    }

    /**
     * Called from the homepage
     *
     * @return 6 newest products
     */
    public Iterable<Product> getNewProducts() {
        return productRepository.findAll(new PageRequest(0, 6, new Sort(new Sort.Order(Direction.DESC, "id")))).getContent();
    }

    /**
     * Loads the product by the request parameters - productdetail.jsf?category_id=<ID>
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

    public Integer getSelectedQuantity() {
        return selectedQuantity;
    }

    public void setSelectedQuantity(Integer selectedQuantity) {
        this.selectedQuantity = selectedQuantity;
    }
}
