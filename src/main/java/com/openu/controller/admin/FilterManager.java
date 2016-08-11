package com.openu.controller.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Metamodel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.openu.model.Product;
import com.openu.model.ProductColor;
import com.openu.model.StockItem;

public class FilterManager<T> {
    private static final String VALUE = "value";
    private Logger logger = LoggerFactory.getLogger(getClass());

    private Root<T> root;
    private CriteriaBuilder criteriaBuilder;
    private CriteriaQuery<T> criteriaQuery;
    private List<Predicate> predicates = new ArrayList<Predicate>();
    private EntityManager em;

    public FilterManager(Class<T> objectClass, EntityManagerFactory entityManagerFactory) {
	setEm(entityManagerFactory.createEntityManager());
	setCriteriaBuilder(getEm().getCriteriaBuilder());
	setCriteriaQuery(getCriteriaBuilder().createQuery(objectClass));
	setRoot(getCriteriaQuery().from(objectClass));
    }

    public Metamodel getMetamodel() {
	return em.getMetamodel();
    }

    public Iterable<T> getQueryResultList() {
	getCriteriaQuery().where(getPredicatesList().toArray(new Predicate[] {}));
	TypedQuery<T> query = getEm().createQuery(getCriteriaQuery());
	return query.getResultList();
    }

    public List<Predicate> getPredicatesList() {
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
	    addPredicate(predicate);
	}
    }
    
    public void addPredicate(Predicate predicate){
	if (predicate != null){
	    getPredicatesList().add(predicate);
	}
    }
    

    public  <R, Q> Predicate getJoinFieldPredicate(String[] params, String joinByField, Function<String, R> getParamByNameFunction,
	    BiFunction<Path<Q>, R, Predicate> criteriaBuilderFunction, String fieldName) {
	if (params == null || params.length == 0 ) {
	    return null;
	}
	Predicate fieldPredicate = null;
	Join<T, Q> join = root.join(joinByField);
	R objectValue = getParamByNameFunction.apply(params[0]);
	if (objectValue != null) {
	    //create the first predicate
	    fieldPredicate = criteriaBuilderFunction.apply(join.<Q>get(fieldName), objectValue);
	    for (int i = 1; i < params.length; i++) {
		//aggregated all the relevant predicate with OR operator
		objectValue = getParamByNameFunction.apply(params[i]);
		if (objectValue != null) {
		    fieldPredicate = criteriaBuilder.or(fieldPredicate,
			    criteriaBuilderFunction.apply(join.<Q>get(fieldName), objectValue));
		}
	    }
	}
	return fieldPredicate;
    }
    
    public  <R, Q> Predicate getStringFieldPredicate(String[] params,String likeTemple, String fieldName) {
	if (params == null || params.length == 0 ) {
	    return null;
	}
	StringBuilder paramAsString = new StringBuilder(likeTemple);
	int indexOfValue = paramAsString.indexOf(VALUE);
	paramAsString.replace(indexOfValue, indexOfValue+VALUE.length(), params[0]);
	Predicate fieldPredicate = null;
	if (paramAsString != null) {
	    fieldPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.<String>get(fieldName)), paramAsString.toString());
	    for (int i = 1; i < params.length; i++) {
		paramAsString.replace(indexOfValue, indexOfValue+VALUE.length(), params[i]);
		    fieldPredicate = criteriaBuilder.or(fieldPredicate,
			    criteriaBuilder.like(criteriaBuilder.lower(root.<String>get(fieldName)), paramAsString.toString()));
	    }
	}
	return fieldPredicate;
    }
    
    public  <R> Predicate getPrimitiveFieldPredicate(String[] params,Function<String, R> getParamByNameFunction,
	    BiFunction<Path<R>, R, Predicate> criteriaBuilderFunction, String fieldName) {
	if (params == null || params.length == 0) {
	    return null;
	}
	try {
	    Predicate fieldPredicate = null;
	    R objectValue = getParamByNameFunction.apply(params[0]);
	    if (objectValue != null) {
		// create the first predicate
		fieldPredicate = criteriaBuilderFunction.apply(root.<R>get(fieldName), objectValue);
		for (int i = 1; i < params.length; i++) {
		    // aggregated all the relevant predicate with OR operator
		    objectValue = getParamByNameFunction.apply(params[i]);
		    if (objectValue != null) {
			fieldPredicate = criteriaBuilder.or(fieldPredicate,
				criteriaBuilderFunction.apply(root.<R>get(fieldName), objectValue));
		    }
		}
	    }
	    return fieldPredicate;
	} catch (NumberFormatException e) {
	    logger.error("cannot create Predicate for" +fieldName + "because of parse problem");
	    return null;
	}
    }
    
    

}
