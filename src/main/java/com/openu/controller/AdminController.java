package com.openu.controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;

import com.openu.model.Administrator;
import com.openu.repository.AdministratorRepository;
import com.openu.service.UserService;

@ManagedBean
@RequestScoped
@Component
public class AdminController extends AbstractCrudController<Administrator> {

    @Autowired
    private AdministratorRepository administratorRepository;

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
        Administrator admin = new Administrator();
        admin.setName(name);
        admin.setUsername(username);
        admin.setPassword(password);
        admin.setEmail(email);
        return admin;
    }

    @Override
    public void delete(Administrator entity) {
        if (entity.getUsername().equals(UserService.ADMIN)) {
            throw new RuntimeException("cannot delete admin");
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
}