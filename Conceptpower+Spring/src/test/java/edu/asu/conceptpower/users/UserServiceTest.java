package edu.asu.conceptpower.users;

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

public class UserServiceTest {

    @Mock
    private IUserManager userManager;

    @InjectMocks
    private UserService service;

    @Before
    public void init() {

        MockitoAnnotations.initMocks(this);
        User user = new User();
        user.setUsername("Test");
        user.setFullname("Test");
        user.setPw("test");
        user.setEmail("test@test.com");
        Mockito.when(userManager.findUser("Test")).thenReturn(user);
        Mockito.when(userManager.findUser("Test2")).thenReturn(null);
    }

    @Test
    public void loadUserByUsernameTest() {
        UserDetails details = service.loadUserByUsername("Test");
        assertNotNull(details);
    }
    
    @Test(expected=UsernameNotFoundException.class)
    public void loadUserByUserNameNotAvailableTest(){
       UserDetails details = service.loadUserByUsername("Test2");
       assertNull(details);
    }
}
