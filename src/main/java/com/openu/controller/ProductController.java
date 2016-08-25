package com.openu.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;
import javax.faces.event.AjaxBehaviorEvent;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

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
import com.openu.model.StockItem;
import com.openu.repository.CustomerRepository;
import com.openu.repository.ProductRepository;
import com.openu.repository.StockItemRepository;
import com.openu.util.CustomerTransaction;
import com.openu.util.Utils;

@Component
@Scope("view")
public class ProductController implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final int MAX_RELATED = 6;

    @Resource
    private EntityManagerFactory entityManagerFactory;

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
    
    private String notAvailableErrorMessage = "";

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
    
    private Customer getCustomerForCart(){
	Customer loggedInCustomer = sessionBean.getCustomer();
        if (loggedInCustomer == null) {
            return null;
        }
        return customerRepository.findByUsername(loggedInCustomer.getUsername());
    }

    @CustomerTransaction
    @Transactional
    public String addToShoppingCart() {
        if (!isStockItemAvailable()){
            if (getSelectedColor()!= null && getSelectedSize()!=null ){
        	setNotAvailableErrorMessage("The selected product quantity exceeds the number of products in stock");
            }
            return null;
        }
        Customer customer = getCustomerForCart();
        if (customer == null){
            return "login";
        }
        Order shoppingCart = customer.getShoppingCart();
        if (shoppingCart == null) {
            shoppingCart = new Order();
            shoppingCart.setStatus(OrderStatus.OPEN);
            shoppingCart.setCustomer(customer);
            customer.getOrders().add(shoppingCart);
        }
        if (checkForDuplication(shoppingCart)){
            //customerRepository.save(customer);
            return null;
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

    private boolean checkForDuplication(Order shoppingCart) {
	List<OrderItem> ShoppingCartitems = shoppingCart.getItems();
        for (OrderItem  orderItem : ShoppingCartitems) {
            boolean isDuplicateItem = orderItem.getColor().equals(selectedColor) && 
        	    orderItem.getSize().equals(selectedSize)&&
        	    orderItem.getProduct().getId().equals(currentProduct.getId());
	    if(isDuplicateItem ){
		orderItem.setQuantity(orderItem.getQuantity()+selectedQuantity);
	        return true;
	    }
	}
        return false;
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
     * called from the homepage
     */
    public Iterable<Product> getMostOrdered() {
        EntityManager em = entityManagerFactory.createEntityManager();
        TypedQuery<Product> query = em.createQuery(
                "select p1 from Product p1 where p1 in (select oi.product from OrderItem oi group by oi.product order by sum(oi.quantity) desc)",
                Product.class);
        query.setMaxResults(5);
        try {
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Loads the product by the request parameters - productdetail.jsf?category_id=<ID>
     */
    public Product getProduct() {
        String productId = Utils.getRequest().getParameter("product_id");
        if (productId != null) {
            currentProduct = loadProduct(productId);
        }
        return currentProduct;
    }

    private Product loadProduct(String productId) {
        return productRepository.findOne(Long.valueOf(productId));
    }

    /**
     * @return returns products from the same category (excluding the product itself)
     */
    @Transactional
    public List<Product> getRelated() {
        List<Product> related = new ArrayList<>();
        Product product = getProduct();
        product.getCategories().forEach(c -> related.addAll(c.getProducts()));
        Collections.shuffle(related);
        related.remove(product);
        if (related.isEmpty() || related.size() < MAX_RELATED) {
            return related;
        }
        return related.subList(0, MAX_RELATED);
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
        setNotAvailableErrorMessage("");
    }

    public ProductSize getSelectedSize() {
        return selectedSize;
    }

    public void setSelectedSize(ProductSize selectedSize) {
        this.selectedSize = selectedSize;
        setNotAvailableErrorMessage("");

    }

    public Integer getSelectedQuantity() {
        return selectedQuantity;
    }

    public void setSelectedQuantity(Integer selectedQuantity) {
        this.selectedQuantity = selectedQuantity;
        setNotAvailableErrorMessage("");

    }
    
    @CustomerTransaction
    private boolean isStockItemAvailable(){
	StockItem selectedStockItem = stockItemRepository.findStockItemByFields(getProduct(), getSelectedColor(), getSelectedSize());
	if (selectedStockItem == null ) {
	    return false; 
	}
	
        Customer customer = getCustomerForCart();
        
        List<OrderItem> ShoppingCartitems = customer.getShoppingCart().getItems();
	for (OrderItem orderItem : ShoppingCartitems) {
	    if (selectedStockItem.isEqualToOrderItem(orderItem)){
		return selectedStockItem.getQuantity() >= (selectedQuantity + orderItem.getQuantity());
	    }
	}
	return selectedStockItem.getQuantity() >= selectedQuantity;
    }
    
    public String  getAvailability(){
	return isStockItemAvailable()?"Available":"Not Available";
    }

    public String getNotAvailableErrorMessage() {
	return notAvailableErrorMessage;
    }

    public void setNotAvailableErrorMessage(String notAvailableErrorMessage) {
	this.notAvailableErrorMessage = notAvailableErrorMessage;
    }
    
    
}
