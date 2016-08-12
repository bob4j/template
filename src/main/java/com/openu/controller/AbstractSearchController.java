package com.openu.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.Predicate;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.openu.model.Product;
import com.openu.model.ProductColor;
import com.openu.model.ProductSize;
import com.openu.repository.ProductRepository;
import com.openu.util.FilterManager;
import com.openu.util.Utils;


public abstract class AbstractSearchController extends AbstractCrudController<Product>  {

    private static final String PRICE_IS_BIGGER = "price>";

    private static final String PRICE_IS_SMALLER = "price<";
    
    private static final String PRICE = "price";

    private static final String NAME = "name";

    private static final String LIKE_TEMPLATE = "%value%";

    private static final String DESCRIPTOR = "description";

    private static final String SIZE = "size";

    private static final String COLOR = "color";

    private static final String STOCK_ITEMS = "stockItems";

    @Resource
    protected ProductRepository productRepository;
       
    @Resource
    private EntityManagerFactory entityManagerFactory;
    
    FilterManager<Product> filterManager;

    Map<String, String[]> parameterMap ;
    
    @Override
    public FilterManager<Product> getFilterManager() {
	return filterManager;
    }
     
    @Override
    public Iterable<Product> getAll() {
	filterManager = new FilterManager<>(Product.class, entityManagerFactory);
	return super.getAll();
    }
    
    @Override
    protected void createPredicatesList(){
	super.createPredicatesList();
	parameterMap  = Utils.getRequest().getParameterMap(); 
	getNamePredicate();
	getDescriptorPredicate();
	getColorPredicate();
	getSizePredicate();
	gutUnKnownParamsPredicate();
	getPriceEqualPredicate();
	getPriceSmallerPricePredicate();
	getPriceBiggerPricePredicate();

    }


    private Predicate gutUnKnownParamsPredicate() {
        Set<String> unKnownParamSet = parameterMap.keySet();
        Predicate nameOrDescriptorPredicate = null;
	if (isUnknownParam(unKnownParamSet) && !unKnownParamSet.isEmpty()) {
	    String[] unKnownParamArray = unKnownParamSet.toArray(new String [unKnownParamSet.size()] );
	    Predicate unKnownNamePredicate = getNotExplicitlyNamePredicate(unKnownParamArray);
	    Predicate unKnownDescriptorPredicate = getNotExplicitlyDescriptorPredicate(unKnownParamArray);
	     nameOrDescriptorPredicate = filterManager.getCriteriaBuilder().or(unKnownDescriptorPredicate, unKnownNamePredicate);

	}
	filterManager.addPredicate(nameOrDescriptorPredicate);
        return nameOrDescriptorPredicate;
    }

    private boolean isPriceSmallerParameter(String param) {
	return param.startsWith(PRICE_IS_SMALLER) ;
    }
    
    private boolean isPriceBiggerParameter(String param) {
	return param.startsWith(PRICE_IS_BIGGER) ;
    }


    private boolean isUnknownParam(Set<String> unKnownParamSet) {
	for (String param : unKnownParamSet) {
	    if (param.startsWith(PRICE_IS_BIGGER)||param.startsWith(PRICE_IS_SMALLER)){
		return false;
	    }
	}
	return  !unKnownParamSet.contains(SIZE) && !unKnownParamSet.contains(PRICE)&&!unKnownParamSet.contains(NAME) 
			&& !unKnownParamSet.contains(DESCRIPTOR) && !unKnownParamSet.contains(COLOR);
    }


    private Predicate getColorPredicate() {
        String[] colorParams = parameterMap.get(COLOR);
	if (colorParams == null){
	    return null;
	}
	Predicate colorPredicate = filterManager.getJoinFieldPredicate(colorParams,STOCK_ITEMS,ProductColor::getColorByName,filterManager.getCriteriaBuilder()::equal,COLOR);
         filterManager.addPredicate(colorPredicate);
         return colorPredicate;
    }
    
