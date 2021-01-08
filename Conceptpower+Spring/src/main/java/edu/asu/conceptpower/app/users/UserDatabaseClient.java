package edu.asu.conceptpower.app.users;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import edu.asu.conceptpower.app.db.DatabaseManager;
import edu.asu.conceptpower.app.model.User;
import edu.asu.conceptpower.app.model.Token;
import edu.asu.conceptpower.app.repository.ITokenRepository;
import edu.asu.conceptpower.app.repository.IUserRepository;

@Component
public class UserDatabaseClient {

    @Autowired
    private IUserRepository userRepository;
    
    @Autowired
    private ITokenRepository tokenRepository;
    
    @Autowired
    @Qualifier("userDatabaseManager")
    private DatabaseManager userDatabase;
    
    public User getUser(String name, String pw) {
        return userRepository.findByUserAndPw(name, pw);
    }
    
    /**
     * Finds a user by its user id.
     * @param name user id.
     * @return found user or null
     */
    public User findUser(String name) {
        return userRepository.findByUser(name);
    }
    
    public List<User> findUsers(User exampleUser) {
        return userRepository.findByEmail(exampleUser.getEmail());
    }
    
    public User[] getAllUser() {
        List<User> results = userRepository.findAll();
         
        return results.toArray(new User[results.size()]);       
    }
    
    public User addUser(User user) {
        userRepository.save(user);
        return user;
    }
    
    @Transactional
    public void deleteUser(String name) {
        userRepository.deleteByUser(name);
    }
    
    public void update(User user) {
        userRepository.save(user);         
    }
    
    public void storeRecoveryToken(Token token) {
        tokenRepository.save(token);
    }
    
    public Token getToken(String token) {
        return tokenRepository.findByToken(token);
    }
    
    @Transactional
    public Token deleteToken(String token) {
        return tokenRepository.deleteByToken(token);
    }
}