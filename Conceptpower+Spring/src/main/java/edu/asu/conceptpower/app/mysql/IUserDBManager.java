package edu.asu.conceptpower.app.mysql;

import java.util.Map;

import edu.asu.conceptpower.app.users.Token;
import edu.asu.conceptpower.app.model.User;

public interface IUserDBManager  {

    public abstract void setAdmins(Map<String, String> admins);

    /**
     * Find a user by its user id.
     * 
     * @param name
     *            user id
     * @return user or null
     */
    public abstract User findUser(String name);

    public abstract User getUser(String name, String pw);

    public abstract Iterable<User> getAllUsers();

    public abstract void addUser(User user);

    public abstract void deleteUser(String username);

    public abstract void storeModifiedUser(User user);

    public abstract void storeModifiedPassword(User user);

    public abstract Token deleteToken(String token);

    public abstract Token findToken(String token);

    public abstract Token createToken(User user);

    public abstract User findUserByEmail(String email);

    public abstract void updatePasswordEncryption(String username);
}
