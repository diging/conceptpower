package edu.asu.conceptpower.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import edu.asu.conceptpower.core.ConceptType;
import edu.asu.conceptpower.core.ConceptTypesManager;
import edu.asu.conceptpower.db4o.DBNames;
import edu.asu.conceptpower.db4o.DatabaseManager;

@ManagedBean
@RequestScoped
public class AddTypeView {

	private ConceptType type;
	private List<ConceptType> availableTypes;
	
	
	@ManagedProperty("#{databaseController}")
	private DatabaseController controller;
	

	@ManagedProperty("#{loginController}")
	private LoginController loginController;
	
	@PostConstruct
	public void init() {
		ConceptTypesManager typeManager;
		availableTypes = new ArrayList<ConceptType>();
		try {
			typeManager = new ConceptTypesManager(controller.getDatabaseProvider().getDatabaseManager(DBNames.TYPES_DB).getClient());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
		ConceptType[] allTypes = typeManager.getAllTypes();
		for (ConceptType t : allTypes) 
			availableTypes.add(t);
		typeManager.close();
	}
	
	public AddTypeView() {
		this.type = new ConceptType();
	}
	

	public DatabaseController getController() {
		return controller;
	}

	public void setController(DatabaseController controller) {
		this.controller = controller;
	}

	public void setType(ConceptType type) {
		this.type = type;
	}

	public ConceptType getType() {
		return type;
	}
	
	public String createType() {
		DatabaseManager manager = controller.getDatabaseProvider().getDatabaseManager(DBNames.TYPES_DB);
	
		ConceptTypesManager conceptManager;
		try {
			conceptManager = new ConceptTypesManager(manager.getClient());
		} catch (IOException e) {
			return "failed";
		}
		
		if (loginController.getUser() == null)
			return "failed";
		
		String userId = loginController.getUser().getUser();
		type.setCreatorId(userId);
		
		conceptManager.addConceptType(type);
		conceptManager.close();
		
		return "success";
	}


	public void setLoginController(LoginController loginController) {
		this.loginController = loginController;
	}


	public LoginController getLoginController() {
		return loginController;
	}


	public void setAvailableTypes(List<ConceptType> availableTypes) {
		this.availableTypes = availableTypes;
	}


	public List<ConceptType> getAvailableTypes() {
		return availableTypes;
	}
}
