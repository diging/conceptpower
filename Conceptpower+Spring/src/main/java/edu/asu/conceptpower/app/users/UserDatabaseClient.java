package edu.asu.conceptpower.app.users;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


import edu.asu.conceptpower.app.db.DatabaseManager;
import edu.asu.conceptpower.app.model.User;
import edu.asu.conceptpower.app.model.Token;
import edu.asu.conceptpower.app.repository.ITokenRepository;
import edu.asu.conceptpower.app.repository.IUserRepository;

@Component
public class UserDatabaseClient {

    @Autowired
    private IUserRepository client;
    
    @Autowired
    private ITokenRepository tokenClient;
    
    @Autowired
    @Qualifier("userDatabaseManager")
    private DatabaseManager userDatabase;
    
    public User getUser(String name, String pw) {
        return client.findByUserAndPw(name, pw);
    }
    
    /**
     * Finds a user by its user id.
     * @param name user id.
     * @return found user or null
     */
    public User findUser(String name) {
        return client.findByUser(name);
    }
    
    public List<User> findUsers(User exampleUser) {
        return client.findByEmail(exampleUser.getEmail());
    }
    
    public User[] getAllUser() {
        List<User> results = client.findAll();
         
        return results.toArray(new User[results.size()]);       
    }
    
    public User addUser(User user) {
        client.save(user);
        return user;
    }
    
    public void deleteUser(String name) {
        client.deleteByUser(name);
    }
    
    public void update(User user) {
        client.save(user);         
    }
    
    public void storeRecoveryToken(Token token) {
        tokenClient.save(token);
    }
    
    public Token getToken(String token) {
        return tokenClient.findByToken(token);
    }
    
    public Token deleteToken(String token) {
        return tokenClient.deleteByToken(token);
    }
}