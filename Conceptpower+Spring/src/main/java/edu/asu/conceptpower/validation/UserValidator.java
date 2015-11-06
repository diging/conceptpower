package edu.asu.conceptpower.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import edu.asu.conceptpower.users.User;

@Component
public class UserValidator implements Validator {

    @Override
    public boolean supports(Class<?> arg0) {
        return User.class.isAssignableFrom(arg0);
    }

    @Override
    public void validate(Object arg0, Errors err) {

        User user = (User) arg0;
        String username = user.getUser();
        String firstname = user.getName();
        String emailid = user.getEmail();
        String pw = user.getPw();

        if (username == null || !username.matches("^[a-z0-9_-]{3,16}$")) {
            System.out.println("In User Validator");
            err.rejectValue("user","user.required");
            System.out.println("Validated dude!!!");
            
        }

        
        
    }

}
