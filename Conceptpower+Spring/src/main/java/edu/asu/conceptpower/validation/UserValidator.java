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
    IUserManager usersManager;

    @Override
    public boolean supports(Class<?> arg0) {
        return User.class.isAssignableFrom(arg0);
    }

    @Override
    public void validate(Object arg0, Errors err) {

        User user = (User) arg0;
        String username = user.getUsername();
        String fullName = user.getName();
        String emailid = user.getEmail();
        String pw = user.getPw();

        // Validator for username - reject if empty or if contains special
        // characters.
        if (username == null || !username.matches("^[a-z0-9_-]{3,16}$")) {
            System.out.println("In User Validator");
            err.rejectValue("username", "required.username");
        }

        // Validator for Name - reject if empty or if contains numbers or
        // special characters.
        if (fullName == null || !fullName.matches("^[a-zA-Z ]{3,25}$")) {
            err.rejectValue("name", "required.name");
        }

        // Validator for emailid - reject if empty
        if (emailid == null) {
            err.rejectValue("email", "required.email");
        }

        // Validator for password - reject if empty
        if (pw.isEmpty()) {
            err.rejectValue("pw", "required.password");
        }
        // Validator for password - reject if too short
        if (pw.length() < 4) {
            err.rejectValue("pw", "password.short");
        }
        // Validator for email - reject if not proper
        if (!emailid.matches(
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")) {
            err.rejectValue("email", "email.proper");

        }
        // Validator for username - reject if already exists
        if (usersManager.findUser(username) != null) {
            err.rejectValue("username", "username.exists");
        }

    }

}
