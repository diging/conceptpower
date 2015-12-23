package edu.asu.conceptpower.users.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.core.env.Environment;

import edu.asu.conceptpower.users.User;
import edu.asu.conceptpower.users.UserDatabaseClient;

public class UsersManagerTest {

    @Mock
    private UserDatabaseClient client;

    @Mock
    private Environment env;

    @InjectMocks
    private UsersManager usersManager;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        try {
            this.usersManager.init();
        } catch (Exception e) {
            // Need to check how to load properties file through Mockito
        }
        User user = new User();
        user.setUsername("Test");
        user.setFullname("Test Test");
        Mockito.when(client.findUser("Test")).thenReturn(user);
        Mockito.when(client.findUser("Test2")).thenReturn(null);

        Mockito.when(client.getUser("Test", "Test")).thenReturn(user);
        Mockito.when(client.getUser("Test2", "Test2")).thenReturn(null);

    }

    @Test
    public void findUserTest() {
        User user = usersManager.findUser("Test");
        assertNotNull(user);
    }

    @Test
    public void findUserNotExistingTest() {
        User user = usersManager.findUser("Test2");
        assertNull(user);
    }

    @Test
    public void getUserTest() {
        User user = usersManager.getUser("Test", "Test");
        assertNotNull(user);
        assertEquals("Test", user.getUsername());
    }

    @Test
    public void getUsersNotExistingTest() {
        User user = usersManager.getUser("Test2", "Test2");
        assertNull(user);
    }

}
