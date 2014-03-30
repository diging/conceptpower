package edu.asu.conceptpower.users.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import edu.asu.conceptpower.users.IUserManager;
import edu.asu.conceptpower.users.User;
import edu.asu.conceptpower.users.UserDatabaseClient;

/**
 * Managing class for user management.
 * 
 * @author Julia Damerow
 * 
 */
@Service
public class UsersManager implements IUserManager {

	@Autowired
	private UserDatabaseClient client;
	private Map<String, String> admins;

	@PostConstruct
	public void init() {
		admins = new HashMap<String, String>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.asu.conceptpower.users.IUserManager#setAdmins(java.util.Map)
	 */
	@Override
	public void setAdmins(Map<String, String> admins) {
		if (admins != null)
			this.admins = admins;
		else
			admins = new HashMap<String, String>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.asu.conceptpower.users.IUserManager#findUser(java.lang.String)
	 */
	@Override
	public User findUser(String name) {
		if (admins.containsKey(name)) {
			String storedPW = admins.get(name);
			User admin = new User(name, storedPW);
			return admin;
		}

		User user = client.findUser(name);
		return user;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.asu.conceptpower.users.IUserManager#getUser(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public User getUser(String name, String pw) {

		if (admins.containsKey(name)) {
			String storedPW = admins.get(name);
			if (storedPW.equals(pw))
				return new User(name, pw, true);
		}
		User user = client.getUser(name, pw);
		return user;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.asu.conceptpower.users.IUserManager#getAllUsers()
	 */
	@Override
	public User[] getAllUsers() {
		User[] users = client.getAllUser();
		List<User> userNames = new ArrayList<User>();
		for (User u : users) {
			User user = new User();
			user.setUser(u.getUser());
			user.setName(u.getName());
			user.setIsAdmin(u.getIsAdmin());
			userNames.add(user);
		}
		return userNames.toArray(new User[userNames.size()]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.asu.conceptpower.users.IUserManager#addUser(edu.asu.conceptpower.
	 * users.User)
	 */
	@Override
	public User addUser(User user) {
		encryptPassword(user);
		client.addUser(user);
		return user;
	}

	private void encryptPassword(User user) {
		user.setPw(BCrypt.hashpw(user.getPw(), BCrypt.gensalt()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.asu.conceptpower.users.IUserManager#deleteUser(java.lang.String)
	 */
	@Override
	public void deleteUser(String username) {
		client.deleteUser(username);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.asu.conceptpower.users.IUserManager#storeModifiedUser(edu.asu.
	 * conceptpower.users.User)
	 */
	@Override
	public void storeModifiedUser(User user) {
		client.update(user);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see edu.asu.conceptpower.users.IUserManager#storeModifiedPassword(edu.asu.
	 *      conceptpower.users.User)
	 */
	@Override
	public void storeModifiedPassword(User user) {
		encryptPassword(user);
		client.update(user);
	}

}
