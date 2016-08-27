package com.openu.controller;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;

import com.openu.model.Administrator;
import com.openu.repository.AdministratorRepository;

@Component
@Scope("view")
public class AdminEditInformationController extends AbstractInformationCollector<Administrator>  {

    @Resource
    private SessionBean sessionBean;
    @Resource
    private AdministratorRepository adminRepository;   
    private Administrator admin;
    private String name;
    @PostConstruct
    public void prepareEditing() {
       this.admin = sessionBean.loadAdmin();
       setEmail(admin.getEmail());
       setName(admin.getName());
       setPassword(admin.getPassword());
       setUsername(admin.getUsername());
    }

    @Override
    protected void addFieldsToUser() {
	admin.setEmail(getEmail());
	admin.setName(getName());
	admin.setPassword(getPassword());
	admin.setEmail(getEmail());
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    @Override
    PagingAndSortingRepository<Administrator, Long> getRepository() {
	return adminRepository;
    }

    @Override
    Administrator getUser() {
	return admin;
    }
    
}
