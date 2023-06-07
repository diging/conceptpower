package edu.asu.conceptpower.web.backing;

import jakarta.validation.constraints.NotEmpty;

public class EmailBackBean {

	@NotEmpty(message = "Please provide your email address.")
	private String email;
	private String token;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
}
