package com.openu.controller;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.PagingAndSortingRepository;

public abstract class AbstractCrudController<T> {

    private static final String ID = "id";
    protected T entity;
    private String sortBy = ID;
    private Direction direction = Direction.ASC;

    protected abstract PagingAndSortingRepository<T, Long> getRepository();

    /**
     * Loads the entity in the controller session context in order for it to be
     * presented on {entity}-details page
     */
    public void load(T entity) {
	this.entity = entity;
    }

    /**
     * Retrieves all entities to be presented on the {entity}-list page
     */
    public Iterable<T> getAll() {
	return getRepository().findAll(new Sort(getDirection(), getSortBy()));
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

    public void setIdUp() {
	setSortBy(ID);
	setDirection(Direction.ASC);
    }

    public void setIdDown() {
	setSortBy(ID);
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
