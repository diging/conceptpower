package edu.asu.conceptpower.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import edu.asu.conceptpower.users.IUserManager;
import edu.asu.conceptpower.web.backing.UserBacking;

@Component
public class EmailValidator implements Validator {
    @Autowired
    private IUserManager usersManager;
    
    private final SpringValidatorAdapter validator;

    public EmailValidator(SpringValidatorAdapter validator) {
        super();
        this.validator = validator;
    }

    @Override
    public boolean supports(Class<?> arg0) {
        return UserBacking.class.isAssignableFrom(arg0);
    }

    /**
     * Validates the User Object for proper specification
     * 
     * @param err
     *            Error object for binding all the validation errors
     * @param arg0
     *            Generic Object to hold the details of the user from the UI
     */
    @Override
    public void validate(Object arg0, Errors err) {

        UserBacking user = (UserBacking) arg0;
        String emailid = user.getEmail();

        if (usersManager.findUserByEmail(emailid) != null) {
            err.rejectValue("email", "exists.email");
        }

    }

}