    private Predicate getPriceSmallerPricePredicate(){
        String[] priceSmallerArray = getPriceArray(PRICE_IS_SMALLER, this::isPriceSmallerParameter);
        if (priceSmallerArray.length == 0){
            return null;
        }
        Predicate priceSmallerPricePredicate = filterManager.getPrimitiveFieldPredicate(priceSmallerArray, Double::parseDouble, filterManager.getCriteriaBuilder()::le, PRICE);
        filterManager.addPredicate(priceSmallerPricePredicate);
        return priceSmallerPricePredicate;
    }
    
    private Predicate getPriceBiggerPricePredicate(){
        String[] priceBiggerArray = getPriceArray(PRICE_IS_BIGGER, this::isPriceBiggerParameter);
        if (priceBiggerArray.length == 0){
            return null;
        }
        Predicate priceBiggerPricePredicate = filterManager.getPrimitiveFieldPredicate(priceBiggerArray, Double::parseDouble, filterManager.getCriteriaBuilder()::ge, PRICE);
        filterManager.addPredicate(priceBiggerPricePredicate);
        return priceBiggerPricePredicate;
    }


    private String[] getPriceArray(String priceOperator, Function<String, Boolean> checkOperatorFunction) {
	Set<String> unKnownParamSet = parameterMap.keySet();
        ArrayList<String> priceParams= new ArrayList<String>();
	for (String param : unKnownParamSet) {
		if (checkOperatorFunction.apply(param)){
		    priceParams.add(param.replaceFirst(priceOperator, ""));
		}
	}
        String[] priceSmallerArray = priceParams.toArray(new String [priceParams.size()]);
	return priceSmallerArray;
    }
    
    private Predicate getSizePredicate() {
        String[] sizeParams = parameterMap.get(SIZE);

	if (sizeParams == null){
	    return null;
	}
	Predicate sizePredicate = filterManager.getJoinFieldPredicate(sizeParams,STOCK_ITEMS,ProductSize::getSizeByLable,filterManager.getCriteriaBuilder()::equal,SIZE);
         filterManager.addPredicate(sizePredicate);
         return sizePredicate;
    }

    
    
    private Predicate getDescriptorPredicate(){
        String[] descriptorParams = parameterMap.get(DESCRIPTOR);
        Predicate descriptorPredicate =  filterManager.getStringFieldPredicate(descriptorParams, LIKE_TEMPLATE, DESCRIPTOR);
        filterManager.addPredicate(descriptorPredicate);
        return descriptorPredicate;
    }
    /**
     * 
     * @param parameterArray
     * @param filterManager
     * @return descriptorPredicate - this function not add the predicate to filterManager
     */
    private Predicate getNotExplicitlyDescriptorPredicate(String[] parameterArray){
        Predicate descriptorPredicate =  filterManager.getStringFieldPredicate(parameterArray, LIKE_TEMPLATE, DESCRIPTOR);
        return descriptorPredicate;

    }
    
    private Predicate getNamePredicate(){
        String[] nameParmameters = parameterMap.get(NAME);
        Predicate namePredicate = filterManager.getStringFieldPredicate(nameParmameters, LIKE_TEMPLATE, NAME);
        filterManager.addPredicate(namePredicate);
        return namePredicate;

    }
    
    /**
     * 
     * @param parameterArray
     * @param filterManager
     * @return descriptorPredicate - this function not add the predicate to filterManager
     */
    private Predicate getNotExplicitlyNamePredicate(String[] parameterArray){
        Predicate namePredicate = filterManager.getStringFieldPredicate(parameterArray, LIKE_TEMPLATE, NAME);
        return namePredicate;

    }
    
    private Predicate getPriceEqualPredicate(){
        String[] priceParmameters = parameterMap.get(PRICE);
        Predicate priceEqualPredicate = filterManager.getPrimitiveFieldPredicate(priceParmameters, Double::parseDouble, filterManager.getCriteriaBuilder()::equal, PRICE);
        filterManager.addPredicate(priceEqualPredicate);
        return priceEqualPredicate;
    }
    
    
    
    


    
}
