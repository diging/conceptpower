package edu.asu.conceptpower.web.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("validators.LoginUsernameValidator")
public class LoginUsernameValidator implements Validator {

	public void validate(FacesContext arg0, UIComponent arg1, Object arg2)
			throws ValidatorException {
		
		if (arg2 == null || arg2.toString().isEmpty()) {
			FacesMessage msg = 
				new FacesMessage("Login failed", 
						"No username given.");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(msg);
		}
	}

}
