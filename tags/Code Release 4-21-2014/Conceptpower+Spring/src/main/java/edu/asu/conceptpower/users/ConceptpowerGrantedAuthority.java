package edu.asu.conceptpower.users;

import org.springframework.security.core.GrantedAuthority;

public class ConceptpowerGrantedAuthority implements GrantedAuthority {
	
	public final static String ROLE_USER = "ROLE_CP_USER";
	public final static String ROLE_ADMIN = "ROLE_CP_ADMIN";

	private String roleName;

	public ConceptpowerGrantedAuthority(String name) {
		this.roleName = name;
	}

	public ConceptpowerGrantedAuthority() {
	}

	/**
		 * 
		 */
	private static final long serialVersionUID = 711167440813692597L;

	@Override
	public String getAuthority() {
		return roleName;
	}

	public void setAuthority(String rolename) {
		this.roleName = rolename;
	}
}
