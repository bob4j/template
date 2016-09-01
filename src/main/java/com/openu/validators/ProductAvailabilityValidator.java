package com.openu.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
@FacesValidator("ProductAvailabilityValidator")
public class ProductAvailabilityValidator implements Validator {
    @Override
    public void validate(FacesContext facesContext, UIComponent component, Object quantity) throws ValidatorException {
	HtmlOutputText availabilityComponent = (HtmlOutputText)component.findComponent("availability");
	 String availabilityValue = availabilityComponent.getValue().toString();
	if (!availabilityValue.equals("Available")){
            FacesMessage msg =
       	     new FacesMessage("product availability validation failed","The product is not available in stock");
            msg.setSeverity(FacesMessage.SEVERITY_WARN);
            facesContext.renderResponse();
            throw new ValidatorException(msg);
        }
    }

}
