package edu.asu.conceptpower.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptType;
import edu.asu.conceptpower.core.ConceptTypesManager;
import edu.asu.conceptpower.db4o.DBNames;
import edu.asu.conceptpower.db4o.DatabaseManager;
import edu.asu.conceptpower.web.util.FacesUtil;

@ManagedBean
@RequestScoped
public class EditTypeView {

	private ConceptType type;
	private List<ConceptType> availableTypes;
	
	
	@ManagedProperty("#{databaseController}")
	private DatabaseController controller;
	

	@ManagedProperty("#{loginController}")
	private LoginController loginController;
	
	@PostConstruct
	public void init() {
		Object selected = FacesUtil.getSessionMapValue(Parameter.SELECTED_TYPE);
		if (selected != null)
			type = (ConceptType) selected;
		
		
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
	
	public EditTypeView() {
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
	
	public String editType() {
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
		
		String modified = type.getModified() != null ? type.getModified(): "";
		if (!modified.trim().isEmpty())
			modified += ", ";
		type.setModified(modified + userId + "@" + (new Date()).toString());
		
		conceptManager.storeModifiedConceptType(type);
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
