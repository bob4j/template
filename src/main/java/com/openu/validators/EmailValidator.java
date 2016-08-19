package com.openu.validators;

import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
@FacesValidator("EmailValidator")
public class EmailValidator  implements Validator {

    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    public static boolean valid(String email) {
        return pattern.matcher(email).matches();
    }
    @Override
    public void validate(FacesContext facesContext, UIComponent component, Object email) throws ValidatorException {
	String emailAsString = email.toString();
	if (!pattern.matcher(emailAsString).matches()){
	 FacesMessage msg =
    	     new FacesMessage("Invalid email format. Valid format is:  abc@ab.ab or abc@ab.ab.ab","Phone validation failed");
         msg.setSeverity(FacesMessage.SEVERITY_ERROR);
         facesContext.renderResponse();
         throw new ValidatorException(msg);
	}
     }

}
