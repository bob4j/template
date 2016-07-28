package com.openu.controller.admin;


import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class FilterUtils<T> {
    private Root<T> root;
    private CriteriaBuilder criteriaBuilder;
    private CriteriaQuery<T> criteriaQuery;
    private List<Predicate> predicates = new ArrayList<Predicate>();
    private EntityManager em;
    public  FilterUtils( Class<T> objectClass,EntityManagerFactory entityManagerFactory){
	setEm(entityManagerFactory.createEntityManager());
        setCriteriaBuilder(getEm().getCriteriaBuilder());
        setCriteriaQuery(getCriteriaBuilder().createQuery(objectClass));
        setRoot(getCriteriaQuery().from(objectClass));
    }
    
    public Iterable<T> getQueryResultList() {
	getCriteriaQuery().where(getPredicates().toArray(new Predicate[] {}));
	TypedQuery<T> query = getEm().createQuery(this.getCriteriaQuery());
	return query.getResultList();
    }
    
    public List<Predicate> getPredicates() {
	return predicates;
    }
    public void setPredicates(List<Predicate> predicates) {
	this.predicates = predicates;
    }
    public EntityManager getEm() {
	return em;
    }
    public void setEm(EntityManager em) {
	this.em = em;
    }

    
    public Root<T> getRoot() {
	return root;
    }
    public void setRoot(Root<T> root) {
	this.root = root;
    }
    public CriteriaBuilder getCriteriaBuilder() {
	return criteriaBuilder;
    }
    public void setCriteriaBuilder(CriteriaBuilder criteriaBuilder) {
	this.criteriaBuilder = criteriaBuilder;
    }
    public CriteriaQuery<T> getCriteriaQuery() {
	return criteriaQuery;
    }
    public void setCriteriaQuery(CriteriaQuery<T> criteriaQuery) {
	this.criteriaQuery = criteriaQuery;
    }
    
    public void addPredicates(Predicate... PredicatesArr) {
	for (Predicate predicate : PredicatesArr) {
	    if (predicate != null) {
		getPredicates().add(predicate);
	    }
	}
    }
}
