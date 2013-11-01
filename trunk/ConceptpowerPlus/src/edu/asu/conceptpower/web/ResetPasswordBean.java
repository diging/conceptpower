package edu.asu.conceptpower.web;

import java.io.Serializable;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import edu.asu.conceptpower.db4o.DBNames;
import edu.asu.conceptpower.db4o.DatabaseManager;
import edu.asu.conceptpower.users.User;
import edu.asu.conceptpower.users.UsersManager;

@ManagedBean
@ViewScoped
public class ResetPasswordBean extends DatabaseBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7642569566155678771L;

	private String userId;
	private User user;
	private boolean hasAccess;
	private String newPassword;
	
	@ManagedProperty("#{loginController}")
	private LoginController loginController;
	
	
	public LoginController getLoginController() {
		return loginController;
	}

	public void setLoginController(LoginController loginController) {
		this.loginController = loginController;
	}

	@PostConstruct
	public void init() {
		setHasAccess(loginController.getIsAdmin());
		Map<String,String> params = 
				FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		userId = params.get("userid");
		System.out.println("user id: " + userId);
		
		if (userId == null || userId.trim().isEmpty())
			return;
		
		DatabaseManager manager = getDatabaseController().getDatabaseProvider().getDatabaseManager(DBNames.USER_DB);

		UsersManager usersManager = new UsersManager(manager);
		setUser(usersManager.findUser(userId));
	}
	
	public String resetPassword() {
		if (newPassword == null || newPassword.trim().isEmpty() || user == null)
			return "failed";
		
		user.setPw(newPassword);
		
		DatabaseManager manager = getDatabaseController().getDatabaseProvider().getDatabaseManager(DBNames.USER_DB);

		UsersManager usersManager = new UsersManager(manager);
		usersManager.storeModifiedUser(user);
		
		return "success";
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public boolean isHasAccess() {
		return hasAccess;
	}

	public void setHasAccess(boolean hasAccess) {
		this.hasAccess = hasAccess;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	
	
}
