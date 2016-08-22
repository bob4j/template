package com.openu.controller.admin;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.openu.controller.AbstractCrudController;
import com.openu.controller.Constants;
import com.openu.model.Order;
import com.openu.model.OrderItem;
import com.openu.model.OrderStatus;
import com.openu.model.StockItem;
import com.openu.repository.OrderRepository;
import com.openu.repository.StockItemRepository;
import com.openu.util.FilterManager;
import com.openu.util.Utils;

@Component
@Scope("view")
public class AdminOrderController extends AbstractCrudController<Order> {

    private static final String CUSTOMER_FIRST_NAME = "firstName";
    private static final String CUSTOMER_LAST_NAME = "lastName";
    private static final String CUSTOMER = "customer";

    private OrderStatus selectedStatusAction = OrderStatus.NOT_SLECTED;
    private OrderStatus selectedStatus = OrderStatus.NOT_SLECTED;
    private ArrayList<String> statusesList;
    private String selectedCustomer = "";
    private Date selectedCreationDate;
    private Date selectedModifiedDate;

    @Resource
    private OrderRepository orderRepository;
    
    @Resource
    private StockItemRepository stockItemRepository;
    
    @Resource
    private EntityManagerFactory entityManagerFactory;

    @Transactional
    public void approve(long orderId) {
	Order order = orderRepository.findOne(orderId);
	if (order.getStatus() != OrderStatus.PLACED) {
	    throw new IllegalArgumentException();
	}
	if (isAllOrderStockItemsAvailable(orderId)) {
	    order.setStatus(OrderStatus.APPROVED);
	    orderRepository.save(order);
	    List<OrderItem> orderItems = getOrderItems(orderId);
	    for (OrderItem orderItem : orderItems) {
		 List <StockItem> selectedItems = stockItemRepository.findStockItemByFields(orderItem.getProduct(),
	   		    orderItem.getColor(), orderItem.getSize());
		 StockItem selectedStockItem = selectedItems.get(0);
		Integer oldQuantity = selectedStockItem.getQuantity();
		selectedStockItem.setQuantity(oldQuantity - orderItem.getQuantity());
		 stockItemRepository.save(selectedStockItem);
	    }
	}
    }

    @Transactional
    public void ship(long orderId) {
        Order order = orderRepository.findOne(orderId);
        if (order.getStatus() != OrderStatus.APPROVED) {
            throw new IllegalArgumentException();
        }
        order.setStatus(OrderStatus.SHIPPED);
        orderRepository.save(order);
    }

    public List<Order> getPlacedOrders() {
        return orderRepository.findAll((root, query, cb) -> cb.and(cb.notEqual(root.get(Constants.STATUS), OrderStatus.PLACED)));
    }

    @Override
    protected PagingAndSortingRepository<Order, Long> getRepository() {
        return orderRepository;
    }

    @Override
    protected Order createEntity() throws Exception {
        throw new UnsupportedOperationException();
    }

    public void statusUp() {
        setSortBy(Constants.STATUS);
        setDirection(Direction.ASC);
    }

    public void statusDown() {
        setSortBy(Constants.STATUS);
        setDirection(Direction.DESC);
    }

    public void modifiedUp() {
        setSortBy(Constants.MODIFIED);
        setDirection(Direction.ASC);
    }

    public void modifiedDown() {
        setSortBy(Constants.MODIFIED);
        setDirection(Direction.DESC);
    }

    public void creationUp() {
        setSortBy(Constants.CREATED);
        setDirection(Direction.ASC);
    }

    public void creationDown() {
        setSortBy(Constants.CREATED);
        setDirection(Direction.DESC);
    }

    public void customerUp() {
        setSortBy(Constants.CUSTOMER);
        setDirection(Direction.ASC);
    }

    public void customerDown() {
        setSortBy(Constants.CUSTOMER);
        setDirection(Direction.DESC);
    }

    @Override
    protected FilterManager<Order> getFilterManager() {
        return new FilterManager<>(Order.class, entityManagerFactory);
    }

    @Override
    protected void createPredicatesList(FilterManager<Order> filterManager) {
        super.createPredicatesList(filterManager);
        CriteriaBuilder criteriaBuilder = filterManager.getCriteriaBuilder();
        Root<Order> root = filterManager.getRoot();
        Predicate statusPredicate = getStatusPredicate(criteriaBuilder, root);
        Predicate customerPredicate = getCustomerPredicate(filterManager);
        Predicate creationDatePredicate = getCreationDatePredicate(criteriaBuilder, root);
        Predicate modifiedDatePredicate = getModifiedDatePredicate(criteriaBuilder, root);
        filterManager.addPredicates(statusPredicate, creationDatePredicate, customerPredicate, modifiedDatePredicate);
    }

