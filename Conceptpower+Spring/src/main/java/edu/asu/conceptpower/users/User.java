package edu.asu.conceptpower.users;

import java.io.Serializable;

public class User implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1019105087386557957L;

    private String username;
    private String pw;
    private String fullname;
    private boolean isAdmin;
    private String email;

    public User() {
    }

    public User(String user, String passwd) {
        this.username = user;
        this.pw = passwd;
        isAdmin = false;
    }

    public User(String user, String passwd, boolean isAdmin) {
        this.username = user;
        this.pw = passwd;
        this.isAdmin = isAdmin;
    }

    public String getUsername() {
        return username;
    }

        
    public void setUsername(String user) {
        this.username = user;
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
        this.fullname = name;
    }

    public String getFullname() {
        return fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
