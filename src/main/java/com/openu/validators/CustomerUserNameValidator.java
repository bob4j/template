package com.openu.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.openu.repository.CustomerRepository;

@Component
@Scope("request")
public class CustomerUserNameValidator implements Validator {
    @Autowired
    CustomerRepository repository;

    @Override
    public void validate(FacesContext facesContext, UIComponent component, Object value) throws ValidatorException {
        String userNameValue = value.toString();
        if (repository.findByUsername(userNameValue) != null) {
            FacesMessage msg = new FacesMessage("Username " + userNameValue + " is already taken", "user name validation failed");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            facesContext.renderResponse();
            throw new ValidatorException(msg);
        }
    }

}
