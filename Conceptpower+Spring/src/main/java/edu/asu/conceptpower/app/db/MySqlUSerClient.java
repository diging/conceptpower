package edu.asu.conceptpower.app.db;

import java.util.Map;

import edu.asu.conceptpower.app.model.User;
import edu.asu.conceptpower.app.mysql.IUserDBManager;
import edu.asu.conceptpower.app.users.Token;

public class MySqlUSerClient implements IUserDBManager{

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
    public User[] getAllUsers() {
        
        return null;
    }

    @Override
    public User addUser(User user) {
        
        return null;
    }

    @Override
    public void deleteUser(String username) {
        
        
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
