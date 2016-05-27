package com.openu.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;

import com.openu.model.Action;
import com.openu.model.Customer;
import com.openu.repository.ActionRepository;
import com.openu.repository.AdministratorRepository;
//TODO SHAHAR fix this class
@ManagedBean
@RequestScoped
@Component
public class AdminActionController extends AbstractCrudController<Action> implements Serializable {

    private static final long serialVersionUID = 1L;

    private String actionName;
    private String actionUrl;




	@Autowired
    private ActionRepository actionRepository;

    @Value("${static.content.dir}")
    private String staticContentDir;

    @Value("${static.content.dir.web}")
    private String staticContentDirWeb;

    @PostConstruct
    public void init() {
        staticContentDir = staticContentDir.replace("~", System.getProperty("user.home"));
    }

    @Override
    protected PagingAndSortingRepository<Action, Long> getRepository() {
        return actionRepository;
    }

    @Override
    protected Action createEntity() throws Exception {
        Action act = new Action();
        act.setName(actionName);
        act.setUrl(actionUrl);
        return act;
    }

   

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getActionUrl() {
    	return actionUrl;
    }
    
    public void setActionUrl(String actionUrl) {
    	this.actionUrl = actionUrl;
    }
}
