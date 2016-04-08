package edu.asu.conceptpower.servlet.users;

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
    private String email;
    private boolean isEncrypted;

    public User() {
    }

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

    public String getUsername() {
        return user;
    }

        
    public void setUsername(String user) {
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

    public void setFullname(String name) {
        this.name = name;
    }

    public String getFullname() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

	public boolean getIsEncrypted() {
		return isEncrypted;
	}

	public void setIsEncrypted(boolean isEncrypted) {
		this.isEncrypted = isEncrypted;
	}

}
