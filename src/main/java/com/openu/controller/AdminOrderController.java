package com.openu.controller;

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
import com.openu.model.Order;
import com.openu.model.OrderItem;
import com.openu.model.OrderStatus;
import com.openu.model.Product;
import com.openu.model.ProductColor;
import com.openu.model.ProductSize;
import com.openu.model.StockItem;
import com.openu.repository.OrderRepository;
import com.openu.repository.StockItemRepository;
import com.openu.service.ApprovedOrderEmailSender;
import com.openu.service.CancelOrderEmailSender;
import com.openu.service.RejectedOrderEmailSender;
import com.openu.service.ShippedOrderEmailSender;
import com.openu.util.Constants;
import com.openu.util.FilterManager;
import com.openu.util.Utils;

@Component
@Scope("view")
public class AdminOrderController extends AbstractCrudController<Order> {

    private OrderStatus selectedStatus = OrderStatus.NOT_SLECTED;
    private ArrayList<String> statusesList;
    private String selectedCustomer = Constants.NO_CUSTOMER_SELECTED;
    private Date selectedCreationDate;
    private Date selectedModifiedDate;

    // ================== Email Senders=============
    @Resource
    private CancelOrderEmailSender cancelOrderEmailSender;

    @Resource
    private RejectedOrderEmailSender rejectedOrderEmailSender;

    @Resource
    private ApprovedOrderEmailSender approvedOrderEmailSender;

    @Resource
    private ShippedOrderEmailSender shippedOrderEmailSender;

    // ========Repository================
    @Resource
    private OrderRepository orderRepository;

    @Resource
    private StockItemRepository stockItemRepository;

    // =========EntityManager===============
    @Resource
    private EntityManagerFactory entityManagerFactory;

    // ==================================================================================================

    // ==============Sort==============================================
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

