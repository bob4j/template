package com.openu.controller;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.openu.model.Month;
import com.openu.util.FilterManager;

public abstract class AbstractCrudController<T> {

    protected T entity;

    // initialize sort parameters
    private String sortBy = Constants.ID;
    private Direction direction = Direction.ASC;

    protected abstract PagingAndSortingRepository<T, Long> getRepository();

    /**
     * Loads the entity in the controller session context in order for it to be presented on {entity}-details page
     */
    public void load(T entity) {
        this.entity = entity;
    }

    /**
     * Retrieves all entities to be presented on the {entity}-list page
     */

    public Iterable<T> getAll() {
        FilterManager<T> filterManager = getFilterManager();
        if (filterManager != null) {
            createPredicatesList(filterManager);
            if (!filterManager.getPredicatesList().isEmpty()) {
                try {
                    return filterManager.getQueryResultList(getDirection(), getSortBy());
                } finally {
                    filterManager.getEm().close();
                }
            }
        }
        return getRepository().findAll(new Sort(getDirection(), getSortBy()));
    }

    protected void createPredicatesList(FilterManager<T> filterManager) {
    }

    protected FilterManager<T> getFilterManager() {
        return null;
    }

    /**
     * Deletes the specific entity
     */
    public void delete(T entity) {
        getRepository().delete(entity);
    }

    /**
     * Persists the entity
     */
    public void save() throws Exception {
        T e = createEntity();
        getRepository().save(e);
    }

    /**
     * Populates the entity which will be persisted
     */
    protected abstract T createEntity() throws Exception;

    /**
     * Returns the entity to be presented on the {entity}-details page
     */
    public T getEntity() {
        return entity;
    }

    public void idUp() {
        setSortBy(Constants.ID);
        setDirection(Direction.ASC);
    }

    public void idDown() {
        setSortBy(Constants.ID);
        setDirection(Direction.DESC);
    }

    public void NameUp() {
        setSortBy(Constants.NAME);
        setDirection(Direction.ASC);
    }

    public void nameDown() {
        setSortBy(Constants.NAME);
        setDirection(Direction.DESC);
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    protected Predicate getDatePredicate(CriteriaBuilder criteriaBuilder, Root<T> root, int selectedYear, Month selectedMonth, int selectedDay,
            String fieldToFilter) {
        if (selectedYear != Constants.NO_YEAR_SELECTED) {
            if (selectedMonth != Month.All) {
                if (selectedDay != Constants.NO_DAY_SELECTED) {
                    return FilterYearMonthDay(criteriaBuilder, root, selectedYear, selectedMonth, selectedDay, fieldToFilter);
                } else {
                    return FilterYearMonth(criteriaBuilder, root, selectedYear, selectedMonth, fieldToFilter);
                }
            } else {
                return filterYear(criteriaBuilder, root, selectedYear, fieldToFilter);

            }
        }
        return null;
    }

    private Predicate FilterYearMonth(CriteriaBuilder criteriaBuilder, Root<T> root, int selectedYear, Month selectedMonth, String fieldToFilter) {
        GregorianCalendar minDayInSelectedDate = new GregorianCalendar(selectedYear, selectedMonth.ordinal(), 1, 23, 59, 59);
        GregorianCalendar maxDayInSelectedDate = new GregorianCalendar(selectedYear, selectedMonth.ordinal(),
                minDayInSelectedDate.getActualMaximum(Calendar.DAY_OF_MONTH), 0, 0, 0);
        return criteriaBuilder
                .between(root.<Long> get(fieldToFilter), minDayInSelectedDate.getTimeInMillis(), maxDayInSelectedDate.getTimeInMillis());
    }

    private Predicate filterYear(CriteriaBuilder criteriaBuilder, Root<T> root, int selectedYear, String fieldToFilter) {
        GregorianCalendar maxMonthInSelectedDate = new GregorianCalendar(selectedYear, Month.December.ordinal(), 1, 23, 59, 59);
        maxMonthInSelectedDate
        .set(selectedYear, Month.December.ordinal(), maxMonthInSelectedDate.getActualMaximum(Calendar.DAY_OF_MONTH), 23, 59, 59);
        GregorianCalendar minMonthInSelectedDate = new GregorianCalendar(selectedYear, Month.January.ordinal(), 1, 0, 0, 0);
        return criteriaBuilder.between(root.<Long> get(fieldToFilter), minMonthInSelectedDate.getTimeInMillis(),
                maxMonthInSelectedDate.getTimeInMillis());
    }

    private Predicate FilterYearMonthDay(CriteriaBuilder criteriaBuilder, Root<T> root, int selectedYear, Month selectedMonth, int selectedDay,
            String fieldToFilter) {
        GregorianCalendar maxHourInSelectedDate = new GregorianCalendar(selectedYear, selectedMonth.ordinal(), selectedDay, 23, 59, 59);
        GregorianCalendar minHourInSelectedDate = new GregorianCalendar(selectedYear, selectedMonth.ordinal(), selectedDay, 0, 0, 0);
        return criteriaBuilder.between(root.<Long> get(fieldToFilter), minHourInSelectedDate.getTimeInMillis(),
                maxHourInSelectedDate.getTimeInMillis());
    }

}
