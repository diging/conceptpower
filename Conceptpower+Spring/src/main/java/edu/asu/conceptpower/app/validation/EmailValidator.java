package edu.asu.conceptpower.app.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import edu.asu.conceptpower.app.model.User;
import edu.asu.conceptpower.app.users.IUserManager;
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
        User userByEmail = usersManager.findUserByEmail(emailid);

        // Validator for Name - reject if empty or if contains numbers or
        // special characters.
        if (fullName.isEmpty()) {
            err.rejectValue("fullname", "required.name");
        }

        if (!fullName.isEmpty() && !fullName.matches("^[a-zA-Z\\p{L} ]{2,25}$")) {
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

        if (!emailid.isEmpty() && userByEmail != null && !userByEmail.getUsername().equals(user.getUsername())) {
            err.rejectValue("email", "exists.email");
        }

    }

}
