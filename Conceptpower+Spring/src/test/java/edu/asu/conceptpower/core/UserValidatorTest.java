package edu.asu.conceptpower.core;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import edu.asu.conceptpower.users.IUserManager;
import edu.asu.conceptpower.users.User;
import edu.asu.conceptpower.validation.UserValidator;
import junit.framework.Assert;

public class UserValidatorTest {
    @Mock
    private IUserManager uManager;
    @InjectMocks
    private UserValidator uValidator;
    @Mock
    private Errors errors;

    User validUser;

    User invalidUser;

    User existingUser;
    
    User emptyUser;

    @Before
    public void init() {
        uManager = Mockito.mock(IUserManager.class);

        MockitoAnnotations.initMocks(this);

        validUser = new User();
        validUser.setUsername("validuser");
        validUser.setEmail("test@abc.xyz");
        validUser.setPw("password");
        validUser.setFullname("Valid User");

        invalidUser = new User();
        invalidUser.setUsername("Invalid User");
        invalidUser.setEmail("test@abc");
        invalidUser.setPw("pas");
        invalidUser.setFullname("Valid9User");

        emptyUser = new User();
        emptyUser.setUsername("");
        emptyUser.setEmail("");
        emptyUser.setPw("");
        emptyUser.setFullname("");
        
        existingUser = new User();
        existingUser.setUsername("username");
        existingUser.setEmail("test@abc.xyz");
        existingUser.setPw("password");
        existingUser.setFullname("Valid User");
        Mockito.when(uManager.findUser("username")).thenReturn(existingUser);

    }

    @Test
    public void testValidUserInput() {

        errors = new BindException(validUser, "user");     
        ValidationUtils.invokeValidator(uValidator, validUser, errors);
        Assert.assertFalse(errors.hasErrors());
        Assert.assertNull(errors.getFieldError("username"));
        Assert.assertNull(errors.getFieldError("email")); 
        Assert.assertNull(errors.getFieldError("pw"));
        Assert.assertNull(errors.getFieldError("fullname"));
    }

    @Test
    public void testInvalidUserInput() {

        errors = new BindException(invalidUser, "user");
        ValidationUtils.invokeValidator(uValidator, invalidUser, errors);
        Assert.assertEquals(4, errors.getFieldErrorCount());
        Assert.assertTrue(errors.hasErrors());
        Assert.assertNotNull(errors.getFieldError("username"));
        Assert.assertNotNull(errors.getFieldError("email"));
        Assert.assertNotNull(errors.getFieldError("pw"));
        Assert.assertNotNull(errors.getFieldError("fullname"));

    }

    @Test
    public void testEmptyUser() {
        errors = new BindException(emptyUser, "user");
        ValidationUtils.invokeValidator(uValidator, emptyUser, errors);
        Assert.assertEquals(4, errors.getFieldErrorCount());
        Assert.assertTrue(errors.hasErrors());        
        Assert.assertNotNull(errors.getFieldError("username"));
        Assert.assertNotNull(errors.getFieldError("email"));
        Assert.assertNotNull(errors.getFieldError("pw"));
        Assert.assertNotNull(errors.getFieldError("fullname"));
    }
    
    @Test
    public void testUserExists() {
        errors = new BindException(existingUser, "user");
        ValidationUtils.invokeValidator(uValidator, existingUser, errors);
        Assert.assertEquals(1, errors.getFieldErrorCount());
        Assert.assertTrue(errors.hasErrors());
        Assert.assertNotNull(errors.getFieldError("username"));
        Assert.assertNull(errors.getFieldError("email"));
        Assert.assertNull(errors.getFieldError("pw"));
        Assert.assertNull(errors.getFieldError("fullname"));
    }

}
