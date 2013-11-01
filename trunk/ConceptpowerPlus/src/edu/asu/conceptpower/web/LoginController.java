package edu.asu.conceptpower.web;

import java.io.Serializable;

import javax.el.ELContext;
import javax.el.ELResolver;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import edu.asu.conceptpower.db4o.DBNames;
import edu.asu.conceptpower.db4o.DatabaseManager;
import edu.asu.conceptpower.users.User;
import edu.asu.conceptpower.users.UsersManager;

@ManagedBean
@SessionScoped
public class LoginController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1858421195272574867L;

	private User user;

	private String name;
	private String passwd;
	
	public LoginController() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String login() {
		ELResolver resolver = FacesContext.getCurrentInstance()
				.getApplication().getELResolver();
		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		DatabaseController provider = (DatabaseController) resolver.getValue(
				elContext, null, "databaseController");
		DatabaseManager manager = provider.getDatabaseProvider()
				.getDatabaseManager(DBNames.USER_DB);

		UsersManager usersManager = new UsersManager(manager);
		
		UserSuperAdminController superAdmins = (UserSuperAdminController) resolver
				.getValue(elContext, null, "userSuperAdminController");
		usersManager.setAdmins(superAdmins.getAdmins());

		User userLoaded = usersManager.getUser(name, passwd);
		usersManager.close();
		if (userLoaded != null) {
			user = userLoaded;
			return "success";
		}

		user = null;
		return "failure";

	}
	
	public String logout() {
		user = null;
		return "logout";
	}
	
	public boolean isLoggedIn() {
		return user != null;
	}

	public void setIsAdmin(boolean isAdmin) {
		// do nothing
	}

	public boolean getIsAdmin() {
		if (user == null)
			return false;
		return user.getIsAdmin();
	}
	
}
