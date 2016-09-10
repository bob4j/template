package com.openu.controller;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.Predicate;

import org.springframework.data.domain.Sort.Direction;

import com.openu.model.Product;
import com.openu.model.ProductColor;
import com.openu.model.ProductSize;
import com.openu.repository.ProductRepository;
import com.openu.util.Constants;
import com.openu.util.FilterManager;
import com.openu.util.Utils;

public abstract class AbstractSearchController extends AbstractCrudController<Product> {

    @Resource
    protected ProductRepository productRepository;

    @Resource
    private EntityManagerFactory entityManagerFactory;

    Map<String, String[]> parameterMap;

    @Override
    public FilterManager<Product> getFilterManager() {
        return new FilterManager<>(Product.class, entityManagerFactory);
    }

    @Override
    protected void createPredicatesList(FilterManager<Product> filterManager) {
        super.createPredicatesList(filterManager);
        parameterMap = Utils.getRequest().getParameterMap();
        getNamePredicate(filterManager);
        getDescriptorPredicate(filterManager);
        getColorPredicate(filterManager);
        getSizePredicate(filterManager);
        gutUnKnownParamsPredicate(filterManager);
        getPriceEqualPredicate(filterManager);
        getPriceSmallerPricePredicate(filterManager);
        getPriceBiggerPricePredicate(filterManager);

    }

    private Predicate gutUnKnownParamsPredicate(FilterManager<Product> filterManager) {
        Set<String> unKnownParamSet = parameterMap.keySet();
        Predicate nameOrDescriptorPredicate = null;
        if (isUnknownParam(unKnownParamSet) && !unKnownParamSet.isEmpty()) {
            String[] unKnownParamArray = unKnownParamSet.toArray(new String[unKnownParamSet.size()]);
            Predicate unKnownNamePredicate = getNotExplicitlyNamePredicate(unKnownParamArray, filterManager);
            Predicate unKnownDescriptorPredicate = getNotExplicitlyDescriptorPredicate(unKnownParamArray, filterManager);
            nameOrDescriptorPredicate = filterManager.getCriteriaBuilder().or(unKnownDescriptorPredicate, unKnownNamePredicate);

        }
        filterManager.addPredicate(nameOrDescriptorPredicate);
        return nameOrDescriptorPredicate;
    }

    private boolean isPriceSmallerParameter(String param) {
        return param.startsWith(Constants.PRICE_IS_SMALLER);
    }

    private boolean isPriceBiggerParameter(String param) {
        return param.startsWith(Constants.PRICE_IS_BIGGER);
    }

    private boolean isUnknownParam(Set<String> unKnownParamSet) {
        for (String param : unKnownParamSet) {
            if (param.startsWith(Constants.PRICE_IS_BIGGER) || param.startsWith(Constants.PRICE_IS_SMALLER) || isNotSearchParam(param)) {
                return false;
            }
        }
        return !unKnownParamSet.contains(Constants.SIZE) && !unKnownParamSet.contains(Constants.PRICE) && !unKnownParamSet.contains(Constants.NAME)
                && !unKnownParamSet.contains(Constants.DESCRIPTOR) && !unKnownParamSet.contains(Constants.COLOR);
    }

    private boolean isNotSearchParam(String param) {
	return param.startsWith("javax.") || param.startsWith("j_id_");
    }

    private Predicate getColorPredicate(FilterManager<Product> filterManager) {
        String[] colorParams = parameterMap.get(Constants.COLOR);
        if (colorParams == null) {
            return null;
        }
        Predicate colorPredicate = filterManager.getJoinFieldPredicate(colorParams, Constants.STOCK_ITEMS, ProductColor::getColorByName,
                filterManager.getCriteriaBuilder()::equal, Constants.COLOR);
        filterManager.addPredicate(colorPredicate);
        return colorPredicate;
    }

    private Predicate getPriceSmallerPricePredicate(FilterManager<Product> filterManager) {
        String[] priceSmallerArray = getPriceArray(Constants.PRICE_IS_SMALLER, this::isPriceSmallerParameter);
        if (priceSmallerArray.length == 0) {
            return null;
        }
        Predicate priceSmallerPricePredicate = filterManager.getPrimitiveFieldPredicate(priceSmallerArray, Double::parseDouble,
                filterManager.getCriteriaBuilder()::le, Constants.PRICE);
        filterManager.addPredicate(priceSmallerPricePredicate);
        return priceSmallerPricePredicate;
    }

