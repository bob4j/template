package com.openu.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
@FacesValidator("PasswordAgainValidator")
public class PasswordAgainValidator implements Validator {
    @Override
    public void validate(FacesContext facesContext, UIComponent component, Object value) throws ValidatorException {
	String passwordAgainValue = value.toString();
	UIInput passwordComponentComponent = (UIInput)component.findComponent("password");
	Object passwordValue = passwordComponentComponent.getLocalValue().toString();
	if (!passwordValue.equals(passwordAgainValue)){
            FacesMessage msg =
       	     new FacesMessage("Passwords do not match","Password Again validation failed");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            facesContext.renderResponse();
            throw new ValidatorException(msg);
        }
    }

}
