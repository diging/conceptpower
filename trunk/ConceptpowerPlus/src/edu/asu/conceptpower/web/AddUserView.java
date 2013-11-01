package edu.asu.conceptpower.web;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.el.ELContext;
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
public class AddUserView implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8368209458707449334L;

	private User user;

	@ManagedProperty("#{loginController}")
	private LoginController loginController;

	@PostConstruct
	public void init() {
		user = new User();
	}

	public String createUser() {
		DatabaseController controller = getDatabaseController();
		DatabaseManager manager = controller.getDatabaseProvider()
				.getDatabaseManager(DBNames.USER_DB);

		UsersManager usersManager = new UsersManager(manager);
		User addedUser = usersManager.addUser(user);
		usersManager.close();
		if (addedUser != null)
			return "success";
		return "failure";
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	protected DatabaseController getDatabaseController() {
		ELContext context = FacesContext.getCurrentInstance().getELContext();

		DatabaseController provider = (DatabaseController) context
				.getELResolver().getValue(context, null, "databaseController");
		return provider;
	}

	public void setLoginController(LoginController loginController) {
		this.loginController = loginController;
	}

	public LoginController getLoginController() {
		return loginController;
	}

}
