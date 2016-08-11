package com.openu.controller.admin;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.openu.controller.AbstractCrudController;
import com.openu.controller.Constants;
import com.openu.model.Month;
import com.openu.model.Order;
import com.openu.model.OrderStatus;
import com.openu.repository.OrderRepository;
import com.openu.util.Utils;
 

@Component
@Scope("view")
public class AdminOrderController extends AbstractCrudController<Order> {

    
    
    @Resource
    private OrderRepository orderRepository;
    @Resource
    private EntityManagerFactory entityManagerFactory;
     
    private OrderStatus selectedStatus = OrderStatus.NOT_SLECTED;
    private Month selectedCreationMonth = Month.All;
    private int selectedCreationYear = Constants.NO_YEAR_SELECTED;
    private int selectedCreationDay = Constants.NO_DAY_SELECTED ;
    private Month selectedModifiedMonth = Month.All;
    private int selectedModifiedYear = Constants.NO_YEAR_SELECTED;
    private int selectedModifiedDay = Constants.NO_DAY_SELECTED ;
    private Date date;
    private ArrayList<String> statusesList;
    private String selectedCustomer = "";
    public Order getOrder() {
        String orderId = Utils.getRequest().getParameter("order_id");
        if (orderId != null) {
            return orderRepository.findOne(Long.valueOf(orderId));
        }
        return null;
    }
    public void onDateSelect(SelectEvent event) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Date Selected", format.format(event.getObject())));
    }
     
    public void click() {
        RequestContext requestContext = RequestContext.getCurrentInstance();
         
        requestContext.update("form:display");
        requestContext.execute("PF('dlg').show()");
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
    public Iterable<Order> getAll() {
        
        FilterManager<Order> orderFilter = getFilterManager();
        createPredicatesList(orderFilter);
        if (orderFilter.getPredicatesList().isEmpty()){
            return super.getAll();
        }
        return orderFilter.getQueryResultList();
    }
    
    @Override
    protected FilterManager<Order> getFilterManager() {
	FilterManager<Order> orderFilter = new FilterManager<Order>(Order.class,entityManagerFactory);
	return orderFilter;
    }
   
    @Override
    protected void createPredicatesList(FilterManager<Order> orderFilter) {
	CriteriaBuilder criteriaBuilder = orderFilter.getCriteriaBuilder();
	Root<Order> root = orderFilter.getRoot();
	Predicate statusPredicate = getStatusPredicate(criteriaBuilder, root);
	Predicate customerPredicate = getCustomerPredicate(orderFilter);
	Predicate creationDatePredicate = getCreationDatePredicate(criteriaBuilder, root);
	Predicate modifiedDatePredicate = getModifiedDatePredicate(criteriaBuilder, root);

	orderFilter.addPredicates( statusPredicate, creationDatePredicate,customerPredicate, modifiedDatePredicate);
    }

    private Predicate getStatusPredicate(CriteriaBuilder criteriaBuilder, Root<Order> root) {
	if (selectedStatus != OrderStatus.NOT_SLECTED) {
	    return criteriaBuilder.equal(root.<OrderStatus>get(Constants.STATUS), selectedStatus);
	} else {
	    return null;
	}
    }
    
    private Predicate getCustomerPredicate( FilterManager<Order> orderFilter) {
	//TODO add predicate
	if (!getSelectedCustomer().isEmpty() ) {
	}
	    return null;
	
    }
    
    private Predicate getCreationDatePredicate(CriteriaBuilder criteriaBuilder, Root<Order> root) {
	return getDatePredicate(criteriaBuilder, root, selectedCreationYear, selectedCreationMonth, selectedCreationDay, Constants.CREATED);
    }
    
    private Predicate getModifiedDatePredicate(CriteriaBuilder criteriaBuilder, Root<Order> root) {
	return getDatePredicate(criteriaBuilder, root, selectedModifiedYear, selectedModifiedMonth, selectedModifiedDay,Constants.MODIFIED);
    }
    
    public List<String> getStatusesList(){
	statusesList = new ArrayList<String>();
	for( OrderStatus status :OrderStatus.values() ){
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

    public Month getSelectedCreationMonth() {
	return selectedCreationMonth;
    }

    public void setSelectedCreationMonth(Month selectedCreationMonth) {
	this.selectedCreationMonth = selectedCreationMonth;
    }
    public Month[] getMonths(){
   	return Month.values();
    }
    
    /**
     * 
     * @return list of the days in the given month and year
     */
    public  List<Integer> getDaysInMonth(int year, Month month ){
	ArrayList<Integer> days = new ArrayList<Integer>();
	if (!month.equals(Month.All) && !(year == Constants.NO_YEAR_SELECTED)){
	    GregorianCalendar calendar = new GregorianCalendar(year,month.ordinal(),1);
	    int actualMaximumDaysInMonth = calendar.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
	    for (int i =1; i<= actualMaximumDaysInMonth; i++){
		days.add(i);
	    }
	}
	return days;
    }
    
    /**
     * 
     * @param fielsToFilter 
     * @return list of  years 
     */
    public List<Integer> getYearsList(String fielsToFilter) {
	ArrayList<Integer> years = new ArrayList<Integer>();
	Iterable<Order> findAll = getRepository().findAll(new Sort(Direction.ASC, fielsToFilter));
	Iterator<Order> iterator = findAll.iterator();
	int minYear = getMinYear(iterator, fielsToFilter);
	int maxYear=getMaxYear(iterator, fielsToFilter);
	if (minYear != 0 && maxYear != 0) {
	    for (int i = minYear; i <= maxYear; i++) {
		years.add(i);
	    }
	}
	return years;
    }
    
    
    public List<Integer> getCreatedYearsList() {
	List<Integer> years = getYearsList(Constants.CREATED);
	if (years.size() == 1){
	    setSelectedCreationYear(years.get(0));
	}
	return years;
    }
    
    public List<Integer> getModifiedYearsList() {
	List<Integer> years =  getYearsList(Constants.MODIFIED);
	if (years.size() == 1){
	    setSelectedModifiedYear(years.get(0));
	}
	return years;

    }
    private int getMinYear(Iterator<Order> iterator, String fielsToFilter) {
	Long minDate = Long.MIN_VALUE;
	if (iterator.hasNext()) {
	    minDate = getDate(iterator,fielsToFilter);
	}
	Calendar cal = Calendar.getInstance();
	cal.setTimeInMillis(minDate);
	return minDate != Long.MIN_VALUE ? cal.get(Calendar.YEAR) : 0;
    }
    private int getMaxYear(Iterator<Order> iterator, String fielsToFilter) {
	Long MaxDate = Long.MIN_VALUE;
	while (iterator.hasNext() ) {
	    MaxDate = getDate(iterator ,fielsToFilter);
	 }
	Calendar cal = Calendar.getInstance();
	cal.setTimeInMillis( MaxDate);
	return MaxDate != Long.MIN_VALUE ? cal.get(Calendar.YEAR) : 0;
    }
    private Long getDate(Iterator<Order> iterator, String fielsToFilter) {
	Long date;
	switch (fielsToFilter) {
	case Constants.MODIFIED:
		date = iterator.next().getModified();
	    break;
	case Constants.CREATED:
		date = iterator.next().getCreated();
	    break;
	default:
		date = Long.MIN_VALUE;
	    break;
	}
	return date;
    }

    public  List<Integer> getDaysInMonthForSelectedMonthCreation(){
	return getDaysInMonth(getSelectedCreationYear(), getSelectedCreationMonth() );
    }
    
    public  List<Integer> getDaysInMonthForSelectedModifiedCreation(){
	return getDaysInMonth(getSelectedModifiedYear(), getSelectedModifiedMonth() );
    }
    
    public int getSelectedCreationYear() {
	return selectedCreationYear;
    }

    public void setSelectedCreationYear(int selectedCreationYear) {
	    this.selectedCreationYear = selectedCreationYear;
    }

    public int getSelectedCreationDay() {
	return selectedCreationDay;
    }

    public void setSelectedCreationDay(int selectedCreationDay) {
	this.selectedCreationDay = selectedCreationDay;
    }

    public Date getDate() {
	return date;
    }

    public void setDate(Date date) {
	this.date = date;
    }
    public Month getSelectedModifiedMonth() {
	return selectedModifiedMonth;
    }
    public void setSelectedModifiedMonth(Month selectedModifiedMonth) {
	this.selectedModifiedMonth = selectedModifiedMonth;
    }
    public int getSelectedModifiedYear() {
	return selectedModifiedYear;
    }
    public void setSelectedModifiedYear(int selectedModifiedYear) {
	this.selectedModifiedYear = selectedModifiedYear;
    }
    public int getSelectedModifiedDay() {
	return selectedModifiedDay;
    }
    public void setSelectedModifiedDay(int selectedModifiedDay) {
	this.selectedModifiedDay = selectedModifiedDay;
    }
    public String getSelectedCustomer() {
	return selectedCustomer;
    }
    public void setSelectedCustomer(String selectedCustomer) {
	this.selectedCustomer = selectedCustomer;
    }
    
    public void clearFilter(){
	selectedCreationDay = Constants.NO_DAY_SELECTED;
	selectedCreationMonth = Month.All;
	selectedCreationYear = Constants.NO_YEAR_SELECTED;
	selectedModifiedDay = Constants.NO_DAY_SELECTED;
	selectedModifiedMonth = Month.All;
	selectedModifiedYear = Constants.NO_YEAR_SELECTED;
	selectedCustomer = "";
	selectedStatus = OrderStatus.NOT_SLECTED;
		
    }
    

}
