package com.openu.controller;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;

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
}