    private Predicate getPriceBiggerPricePredicate(FilterManager<Product> filterManager) {
        String[] priceBiggerArray = getPriceArray(Constants.PRICE_IS_BIGGER, this::isPriceBiggerParameter);
        if (priceBiggerArray.length == 0) {
            return null;
        }
        Predicate priceBiggerPricePredicate = filterManager.getPrimitiveFieldPredicate(priceBiggerArray, Double::parseDouble,
                filterManager.getCriteriaBuilder()::ge, Constants.PRICE);
        filterManager.addPredicate(priceBiggerPricePredicate);
        return priceBiggerPricePredicate;
    }

    private String[] getPriceArray(String priceOperator, Function<String, Boolean> checkOperatorFunction) {
        Set<String> unKnownParamSet = parameterMap.keySet();
        ArrayList<String> priceParams = new ArrayList<String>();
        for (String param : unKnownParamSet) {
            if (checkOperatorFunction.apply(param)) {
                priceParams.add(param.replaceFirst(priceOperator, ""));
            }
        }
        String[] priceSmallerArray = priceParams.toArray(new String[priceParams.size()]);
        return priceSmallerArray;
    }

    private Predicate getSizePredicate(FilterManager<Product> filterManager) {
        String[] sizeParams = parameterMap.get(Constants.SIZE);

        if (sizeParams == null) {
            return null;
        }
        Predicate sizePredicate = filterManager.getJoinFieldPredicate(sizeParams, Constants.STOCK_ITEMS, ProductSize::getSizeByLable,
                filterManager.getCriteriaBuilder()::equal, Constants.SIZE);
        filterManager.addPredicate(sizePredicate);
        return sizePredicate;
    }

    private Predicate getDescriptorPredicate(FilterManager<Product> filterManager) {
        String[] descriptorParams = parameterMap.get(Constants.DESCRIPTOR);
        Predicate descriptorPredicate = filterManager.getStringFieldPredicate(descriptorParams, Constants.LIKE_TEMPLATE, Constants.DESCRIPTOR);
        filterManager.addPredicate(descriptorPredicate);
        return descriptorPredicate;
    }

    /**
     *
     * @param parameterArray
     * @param filterManager
     * @return descriptorPredicate - this function not add the predicate to filterManager
     */
    private Predicate getNotExplicitlyDescriptorPredicate(String[] parameterArray, FilterManager<Product> filterManager) {
        Predicate descriptorPredicate = filterManager.getStringFieldPredicate(parameterArray, Constants.LIKE_TEMPLATE, Constants.DESCRIPTOR);
        return descriptorPredicate;

    }

    private Predicate getNamePredicate(FilterManager<Product> filterManager) {
        String[] nameParmameters = parameterMap.get(Constants.NAME);
        Predicate namePredicate = filterManager.getStringFieldPredicate(nameParmameters, Constants.LIKE_TEMPLATE, Constants.NAME);
        filterManager.addPredicate(namePredicate);
        return namePredicate;

    }

    /**
     *
     * @param parameterArray
     * @param filterManager
     * @return descriptorPredicate - this function not add the predicate to filterManager
     */
    private Predicate getNotExplicitlyNamePredicate(String[] parameterArray, FilterManager<Product> filterManager) {
        Predicate namePredicate = filterManager.getStringFieldPredicate(parameterArray, Constants.LIKE_TEMPLATE, Constants.NAME);
        return namePredicate;

    }

    private Predicate getPriceEqualPredicate(FilterManager<Product> filterManager) {
        String[] priceParmameters = parameterMap.get(Constants.PRICE);
        Predicate priceEqualPredicate = filterManager.getPrimitiveFieldPredicate(priceParmameters, Double::parseDouble,
                filterManager.getCriteriaBuilder()::equal, Constants.PRICE);
        filterManager.addPredicate(priceEqualPredicate);
        return priceEqualPredicate;
    }

 
}
