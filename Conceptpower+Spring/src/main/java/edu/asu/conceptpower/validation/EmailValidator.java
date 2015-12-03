package edu.asu.conceptpower.validation;

import java.util.Map;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorFactory;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import edu.asu.conceptpower.users.IUserManager;
import edu.asu.conceptpower.web.backing.UserBacking;

@Component
public class EmailValidator implements Validator {

    @Autowired
    private IUserManager usersManager;

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
        String fullName = user.getFullname();

        // Validator for Name - reject if empty or if contains numbers or
        // special characters.
        if (fullName.isEmpty()) {
            err.rejectValue("fullname", "required.name");
        }

        if (!fullName.isEmpty() && !fullName.matches("^[a-zA-Z ]{3,25}$")) {
            err.rejectValue("fullname", "proper.name");
        }

        if (emailid.isEmpty()) {
            err.rejectValue("email", "required.email");

        }

        // Validator for email - reject if not proper
        if (!emailid.isEmpty() && !emailid.matches(
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")) {
            err.rejectValue("email", "proper.email");

        }

        if (!emailid.isEmpty() && usersManager.findUserByEmail(emailid) != null) {
            err.rejectValue("email", "exists.email");
        }

    }

}
