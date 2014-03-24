package edu.asu.conceptpower.users;

import java.util.Map;

public interface IUserManager {

	public abstract void setAdmins(Map<String, String> admins);

	/**
	 * Find a user by its user id.
	 * @param name user id
	 * @return user or null
	 */
	public abstract User findUser(String name);

	public abstract User getUser(String name, String pw);

	public abstract User[] getAllUsers();

	public abstract User addUser(User user);

	public abstract void deleteUser(String username);

	public abstract void storeModifiedUser(User user);

}