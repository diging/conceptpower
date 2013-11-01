package edu.asu.conceptpower.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import edu.asu.conceptpower.db4o.DBNames;
import edu.asu.conceptpower.db4o.DatabaseManager;
import edu.asu.conceptpower.users.User;
import edu.asu.conceptpower.users.UsersManager;

@ManagedBean
@RequestScoped
public class UserListView implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4348245573637453930L;

	@ManagedProperty("#{loginController}")
	private LoginController loginController;
	
	
	private boolean hasAccess;
	private DataModel<User> datamodel;
	
	@PostConstruct
	public void init() {
		hasAccess = loginController.getIsAdmin();
		datamodel = new ListDataModel<User>();
	}
	
	public String removeUser() {
		User remove = datamodel.getRowData();
		
		DatabaseManager manager = getDatabaseController().getDatabaseProvider().getDatabaseManager(DBNames.USER_DB);

		UsersManager usersManager = new UsersManager(manager);
		usersManager.deleteUser(remove.getUser());
		
		usersManager.close();
		
		return "removed";
		
	}
	
	public void setLoginController(LoginController loginController) {
		this.loginController = loginController;
	}

	public LoginController getLoginController() {
		return loginController;
	}

	protected DatabaseController getDatabaseController() {
		ELContext context = FacesContext.getCurrentInstance().getELContext();

		DatabaseController provider = (DatabaseController) context
				.getELResolver().getValue(context, null, "databaseController");
		return provider;
	}

	public void setHasAccess(boolean hasAccess) {
		this.hasAccess = hasAccess;
	}

	public boolean getHasAccess() {
		return hasAccess;
	}
	
	public void setDatamodel(DataModel<User> datamodel) {
		this.datamodel = datamodel;
	}

	public DataModel<User> getDatamodel() {
		DatabaseManager manager = getDatabaseController().getDatabaseProvider().getDatabaseManager(DBNames.USER_DB);

		UsersManager usersManager = new UsersManager(manager);
		User[] allUsers = usersManager.getAllUsers();
		List<User> users = new ArrayList<User>();
		for (User u : allUsers) {
			users.add(u);
		}
		
		usersManager.close();
		
		datamodel = new ListDataModel<User>();
		((ListDataModel<User>)datamodel).setWrappedData(users);
		
		return datamodel;
	}

}
