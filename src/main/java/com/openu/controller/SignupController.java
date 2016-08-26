package com.openu.controller;

import java.io.IOException;
import java.io.Serializable;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("view")
public class SignupController  extends AbstractCustomerInformationCollector implements Serializable {

 
    private static final String LOGIN = "/login";
    private static final String USERNAME_IS_ALREADY_TAKEN = "username is already taken";
    private static final long serialVersionUID = -4695924797588821274L;
    
    @Override
    protected void addFieldsToUser() {
        super.addFieldsToUser();
        if (getRepository().findByUsername(getUsername()) != null) {
            throw new RuntimeException(USERNAME_IS_ALREADY_TAKEN);
        }
        customer.setUsername(getUsername());
    }
    
    protected void doAfterApply() {
          try {
              ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
              RequestDispatcher dispatcher = ((ServletRequest) context.getRequest()).getRequestDispatcher(LOGIN);
	      dispatcher.forward((ServletRequest) context.getRequest(), (ServletResponse) context.getResponse());
	      FacesContext.getCurrentInstance().responseComplete();
	} catch (ServletException | IOException e) {
	    e.printStackTrace();
	    
	}

      }
    
    
}
