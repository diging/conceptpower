package edu.asu.conceptpower.app.db;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import edu.asu.conceptpower.app.db.repository.UserRepository;
import edu.asu.conceptpower.app.model.User;
import edu.asu.conceptpower.app.mysql.IUserDBManager;
import edu.asu.conceptpower.app.users.Token;

public class MySqlUSerClient implements IUserDBManager{

    @Autowired
    UserRepository usrRepository;
    
    @Override
    public void setAdmins(Map<String, String> admins) {
        
        
    }

    @Override
    public User findUser(String name) {
        
        return null;
    }

    @Override
    public User getUser(String name, String pw) {
        
        return null;
    }

    @Override
    public Iterable<User> getAllUsers() {
        
        return usrRepository.findAll();
        
    }

    @Override
    public void addUser(User user) {
        
        usrRepository.save(user);
    }

    @Override
    public void deleteUser(String username) {
        
        usrRepository.delete(entity);
    }

    @Override
    public void storeModifiedUser(User user) {
        
        
    }

    @Override
    public void storeModifiedPassword(User user) {
        
        
    }

    @Override
    public Token deleteToken(String token) {
        
        return null;
    }

    @Override
    public Token findToken(String token) {
        
        return null;
    }

    @Override
    public Token createToken(User user) {
        
        return null;
    }

    @Override
    public User findUserByEmail(String email) {
        
        return null;
    }

    @Override
    public void updatePasswordEncryption(String username) {
        
        
    }

}
