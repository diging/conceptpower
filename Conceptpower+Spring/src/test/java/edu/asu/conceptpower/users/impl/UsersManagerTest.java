package edu.asu.conceptpower.users.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.core.env.Environment;

import edu.asu.conceptpower.root.UserDatabaseClient;
import edu.asu.conceptpower.root.UsersManager;
import edu.asu.conceptpower.users.User;

public class UsersManagerTest {

    @Mock
    private UserDatabaseClient client = Mockito.mock(UserDatabaseClient.class);

    @Mock
    private Environment env = Mockito.mock(Environment.class);

    @InjectMocks
    private UsersManager usersManager;

    @Mock
    private Map<String, String> admins = Mockito.mock(Map.class);

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        User user = new User();
        user.setUsername("Test");
        user.setFullname("Test Test");
        user.setEmail("test@test.com");
        Mockito.when(client.findUser("Test")).thenReturn(user);
        Mockito.when(client.findUser("Test2")).thenReturn(null);

        admins.put("admin", "admin");

        Mockito.when(client.getUser("Test", "Test")).thenReturn(user);
        Mockito.when(admins.containsKey("admin")).thenReturn(true);
        Mockito.when(admins.get("admin")).thenReturn("admin");

    }

    @Test
    public void findUserTest() {
        User user = usersManager.findUser("Test");
        assertNotNull(user);
        assertEquals("Test", user.getUsername());
        assertEquals("Test Test", user.getFullname());
        assertEquals("test@test.com", user.getEmail());
    }

    @Test
    public void findAdminUserTest() {
        User user = usersManager.findUser("admin");
        assertNotNull(user);
        assertEquals("admin", user.getUsername());
        assertEquals("admin", user.getPw());
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
    public void getAdminUserTest() {
        User user = usersManager.getUser("admin", "admin");
        assertNotNull(user);
        assertEquals("admin", user.getUsername());
        assertEquals(true, user.getIsAdmin());
    }

    @Test
    public void getUsersNotExistingTest() {
        User user = usersManager.getUser("Test2", "Test2");
        assertNull(user);
    }

    @Test
    public void getUsersIllegalPasswordTest() {
        User user = usersManager.getUser("Test", "Test2");
        assertNull(user);
    }
    
    @Test
    public void storeModifiedPassword(){
    	User user = new User();
    	user.setPw("Password");
    	usersManager.storeModifiedPassword(user);
    	Mockito.verify(client).update(user);
    }
}
