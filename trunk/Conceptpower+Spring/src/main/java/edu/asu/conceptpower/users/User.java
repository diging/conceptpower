package edu.asu.conceptpower.users;

import java.io.Serializable;

public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1019105087386557957L;
	
	private String user;
	private String pw;
	private String name;
	private boolean isAdmin;
	
	public User() {}
	
	public User(String user, String passwd) {
		this.user = user;
		this.pw = passwd;
		isAdmin = false;
	}
	
	public User(String user, String passwd, boolean isAdmin) {
		this.user = user;
		this.pw = passwd;
		this.isAdmin = isAdmin;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPw() {
		return pw;
	}

	public void setPw(String pw) {
		this.pw = pw;
	}

	public void setIsAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public boolean getIsAdmin() {
		return isAdmin;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	
}
