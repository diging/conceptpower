package edu.asu.conceptpower.root;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import edu.asu.conceptpower.servlet.users.ConceptpowerGrantedAuthority;
import edu.asu.conceptpower.servlet.users.IUserManager;
import edu.asu.conceptpower.servlet.users.User;

@Service
public class UserService implements UserDetailsService {

    @Autowired
	private IUserManager userManager;

    @Override
	public UserDetails loadUserByUsername(String arg0)
			throws UsernameNotFoundException {
		User user = userManager.findUser(arg0);
		
		if (user == null)
			throw new UsernameNotFoundException("Couldn't find username.");
		
		List<ConceptpowerGrantedAuthority> roles = new ArrayList<ConceptpowerGrantedAuthority>();
		roles.add(new ConceptpowerGrantedAuthority(ConceptpowerGrantedAuthority.ROLE_USER));
		if (user.getIsAdmin())
			roles.add(new ConceptpowerGrantedAuthority(ConceptpowerGrantedAuthority.ROLE_ADMIN));
		
		UserDetails details = new CPUserDetails(user.getUsername(), user.getFullname(), user.getPw(), roles, user.getEmail());
		return details;
	}
    

    public IUserManager getUserManager() {
        return userManager;
    }

    public void setUserManager(IUserManager userManager) {
        this.userManager = userManager;
    }
}
