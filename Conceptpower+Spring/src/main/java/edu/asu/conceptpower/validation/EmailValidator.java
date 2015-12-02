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
public class EmailValidator implements Validator, InitializingBean, ApplicationContextAware, ConstraintValidatorFactory {

    @Autowired
    private IUserManager usersManager;
    private javax.validation.Validator validator;

    @Override
    public boolean supports(Class<?> arg0) {
        return UserBacking.class.isAssignableFrom(arg0);
    }

    private ApplicationContext applicationContext;
    
     
    
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    
            this.applicationContext = applicationContext;
    
        }

        public <T extends ConstraintValidator<?, ?>> T getInstance(Class<T> key) {
            
             
            
                    Map beansByNames = applicationContext.getBeansOfType(key);
            
                    if (beansByNames.isEmpty()) {
            
                        try {
            
                            return key.newInstance();
            
                        } catch (InstantiationException e) {
            
                            throw new RuntimeException("Could not instantiate constraint validator class '" + key.getName() + "'", e);
            
                        } catch (IllegalAccessException e) {
            
                            throw new RuntimeException("Could not instantiate constraint validator class '" + key.getName() + "'", e);
            
                        }
            
                    }
            
                    if (beansByNames.size() > 1) {
            
                        throw new RuntimeException("Only one bean of type '" + key.getName() + "' is allowed in the application context");
            
                    }
            
                    return (T) beansByNames.values().iterator().next();
            
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


    @Override
    public void afterPropertiesSet() throws Exception {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
                validator = validatorFactory.usingContext().getValidator();

        
    }

}
