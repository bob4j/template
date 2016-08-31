package com.openu.controller;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.PagingAndSortingRepository;

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
        if (e != null) {
            getRepository().save(e);
        }
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

}
