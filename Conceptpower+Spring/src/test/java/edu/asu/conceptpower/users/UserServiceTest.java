package edu.asu.conceptpower.users;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import edu.asu.conceptpower.app.users.IUserManager;
import edu.asu.conceptpower.app.users.UserService;

public class UserServiceTest {

    @Mock
    private IUserManager userManager = Mockito.mock(IUserManager.class);

    @InjectMocks
    private UserService service;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        User user = new User();
        user.setUsername("Test");
        user.setFullname("Test Test");
        user.setPw("test");
        user.setEmail("test@test.com");
        Mockito.when(userManager.findUser("Test")).thenReturn(user);
    }

    @Test
    public void loadUserByUsernameTest() {
        UserDetails details = service.loadUserByUsername("Test");
        assertNotNull(details);
        assertEquals("Test", details.getUsername());
        assertEquals("test", details.getPassword());
        // No getter for Email and Full Name
    }

    @Test(expected = UsernameNotFoundException.class)
    public void loadUserByUserNameNotAvailableTest() {
        UserDetails details = service.loadUserByUsername("Test2");
        assertNull(details);
    }
}
