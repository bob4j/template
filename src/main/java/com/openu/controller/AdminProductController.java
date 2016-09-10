package com.openu.controller;

import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.Part;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.openu.model.Category;
import com.openu.model.Image;
import com.openu.model.Product;
import com.openu.model.ProductColor;
import com.openu.model.ProductSize;
import com.openu.model.StockItem;
import com.openu.repository.CategoryRepository;
import com.openu.util.Constants;
import com.openu.util.Utils;

/**
 *
 * This Class is Abstract class for controlling the search product functionality
 */
@Component
@Scope("view")
public class AdminProductController extends AbstractSearchController implements Serializable {


    private static final long serialVersionUID = 1L;

    private String brand;
    private String model;
    private String description;
    private Double price;
    private Part imagePart;

    @Resource
    private CategoryRepository categoryRepository;

    @Value("${static.content.dir}")
    private String staticContentDir;

    @Value("${static.content.dir.web}")
    private String staticContentDirWeb;

    private List<Long> categoryIds = new ArrayList<>();
    public List<StockItem> stockItems = new ArrayList<>();

    /**
     * this function will be called before page loading.
     *
     */
    @PostConstruct
    public void init() {
        staticContentDir = staticContentDir.replace("~", System.getProperty("user.home"));
        String productId = Utils.getRequest().getParameter("product_id");
        if (productId != null) {
            entity = productRepository.findOne(Long.valueOf(productId));
            categoryIds = entity.getCategories().stream().map(c -> c.getId()).collect(Collectors.toList());
            stockItems = new ArrayList<>(entity.getStockItems());
        } else {
            stockItems.add(new StockItem());
        }
    }

    public void addStockItem(AjaxBehaviorEvent event) {
        stockItems.add(new StockItem());
    }
    
    public void removeLastStockItem(AjaxBehaviorEvent event) {
	if (stockItems.size() > 1){
	    stockItems.remove(stockItems.size()-1);
	}
    }

    public List<ProductColor> getColors() {
        return Stream.of(ProductColor.values()).collect(Collectors.toList());
    }

    public List<ProductSize> getSizes() {
        return Stream.of(ProductSize.values()).collect(Collectors.toList());
    }

    @Override
    protected PagingAndSortingRepository<Product, Long> getRepository() {
        return productRepository;
    }

    /**
     * Override {@link AbstractCrudController#createEntity()} create Product entity for admin Product
     */
    @Override
    protected Product createEntity() throws Exception {
        Product p = new Product();
        p.setBrand(brand);
        p.setName(model);
        p.setDescription(description);
        p.setPrice(price);
        String filename = System.currentTimeMillis() + getImageSuffix();
        Files.copy(imagePart.getInputStream(), Paths.get(staticContentDir, filename));
        p.setImage(new Image(staticContentDirWeb + "/" + filename));
        p.setCategories(categoryIds.stream().map(id -> categoryRepository.findOne(id)).collect(Collectors.toList()));
        stockItems.forEach(si -> si.setProduct(p));
        p.setStockItems(getStockItemsWithoutDuplicates(stockItems));
        return p;
    }

    private List<StockItem> getStockItemsWithoutDuplicates(List<StockItem> sis) {
        List<StockItem> items = new ArrayList<>();
        for (StockItem si : sis) {
            StockItem existing = items.stream().filter(item -> item.getColor() == si.getColor() && item.getSize() == si.getSize()).findFirst()
                    .orElse(null);
            if (existing != null) {
                existing.setId(Optional.ofNullable(si.getId()).orElse(existing.getId()));
                existing.setQuantity(existing.getQuantity() + si.getQuantity());
            } else {
                items.add(si);
            }
        }
        return items;
    }

    public String update() throws Exception {
        if (!StringUtils.isEmpty(imagePart.getSubmittedFileName())) {
            String filename = System.currentTimeMillis() + getImageSuffix();
            Files.copy(imagePart.getInputStream(), Paths.get(staticContentDir, filename));
            entity.setImage(new Image(staticContentDirWeb + "/" + filename));
        }
        entity.getCategories().clear();
        entity.setCategories(categoryIds.stream().map(id -> categoryRepository.findOne(id)).collect(Collectors.toList()));
        entity.getStockItems().clear();
        stockItems.forEach(si -> si.setProduct(entity));
        entity.setStockItems(getStockItemsWithoutDuplicates(stockItems));
        productRepository.save(entity);
        return "/admin/products?faces-redirect=true";
    }

    public Iterable<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    private String getImageSuffix() {
        return imagePart.getSubmittedFileName().substring(imagePart.getSubmittedFileName().lastIndexOf("."),
                imagePart.getSubmittedFileName().length());
    }

    public void setImagePart(Part image) {
        imagePart = image;
    }

    public Part getImagePart() {
        return imagePart;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public List<Long> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<Long> categoryIds) {
        this.categoryIds = categoryIds;
    }

    public List<StockItem> getStockItems() {
        return stockItems;
    }

    public void setStockItems(List<StockItem> stockItems) {
        this.stockItems = stockItems;
    }

    public void brandUp() {
        setSortBy(Constants.BRAND);
        setDirection(Direction.ASC);
    }

    public void brandDown() {
        setSortBy(Constants.BRAND);
        setDirection(Direction.DESC);
    }
    
   
}