    // ==========FILTER=====================================

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
            predicates.add(filterManager.getJoinStringFieldPredicate(new String[] { term }, Constants.CUSTOMER, Constants.CUSTOMER_FIRST_NAME,
                    Constants.LIKE_TEMPLATE));
            predicates.add(filterManager.getJoinStringFieldPredicate(new String[] { term }, Constants.CUSTOMER, Constants.CUSTOMER_LAST_NAME,
                    Constants.LIKE_TEMPLATE));
        }
        if (!predicates.isEmpty()) {
            filterManager.addPredicate(filterManager.getCriteriaBuilder().or(predicates.toArray(new Predicate[0])));
        }
    }

    private Predicate getCreationDatePredicate(CriteriaBuilder criteriaBuilder, Root<Order> root) {
        return getDatePredicate(criteriaBuilder, root, selectedCreationDate, Constants.CREATED);
    }

    private Predicate getModifiedDatePredicate(CriteriaBuilder criteriaBuilder, Root<Order> root) {
        return getDatePredicate(criteriaBuilder, root, selectedModifiedDate, Constants.MODIFIED);
    }

    private Predicate getDatePredicate(CriteriaBuilder criteriaBuilder, Root<Order> root, Date date, String field) {
        if (date == null) {
            return null;
        }
        Calendar until = Calendar.getInstance();
        until.setTime(date);
        until.set(Calendar.HOUR, Constants.MAX_HOUR_IN_DAY);
        until.set(Calendar.MINUTE, Constants.MAX_MIN_IN_HOUR);
        return criteriaBuilder.between(root.get(field), date.getTime(), until.getTimeInMillis());
    }

    public void clearFilter() {
        selectedCustomer = Constants.NO_CUSTOMER_SELECTED;
        selectedStatus = OrderStatus.NOT_SLECTED;
        selectedCreationDate = null;
        selectedModifiedDate = null;
    }

    // ===========Filter getters and Setters
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

    public String getSelectedCustomer() {
        return selectedCustomer;
    }

    public void setSelectedCustomer(String selectedCustomer) {
        this.selectedCustomer = Optional.ofNullable(selectedCustomer).map(c -> c.toLowerCase()).orElse(Constants.NO_CUSTOMER_SELECTED);
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

    // ===================end filter getter and setters========================================

    // ===============================Order Status Action =====================
    @Transactional
    public void shipOrder(Order order) {
        if (order.getStatus() != OrderStatus.APPROVED) {
            throw new IllegalArgumentException(Constants.CANNOT_SHIP_ORDER_WITH_STATUS + order.getStatus());
        }
        order.setStatus(OrderStatus.SHIPPED);
        order.setModified(System.currentTimeMillis());
        orderRepository.save(order);
        shippedOrderEmailSender.sendOrderEmail(order);
    }

    @Transactional
    public void rejectOrder(Order order) {
        if (order.getStatus() != OrderStatus.SHIPPED) {
            throw new IllegalArgumentException(Constants.CANNOT_REJECT_ORDER_WITH_STATUS + order.getStatus());
        }
        updateStockItemRepositoryAfterAction(order, false);
        order.setStatus(OrderStatus.REJECTED);
        order.setModified(System.currentTimeMillis());
        orderRepository.save(order);
        rejectedOrderEmailSender.sendOrderEmail(order);
    }

    @Transactional
    public void approveOrder(Order order) {
        if (order.getStatus() != OrderStatus.PLACED) {
            throw new IllegalArgumentException(Constants.CANNOT_APROVE_ORDER_WITH_STATUS + order.getStatus());
        }
        boolean aproveSuccess = updateStockItemRepositoryAfterApprove(order);
        if (!aproveSuccess) {
            return;
        }
        order.setModified(System.currentTimeMillis());
        order.setStatus(OrderStatus.APPROVED);
        orderRepository.save(order);
        approvedOrderEmailSender.sendOrderEmail(order);
    }

    private synchronized boolean updateStockItemRepositoryAfterApprove(Order order) {
        if (isAllOrderStockItemsAvailable(order)) {
            StockItem selectedStockItem;
            List<OrderItem> orderItems = order.getItems();
            if (orderItems == null) {
                return false;
            }
            for (OrderItem orderItem : orderItems) {
                selectedStockItem = stockItemRepository.findStockItemByFields(orderItem.getProduct(), orderItem.getColor(), orderItem.getSize());
                decreaseStockItemQuantity(selectedStockItem, orderItem);
            }
            return true;
        }
        return false;

    }

    public boolean canCancelOrder(Long orderId) {
        Order order = orderRepository.findOne(orderId);
        if (order == null) {
            return false;
        }
        return order.getStatus().equals(OrderStatus.PLACED) || order.getStatus().equals(OrderStatus.APPROVED);
    }

    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findOne(orderId);
        cancelOrder(order);
    }

    @Transactional
    public void cancelOrder(Order order) {
        boolean shouldUpdateStockItems = order.getStatus().equals(OrderStatus.APPROVED);
        if (shouldUpdateStockItems) {
            updateStockItemRepositoryAfterAction(order, false);
        }
        order.setStatus(OrderStatus.CANCELLED);
        order.setModified(System.currentTimeMillis());
        orderRepository.save(order);
        cancelOrderEmailSender.sendOrderEmail(order);
    }

    @Transactional
    public void placeOrder(Order order) {
        if (order.getStatus() == OrderStatus.CANCELLED || order.getStatus() == OrderStatus.REJECTED) {
            order.setStatus(OrderStatus.PLACED);
            order.setModified(System.currentTimeMillis());
            orderRepository.save(order);

        } else {
            throw new IllegalArgumentException(Constants.CANNOT_PLACE_ORDER_WITH_STATUS + order.getStatus());
        }
    }

    // ============================order status Action getters and setters====================
    public String getOrderActionName(Long orderId) {
        Order order = orderRepository.findOne(orderId);
        switch (order.getStatus()) {
        case APPROVED:
            return OrderStatus.SHIPPED.getNameForUI();
        case CANCELLED:
            return OrderStatus.PLACED.getNameForUI();
        case PLACED:
            if (isAllOrderStockItemsAvailable(order)) {
                return OrderStatus.APPROVED.getNameForUI();
            } else {
                return OrderStatus.CANCELLED.getNameForUI();
            }
        case REJECTED:
            return OrderStatus.PLACED.getNameForUI();
        case SHIPPED:
            return OrderStatus.REJECTED.getNameForUI();
        default:
            return null;
        }
    }

    public void ApplyAction(Long orderId) {
        Order order = orderRepository.findOne(orderId);
        switch (order.getStatus()) {
        case APPROVED:
            shipOrder(order);
            break;
        case CANCELLED:
            placeOrder(order);
            break;
        case PLACED:
            if (isAllOrderStockItemsAvailable(order)) {
                approveOrder(order);
            } else {
                cancelOrder(order);
            }
            break;
        case REJECTED:
            placeOrder(order);
            break;
        case SHIPPED:
            rejectOrder(order);
            break;

        default:
            break;
        }
    }

    // =======================order status Action Utility===================

    /**
     *
     * @param orderId
     * @param decreaseStockItemQuantity
     *            -true if after the action we need to decrease the quantity of the stock items in the order - false if
     *            after the action we need to increase the quantity of the stock items in the order
     */
    private void updateStockItemRepositoryAfterAction(Order order, boolean decreaseStockItemQuantity) {
        StockItem selectedStockItem;
        List<OrderItem> orderItems = order.getItems();
        if (orderItems == null) {
            return;
        }
        for (OrderItem orderItem : orderItems) {
            selectedStockItem = stockItemRepository.findStockItemByFields(orderItem.getProduct(), orderItem.getColor(), orderItem.getSize());
            if (decreaseStockItemQuantity) {
                decreaseStockItemQuantity(selectedStockItem, orderItem);
            } else {
                increaseStockItemQuantity(selectedStockItem, orderItem);
            }
            stockItemRepository.save(selectedStockItem);
        }
    }

    private synchronized void increaseStockItemQuantity(StockItem selectedStockItem, OrderItem orderItem) {
        selectedStockItem.setQuantity(selectedStockItem.getQuantity() + orderItem.getQuantity());
    }

    private synchronized void decreaseStockItemQuantity(StockItem selectedStockItem, OrderItem orderItem) {
        selectedStockItem.setQuantity(selectedStockItem.getQuantity() - orderItem.getQuantity());
    }

    public List<Order> getPlacedOrders() {
        return orderRepository.findAll((root, query, cb) -> cb.and(cb.notEqual(root.get(Constants.STATUS), OrderStatus.PLACED)));
    }

    public Order getOrder() {
        String orderId = Utils.getRequest().getParameter(Constants.ORDER_ID);
        if (orderId != null) {
            return orderRepository.findOne(Long.valueOf(orderId));
        }
        return null;
    }

    public synchronized boolean isAllOrderStockItemsAvailable(Order order) {
        StockItem selectedItem;
        List<OrderItem> items = order.getItems();
        if (items == null) {
            return false;
        }
        for (OrderItem orderItem : items) {
            selectedItem = stockItemRepository.findStockItemByFields(orderItem.getProduct(), orderItem.getColor(), orderItem.getSize());
            if (selectedItem == null || selectedItem.getQuantity() < orderItem.getQuantity()) {
                return false;
            }
        }
        return true;
    }

    public String getItemOrderAvailability(OrderItem item) {
        ProductColor color = item.getColor();
        ProductSize size = item.getSize();
        Product product = item.getProduct();
        StockItem findStockItemByFields = stockItemRepository.findStockItemByFields(product, color, size);
        boolean isItemAvailable = findStockItemByFields.getQuantity() >= item.getQuantity();
        return isItemAvailable ? Constants.ITEM_AVAILABLE : Constants.ITEM_NOT_AVAILABLE;
    }

}
