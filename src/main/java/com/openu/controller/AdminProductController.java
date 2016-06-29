package com.openu.controller;

import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.Part;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;

import com.openu.model.Category;
import com.openu.model.Image;
import com.openu.model.Product;
import com.openu.model.ProductColor;
import com.openu.model.ProductSize;
import com.openu.model.StockItem;
import com.openu.repository.CategoryRepository;
import com.openu.repository.ProductRepository;

@Component
@Scope("view")
public class AdminProductController extends AbstractCrudController<Product> implements Serializable {

    private static final long serialVersionUID = 1L;

    private String brand;
    private String model;
    private String description;
    private Double price;
    private Part imagePart;

    private List<Long> categoryIds = new ArrayList<>();

    // categories,stockitems

    @Resource
    private ProductRepository productRepository;

    @Resource
    private CategoryRepository categoryRepository;

    @Value("${static.content.dir}")
    private String staticContentDir;

    @Value("${static.content.dir.web}")
    private String staticContentDirWeb;

    public List<SItem> stockItems = new ArrayList<>();

    public void addStockItem(AjaxBehaviorEvent event) {
        stockItems.add(new SItem());
    }

    public List<SItem> getStockItems() {
        return stockItems;
    }

    public void setStockItems(List<SItem> stockItems) {
        this.stockItems = stockItems;
    }

    public List<ProductColor> getColors() {
        return Stream.of(ProductColor.values()).collect(Collectors.toList());
    }

    public List<ProductSize> getSizes() {
        return Stream.of(ProductSize.values()).collect(Collectors.toList());
    }

    public static class SItem implements Serializable {
        ProductColor color;
        ProductSize size;
        Integer quantity = 1;

        public ProductColor getColor() {
            return color;
        }

        public void setColor(ProductColor color) {
            this.color = color;
        }

        public ProductSize getSize() {
            return size;
        }

        public void setSize(ProductSize size) {
            this.size = size;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

    }

    @PostConstruct
    public void init() {
        staticContentDir = staticContentDir.replace("~", System.getProperty("user.home"));
        stockItems.add(new SItem());
    }

    @Override
    protected PagingAndSortingRepository<Product, Long> getRepository() {
        return productRepository;
    }

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
        stockItems.forEach(si -> p.getStockItems().add(new StockItem(p, si.size, si.color, si.quantity)));
        return p;
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

}
