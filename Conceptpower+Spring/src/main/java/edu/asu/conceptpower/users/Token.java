package edu.asu.conceptpower.users;

import java.util.Date;

/**
 * This class represents a token for password recovery.
 * @author jdamerow
 *
 */
public class Token {

	private String token;
	private Date creationDate;
	
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
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
	
}
