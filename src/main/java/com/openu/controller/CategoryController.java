package com.openu.controller;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.openu.model.Administrator;
import com.openu.model.Category;
import com.openu.model.Customer;
import com.openu.repository.CategoryRepository;
import com.openu.util.Utils;

@ManagedBean
@RequestScoped
@Component
public class CategoryController implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -6066730947592699676L;
	@Autowired
    private CategoryRepository categoryRepository;

    public Category getCategory() {
        String categoryId = Utils.getRequest().getParameter("category_id");
        return categoryRepository.findOne(Long.valueOf(categoryId));
    }

    public Iterable<Category> getAll() {
        return categoryRepository.findAll();
    }
    
    public String getCustomer() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof Customer) {
            return ((Customer) principal).getFirstName();
        }
        return null;
    }

    public Administrator getAdmin() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof Administrator) {
            return (Administrator) principal;
        }
        return null;
    }


}
