package edu.asu.conceptpower.app.model;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

/**
 * This model stores information about the generated tokens for a user
 * 
 * @author Keerthivasan Krishnamurthy, Digital Innovation Group
 * 
 */
@Entity(name="token")
public class Token implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name="user_name")
    private String userName;
    
    @Column(name="token")
    private String token;
    
    @Column(name="creation_date")
    private Date creationDate;
    
    @OneToOne
    @JoinColumn(name = "user_name", referencedColumnName="user_name")
    private User user;
    
    public Token(){ }
    
    public Token(String token, Date creationDate) {
        super();
        this.token = token;
        this.creationDate = creationDate;
    }
    
    public Token(String token) {
        this.token = token;
    }
    
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public Date getCreationDate() {
        return creationDate;
    }
    
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
