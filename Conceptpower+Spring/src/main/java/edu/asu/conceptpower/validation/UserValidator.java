package edu.asu.conceptpower.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import edu.asu.conceptpower.users.IUserManager;
import edu.asu.conceptpower.users.User;

@Component
public class UserValidator implements Validator {
    @Autowired
    private IUserManager usersManager;

    @Override
    public boolean supports(Class<?> arg0) {
        return User.class.isAssignableFrom(arg0);
    }

    /**
     * Validates the User Object for proper specification
     * 
     * @param err
     *      Error object for binding all the validation errors
     * @param arg0
     *      Generic Object to hold the details of the user from the UI
     */
    @Override
    public void validate(Object arg0, Errors err) {

        User user = (User) arg0;
        String username = user.getUsername();
        String fullName = user.getFullname();
        String emailid = user.getEmail();
        String pw = user.getPw();

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

        if (!fullName.isEmpty() && !fullName.matches("^[a-zA-Z ]{3,25}$")) {
            err.rejectValue("fullname", "proper.name");
        }

        // Validator for emailid - reject if empty
        if (emailid.isEmpty()) {
            err.rejectValue("email", "required.email");
        }

        // Validator for password - reject if empty
        if (pw.isEmpty()) {
            err.rejectValue("pw", "required.password");
        }
        // Validator for password - reject if too short
        if (!pw.isEmpty() && pw.length() < 4) {
            err.rejectValue("pw", "password.short");
        }
        // Validator for email - reject if not proper
        if (!emailid.isEmpty() && !emailid.matches(
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")) {
            err.rejectValue("email", "proper.email");

        }
        // Validator for username - reject if already exists
        if (usersManager.findUser(username) != null) {
            err.rejectValue("username", "username.exists");
        }

    }

}
