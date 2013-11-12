package edu.asu.conceptpower.users;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * UserDetails implementation for Conceptpower.
 * @author Julia Damerow
 *
 */
class CPUserDetails implements UserDetails {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2936283695620688432L;
	
	private String username;
	private String name;
	private String password;
	private List<ConceptpowerGrantedAuthority> authorities;
	private String email;

	public CPUserDetails(String username, String name, String password,
			List<ConceptpowerGrantedAuthority> authorities, String email) {
		super();
		this.username = username;
		this.name = name;
		this.password = password;
		this.authorities = authorities;
		this.email = email;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}
	
	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	
}