    private Predicate getStatusPredicate(CriteriaBuilder criteriaBuilder, Root<Order> root) {
        if (selectedStatus != OrderStatus.NOT_SLECTED) {
            return criteriaBuilder.equal(root.<OrderStatus> get(Constants.STATUS), selectedStatus);
        } else {
            return null;
        }
    }

    private Predicate getCustomerPredicate(FilterManager<Order> filterManager) {
        String[] customerArray = { selectedCustomer };
        Predicate firstNamePredicate = filterManager.getJoinStringFieldPredicate(customerArray, CUSTOMER, CUSTOMER_FIRST_NAME, "%value%");
        Predicate lastNamePredicate = filterManager.getJoinStringFieldPredicate(customerArray, CUSTOMER, CUSTOMER_LAST_NAME, "%value%");
        return filterManager.getCriteriaBuilder().or(firstNamePredicate, lastNamePredicate);
    }

    private Predicate getCreationDatePredicate(CriteriaBuilder criteriaBuilder, Root<Order> root) {
        return getDatePredicate(criteriaBuilder, root, selectedCreationDate, "created");
    }

    private Predicate getModifiedDatePredicate(CriteriaBuilder criteriaBuilder, Root<Order> root) {
        return getDatePredicate(criteriaBuilder, root, selectedModifiedDate, "modified");
    }

    private Predicate getDatePredicate(CriteriaBuilder criteriaBuilder, Root<Order> root, Date date, String field) {
        if (date == null) {
            return null;
        }
        Calendar until = Calendar.getInstance();
        until.setTime(date);
        until.set(Calendar.HOUR, 23);
        until.set(Calendar.MINUTE, 59);
        return criteriaBuilder.between(root.get(field), date.getTime(), until.getTimeInMillis());
    }

    public List<String> getStatusesList() {
        statusesList = new ArrayList<>();
        for (OrderStatus status : OrderStatus.values()) {
            statusesList.add(status.getNameForUI());
        }
        return statusesList;

    }

    public String getSelectedStatus() {
        return selectedStatus.getNameForUI();
    }

    public void setSelectedStatus(String selectedStatus) {
        this.selectedStatus = OrderStatus.getValueByNameForUI(selectedStatus);
    }

    public String getSelectedStatusAction() {
	return selectedStatusAction.getNameForUI();
    }

    public void setSelectedStatusAction(String selectedStatusAction) {
	this.selectedStatusAction = OrderStatus.getValueByNameForUI(selectedStatusAction);  
	}

    public String getSelectedCustomer() {
        return selectedCustomer;
    }

    public void setSelectedCustomer(String selectedCustomer) {
        this.selectedCustomer = selectedCustomer;
    }

    public void clearFilter() {
        selectedCustomer = "";
        selectedStatus = OrderStatus.NOT_SLECTED;
        selectedCreationDate = null;
        selectedModifiedDate = null;
    }

    public Date getSelectedCreationDate() {
        return selectedCreationDate;
    }

    public void setSelectedCreationDate(Date selectedCreationDate) {
        this.selectedCreationDate = selectedCreationDate;
    }

    public Date getSelectedModifiedDate() {
        return selectedModifiedDate;
    }

    public void setSelectedModifiedDate(Date selectedModifiedDate) {
        this.selectedModifiedDate = selectedModifiedDate;
    }
    
    private List<OrderItem> getOrderItems(Long orderId){
	Order order = orderRepository.findOne(orderId);
	return order.getItems();
    }

    public Order getOrder() {
	String orderId = Utils.getRequest().getParameter("order_id");
	if (orderId != null) {
	     return orderRepository.findOne(Long.valueOf(orderId));
	}
	return null;
    }
    public boolean isAllOrderStockItemsAvailable(Long orderId) {
	List<OrderItem> items = getOrderItems(orderId);
   	for (OrderItem orderItem : items) {
   	    List <StockItem> selectedItem = stockItemRepository.findStockItemByFields(orderItem.getProduct(),
   		    orderItem.getColor(), orderItem.getSize());
   	    //TODO Change to StockItem
   	    if (selectedItem == null || selectedItem.get(0).getQuantity() < orderItem.getQuantity()) {
   		return false;
   	    }
   	}
   	return true;
       }

    @Transactional
    public void cancelOrder(Long orderId){
	Order order = orderRepository.findOne(orderId);
	order.setStatus(OrderStatus.CANCELLED);
	sendEmail();
	orderRepository.save(order);
    }
    
    private void sendEmail(){
	
    }

}
