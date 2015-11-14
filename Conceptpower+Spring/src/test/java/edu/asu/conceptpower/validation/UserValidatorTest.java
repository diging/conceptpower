package edu.asu.conceptpower.validation;

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

    private User validUser;
    private User invalidUser;
    private User existingUser;
    private User emptyUser;

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

        Errors errors = new BindException(validUser, "user");
        ValidationUtils.invokeValidator(uValidator, validUser, errors);
        Assert.assertFalse(errors.hasErrors());
        Assert.assertNull(errors.getFieldError("username"));
        Assert.assertNull(errors.getFieldError("email"));
        Assert.assertNull(errors.getFieldError("pw"));
        Assert.assertNull(errors.getFieldError("fullname"));
    }

    @Test
    public void testInvalidUserInput() {

        Errors errors = new BindException(invalidUser, "user");
        ValidationUtils.invokeValidator(uValidator, invalidUser, errors);
        Assert.assertEquals(4, errors.getFieldErrorCount());
        Assert.assertEquals(errors.getFieldError("username").getCode(), "proper.username");
        Assert.assertEquals(errors.getFieldError("email").getCode(), "proper.email");
        Assert.assertEquals(errors.getFieldError("pw").getCode(), "password.short");
        Assert.assertEquals(errors.getFieldError("fullname").getCode(), "proper.name");

    }

    @Test
    public void testEmptyUser() {
        Errors errors = new BindException(emptyUser, "user");
        ValidationUtils.invokeValidator(uValidator, emptyUser, errors);
        Assert.assertEquals(4, errors.getFieldErrorCount());
        Assert.assertEquals(errors.getFieldError("username").getCode(), "required.username");
        Assert.assertEquals(errors.getFieldError("email").getCode(), "required.email");
        Assert.assertEquals(errors.getFieldError("pw").getCode(), "required.password");
        Assert.assertEquals(errors.getFieldError("fullname").getCode(), "required.name");

    }

    @Test
    public void testUserExists() {
        Errors errors = new BindException(existingUser, "user");
        ValidationUtils.invokeValidator(uValidator, existingUser, errors);
        Assert.assertEquals(1, errors.getFieldErrorCount());
        Assert.assertEquals(errors.getFieldError("username").getCode(), "exists.username");
        Assert.assertNull(errors.getFieldError("email"));
        Assert.assertNull(errors.getFieldError("pw"));
        Assert.assertNull(errors.getFieldError("fullname"));
    }

}
