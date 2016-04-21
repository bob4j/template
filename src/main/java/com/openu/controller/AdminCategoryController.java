package com.openu.controller;

import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.servlet.http.Part;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;

import com.openu.model.Category;
import com.openu.model.Image;
import com.openu.repository.CategoryRepository;

@ManagedBean
@RequestScoped
@Component
public class AdminCategoryController extends AbstractCrudController<Category> implements Serializable {

    private static final long serialVersionUID = 1L;

    private String categoryName;

    private Part imagePart;

    @Autowired
    private CategoryRepository categoryRepository;

    @Value("${static.content.dir}")
    private String staticContentDir;

    @Value("${static.content.dir.web}")
    private String staticContentDirWeb;

    @PostConstruct
    public void init() {
        staticContentDir = staticContentDir.replace("~", System.getProperty("user.home"));
    }

    @Override
    protected PagingAndSortingRepository<Category, Long> getRepository() {
        return categoryRepository;
    }

    @Override
    protected Category createEntity() throws Exception {
        Category cat = new Category();
        cat.setName(categoryName);
        String filename = System.currentTimeMillis() + getImageSuffix();
        Files.copy(imagePart.getInputStream(), Paths.get(staticContentDir, filename));
        cat.setImage(new Image(staticContentDirWeb + "/" + filename));
        return cat;
    }

    private String getImageSuffix() {
        return imagePart.getSubmittedFileName().substring(imagePart.getSubmittedFileName().lastIndexOf("."),
                imagePart.getSubmittedFileName().length());
    }

    public void setImagePart(Part image) {
        imagePart = image;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Part getImagePart() {
        return imagePart;
    }

}
