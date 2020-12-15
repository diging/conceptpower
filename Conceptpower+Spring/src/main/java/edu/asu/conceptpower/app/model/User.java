package edu.asu.conceptpower.app.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This model helps to keep track of information about the list of users for authentication/authorization
 * 
 * @author Keerthivasan Krishnamurthy
 * 
 */

@Entity
@Table(name = "user")
public class User implements Serializable{

    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name="user_name")
    private String user;
    
    @Column(name="password")
    private String pw;
    
    @Column(name="name")
    private String fullName;
    
    @Column(name="is_admin")
    private boolean isAdmin;
    
    @Column(name="email")
    private String email;
    
    @Column(name="is_encrypted")
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
        this.fullName = name;
    }

    public String getFullname() {
        return fullName;
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
