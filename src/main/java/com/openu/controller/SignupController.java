package com.openu.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIForm;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("view")
public class SignupController  extends AbstractCustomerInformationCollector implements Serializable {

 
    private static final long serialVersionUID = -4695924797588821274L;

    public void validate(ComponentSystemEvent e) {
	FacesContext fc = FacesContext.getCurrentInstance();
        if (!fc.getMessageList().isEmpty()) {
            return;
        }
        UIForm form = (UIForm) e.getComponent();
       
        String userNamefield = getField(e, "username");
	if (getRepository().findByUsername(userNamefield) != null) {
            fc.addMessage(form.getClientId(), new FacesMessage("Username is already taken"));
        }
	 if (!getField(e, "password").equals(getField(e, "passwordAgain"))) {
           fc.addMessage(form.getClientId(), new FacesMessage("Passwords do not match"));
       }
        
        if (!fc.getMessageList().isEmpty()) {
            fc.renderResponse();
        }
    }
    
    @Override
    protected void addFieldsToUser() {
        super.addFieldsToUser();
        if (getRepository().findByUsername(getUsername()) != null) {
            throw new RuntimeException("username is already taken");
        }
        customer.setUsername(getUsername());
    }
    
    protected void doAfterApply() {
          try {
              ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
              RequestDispatcher dispatcher = ((ServletRequest) context.getRequest()).getRequestDispatcher("/login");
	      dispatcher.forward((ServletRequest) context.getRequest(), (ServletResponse) context.getResponse());
	      FacesContext.getCurrentInstance().responseComplete();
	} catch (ServletException | IOException e) {
	    e.printStackTrace();
	    
	}

      }
    
    
}
