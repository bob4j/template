package com.openu.controller.admin;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

import com.openu.controller.AbstractCrudController;
import com.openu.controller.Constants;
import com.openu.model.Order;
import com.openu.model.OrderStatus;
import com.openu.repository.OrderRepository;
import com.openu.util.FilterManager;
import com.openu.util.Utils;

@Component
@Scope("view")
public class AdminOrderController extends AbstractCrudController<Order> {

    private static final String CUSTOMER_FIRST_NAME = "firstName";
    private static final String CUSTOMER_LAST_NAME = "lastName";
    private static final String CUSTOMER = "customer";

    private OrderStatus selectedStatus = OrderStatus.NOT_SLECTED;
    private ArrayList<String> statusesList;
    private String selectedCustomer = "";
    private Date selectedCreationDate;
    private Date selectedModifiedDate;

    @Resource
    private OrderRepository orderRepository;
    @Resource
    private EntityManagerFactory entityManagerFactory;

    public Order getOrder() {
        String orderId = Utils.getRequest().getParameter("order_id");
        if (orderId != null) {
            return orderRepository.findOne(Long.valueOf(orderId));
        }
        return null;
    }

    @Transactional
    public void approve(long orderId) {
        Order order = orderRepository.findOne(orderId);
        if (order.getStatus() != OrderStatus.PLACED) {
            throw new IllegalArgumentException();
        }
        // TODO decrement stock inventory
        order.setStatus(OrderStatus.APPROVED);
        orderRepository.save(order);
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

}
