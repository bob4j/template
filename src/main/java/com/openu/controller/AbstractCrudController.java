package com.openu.controller;

import org.springframework.data.repository.PagingAndSortingRepository;

public abstract class AbstractCrudController<T> {

    protected T entity;

    protected abstract PagingAndSortingRepository<T, Long> getRepository();

    public void load(T entity) {
        this.entity = entity;
    }

    public Iterable<T> getAll() {
        return getRepository().findAll();
    }

    public void delete(T entity) {
        getRepository().delete(entity);
    }

    protected abstract T createEntity() throws Exception;

    public void save() throws Exception {
        T e = createEntity();
        getRepository().save(e);
    }

    public T getEntity() {
        return entity;
    }
}
