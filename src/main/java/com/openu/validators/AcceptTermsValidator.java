package com.openu.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
@FacesValidator("AcceptTermsValidator")
public class AcceptTermsValidator  implements Validator {


   
    @Override
    public void validate(FacesContext facesContext, UIComponent component, Object o) throws ValidatorException {
	if (o == null || !(Boolean) o) {
            FacesMessage msg =
        	    new FacesMessage("You must accept terms of use","accept terms validation failed");
            msg.setSeverity(FacesMessage.SEVERITY_WARN);
            facesContext.renderResponse();
            throw new ValidatorException(msg);
        }
     }
}
