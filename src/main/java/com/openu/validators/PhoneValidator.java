package com.openu.validators;

import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
@FacesValidator("PhoneValidator")
public class PhoneValidator implements Validator {
    private static final String MOBILE_PHONE_STRING_PATTERN = "[0]"+"[0-9]"+"[0-9]"+"-"+"[0-9]"+"[0-9]"+"[0-9]"+"[0-9]"+"[0-9]"+"[0-9]"+"[0-9]";
    private static final String STATIC_PHONE_STRING_PATTERN = "[0]"+"[0-9]"+"-"+"[0-9]"+"[0-9]"+"[0-9]"+"[0-9]"+"[0-9]"+"[0-9]"+"[0-9]";
    private static Pattern mobilePhonePattern = Pattern.compile(MOBILE_PHONE_STRING_PATTERN);
    private static Pattern staticPhonePattern = Pattern.compile(STATIC_PHONE_STRING_PATTERN);


    @Override
    public void validate(FacesContext facesContext, UIComponent component, Object value) throws ValidatorException {
	String valueAsString = value.toString();
	         boolean mobilePhoneMatcher = mobilePhonePattern.matcher(valueAsString).matches();
	         boolean staticPhoneMatcher = staticPhonePattern.matcher(valueAsString).matches();
	         if (!mobilePhoneMatcher && !staticPhoneMatcher ){
	             FacesMessage msg =
	        	     new FacesMessage("Invalid Phone format. Valid format is:  09-9999999 or 099-9999999","Phone validation failed");
	             msg.setSeverity(FacesMessage.SEVERITY_ERROR);
	             facesContext.renderResponse();
	             throw new ValidatorException(msg);
	         }
    }

}
