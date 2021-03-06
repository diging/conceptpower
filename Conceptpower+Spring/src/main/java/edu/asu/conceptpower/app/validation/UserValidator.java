package edu.asu.conceptpower.app.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import edu.asu.conceptpower.app.users.IUserManager;
import edu.asu.conceptpower.web.backing.UserBacking;

@Component
public class UserValidator implements Validator {
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
        String username = user.getUsername();
        String fullName = user.getFullname();
        String emailid = user.getEmail();
        String password = user.getPassword();
        String retypePassword = user.getRetypedPassword();

        // Validator for username - reject if empty or if contains special
        // characters.
        if (username.isEmpty()) {
            err.rejectValue("username", "required.username");
        }

        if (!username.isEmpty() && !username.matches("^[a-z0-9_-]{3,16}$")) {
            err.rejectValue("username", "proper.username");
        }

        // Validator for Name - reject if empty or if contains numbers or
        // special characters.
        if (fullName.isEmpty()) {
            err.rejectValue("fullname", "required.name");
        }

        if (!fullName.isEmpty() && !fullName.matches("^[a-zA-Z\\p{L} ]{2,25}$")) {
            err.rejectValue("fullname", "proper.name");
        }

        // Validator for emailid - reject if empty
        if (emailid.isEmpty()) {
            err.rejectValue("email", "required.email");
        }

        // Validator for password - reject if empty
        if (password.isEmpty()) {
            err.rejectValue("password", "required.password");
        }
        // Validator for password - reject if too short
        if (!password.isEmpty() && password.length() < 4) {
            err.rejectValue("password", "short.password");
        }

        if (retypePassword.isEmpty()) {
            err.rejectValue("retypedPassword", "required.password");
        }

        if (!retypePassword.isEmpty() && !password.equals(retypePassword)) {
            err.rejectValue("retypedPassword", "match.passwords");
        }

        // Validator for email - reject if not proper
        if (!emailid.isEmpty() && !emailid.matches(
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")) {
            err.rejectValue("email", "proper.email");

        }
        // Validator for username - reject if already exists
        if (usersManager.findUser(username) != null) {
            err.rejectValue("username", "exists.username");
        }

        if (usersManager.findUserByEmail(emailid) != null) {
            err.rejectValue("email", "exists.email");

        }

    }

}
