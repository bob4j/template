package com.openu.controller.admin;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Splitter;
import com.openu.controller.AbstractCrudController;
import com.openu.controller.Constants;
import com.openu.model.Order;
import com.openu.model.OrderItem;
import com.openu.model.OrderStatus;
import com.openu.model.Product;
import com.openu.model.ProductColor;
import com.openu.model.ProductSize;
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
    public synchronized void  approve(long orderId) {
        Order order = orderRepository.findOne(orderId);   
        if (order.getStatus().equals(OrderStatus.SHIPPED)){
            order.setStatus(OrderStatus.APPROVED);
            orderRepository.save(order);
            sendEmail(order);
        } else if (isAllOrderStockItemsAvailable(orderId)) {
            order.setStatus(OrderStatus.APPROVED);
            orderRepository.save(order);
            updateStockItemRepositoryAfterAction(orderId, true);
            sendEmail(order);
        }
        
    }

    /**
     *
     * @param orderId
     * @param decreaseStockItemQuantity
     *            -true if after the action we need to decrease the quantity of the stock items in the order - false if
     *            after the action we need to increase the quantity of the stock items in the order
     */
    private void updateStockItemRepositoryAfterAction(long orderId, boolean decreaseStockItemQuantity) {
	StockItem selectedStockItem;
	List<OrderItem> orderItems = getOrderItems(orderId);
        for (OrderItem orderItem : orderItems) {
            selectedStockItem = stockItemRepository.findStockItemByFields(orderItem.getProduct(), orderItem.getColor(),
                    orderItem.getSize());
            Integer oldQuantity = selectedStockItem.getQuantity();
            if (decreaseStockItemQuantity) {
                selectedStockItem.setQuantity(oldQuantity - orderItem.getQuantity());
            } else  {
        	 selectedStockItem.setQuantity(oldQuantity + orderItem.getQuantity());
            }
            stockItemRepository.save(selectedStockItem);
        }
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
        setCustomerPredicate(filterManager);
        Predicate creationDatePredicate = getCreationDatePredicate(criteriaBuilder, root);
        Predicate modifiedDatePredicate = getModifiedDatePredicate(criteriaBuilder, root);
        filterManager.addPredicates(statusPredicate, creationDatePredicate, modifiedDatePredicate);
    }

    private Predicate getStatusPredicate(CriteriaBuilder criteriaBuilder, Root<Order> root) {
        if (selectedStatus != OrderStatus.NOT_SLECTED) {
            return criteriaBuilder.equal(root.<OrderStatus> get(Constants.STATUS), selectedStatus);
        } else {
            return null;
        }
    }

    private void setCustomerPredicate(FilterManager<Order> filterManager) {
        List<String> terms = Splitter.on(" ").trimResults().omitEmptyStrings().splitToList(selectedCustomer);
        List<Predicate> predicates = new ArrayList<>();
        for (String term : terms) {
            predicates.add(filterManager.getJoinStringFieldPredicate(new String[] { term }, CUSTOMER, CUSTOMER_FIRST_NAME, "%value%"));
            predicates.add(filterManager.getJoinStringFieldPredicate(new String[] { term }, CUSTOMER, CUSTOMER_LAST_NAME, "%value%"));
        }
        if (!predicates.isEmpty()) {
            filterManager.addPredicate(filterManager.getCriteriaBuilder().or(predicates.toArray(new Predicate[0])));
        }
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
        this.selectedCustomer = Optional.ofNullable(selectedCustomer).map(c -> c.toLowerCase()).orElse("");
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

    private List<OrderItem> getOrderItems(Long orderId) {
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
	StockItem selectedItem;
	List<OrderItem> items = getOrderItems(orderId);
        for (OrderItem orderItem : items) {
            selectedItem = stockItemRepository.findStockItemByFields(orderItem.getProduct(), orderItem.getColor(),
                    orderItem.getSize());
            if (selectedItem == null || selectedItem.getQuantity() < orderItem.getQuantity()) {
                return false;
            }
        }
        return true;
    }

    @Transactional
    public void ship(long orderId) {
        Order order = orderRepository.findOne(orderId);
        order.setStatus(OrderStatus.SHIPPED);
        orderRepository.save(order);
        sendEmail(order);
    }
    @Transactional
    public synchronized void rejectOrder(Long orderId) {
        Order order = orderRepository.findOne(orderId);
        if (order.getStatus() != OrderStatus.SHIPPED){
            throw new IllegalArgumentException("cannot reject order with status " + order.getStatus());
        }
        order.setStatus(OrderStatus.REJECTED);
        updateStockItemRepositoryAfterAction(orderId, false);
        orderRepository.save(order);
        sendEmail(order);
    }
 
    @Transactional
    public synchronized void cancelOrder(Long orderId) {
        Order order = orderRepository.findOne(orderId);
        order.setStatus(OrderStatus.CANCELLED);
        boolean shouldUpdateStockItems = order.getStatus().equals(OrderStatus.APPROVED) || order.getStatus().equals(OrderStatus.SHIPPED);
        if (shouldUpdateStockItems) {
            updateStockItemRepositoryAfterAction(orderId, false);
        }
        orderRepository.save(order);
        sendEmail(order);
    }

    @Transactional
    public synchronized  void PlaceOrder(Long orderId) {
        Order order = orderRepository.findOne(orderId);
        boolean shouldUpdateStockItems = order.getStatus().equals(OrderStatus.APPROVED) || order.getStatus().equals(OrderStatus.SHIPPED);
        if (shouldUpdateStockItems) {
            updateStockItemRepositoryAfterAction(orderId, false);
        }
        order.setStatus(OrderStatus.PLACED);
        orderRepository.save(order);
        sendEmail(order);
    }

    private void sendEmail(Order order) {

    }

    public String getItemOrderAvailability(OrderItem item) {
        ProductColor color = item.getColor();
        ProductSize size = item.getSize();
        Product product = item.getProduct();
        StockItem findStockItemByFields = stockItemRepository.findStockItemByFields(product, color, size);
        boolean isItemAvailable = findStockItemByFields.getQuantity() >= item.getQuantity();
        return isItemAvailable ? "Available" : "Not Available";

    }

    public void applyStatusAction() {

        Long orderId = getOrderIdForApplyStatusAction();
        if (orderId == null) {
            return;
        }

        switch (selectedStatusAction) {
        case NOT_SLECTED:
            return;
        case APPROVED:
            approve(orderId);
        case CANCELLED:
            cancelOrder(orderId);
            return;
        case OPEN:
            return;
        case PLACED:
            PlaceOrder(orderId);
            return;
        case REJECTED:
            return;
        case SHIPPED:
            ship(orderId);
            return;
        default:
            break;
        }
    }

    private Long getOrderIdForApplyStatusAction() {
        String id = Utils.getRequest().getParameter("apply_status_order_id");
        return Long.valueOf(id);
    }
    
    public List <OrderStatus> getStatusesActionList() {
	
        Order order = getOrder();
        if (order == null){
          return null;  
        }
        ArrayList<OrderStatus> statussesActionList = new ArrayList<OrderStatus>();
        OrderStatus status = order.getStatus();
	switch (status) {
	case OPEN:
	     statussesActionList.add(OrderStatus.OPEN);
	     break;
	case CANCELLED:
	     statussesActionList.add(OrderStatus.PLACED);
	     statussesActionList.add(OrderStatus.APPROVED);
	     statussesActionList.add(OrderStatus.CANCELLED);

	     break;
	case APPROVED:
	    statussesActionList.add(OrderStatus.PLACED);
	    statussesActionList.add(OrderStatus.SHIPPED);
	    statussesActionList.add(OrderStatus.CANCELLED);
	     statussesActionList.add(OrderStatus.APPROVED);

	    break;
	case SHIPPED:
	    statussesActionList.add(OrderStatus.PLACED);
	    statussesActionList.add(OrderStatus.APPROVED);
	     statussesActionList.add(OrderStatus.SHIPPED);
	     statussesActionList.add(OrderStatus.REJECTED);


	    break;

	default:
	    return null;
	}
	return statussesActionList;

    }

}
