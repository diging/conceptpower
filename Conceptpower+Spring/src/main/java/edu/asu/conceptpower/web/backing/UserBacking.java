package edu.asu.conceptpower.web.backing;

public class UserBacking {

	private String username;
	private String name;
	private String password;
	private String retypedPassword;
	private boolean isAdmin;

	public UserBacking(String username, String name) {
		super();
		this.username = username;
		this.name = name;
	}

	public UserBacking() {

	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRetypedPassword() {
		return retypedPassword;
	}

	public void setRetypedPassword(String retypedPassword) {
		this.retypedPassword = retypedPassword;
	}

	public boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

}
