package com.openu.controller;

import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;

import com.openu.model.Administrator;
import com.openu.repository.AdministratorRepository;
import com.openu.service.CreateAdminUserEmailSender;
import com.openu.util.Constants;

@ManagedBean
@RequestScoped
@Component
public class AdminController extends AbstractCrudController<Administrator> {

    @Autowired
    private AdministratorRepository administratorRepository;
    
    @Resource
    private CreateAdminUserEmailSender createAdminUserEmailSender;
    
    @Resource
    private SessionBean sessionBean;

    private String name;
    private String username;
    private String password;
    private String email;

    public Iterable<Administrator> getUsers() {
        return administratorRepository.findAll();
    }

    @Override
    protected PagingAndSortingRepository<Administrator, Long> getRepository() {
        return administratorRepository;
    }

    @Override
    protected Administrator createEntity() throws Exception {
        if (administratorRepository.findByUsername(username) != null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "username already exists", null));
            return null;
        }
        Administrator admin = new Administrator();
        admin.setName(name);
        admin.setUsername(username);
        admin.setPassword(password);
        admin.setEmail(email);
        return admin;
    }

    @Override
    public void delete(Administrator entity) {
        if (entity.getUsername().equals(Constants.ADMIN)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "cannot delete protected user", null));
            return;
        }
        super.delete(entity);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public void save() throws Exception {
	super.save();
	Administrator admin = sessionBean.getAdmin();
	if (admin != null) {
	    createAdminUserEmailSender.sendCreateAdminUserMessage(email, name, admin.getName(), password, username);
	}
    }
}