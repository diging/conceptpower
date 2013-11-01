package edu.asu.conceptpower.web;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptManager;
import edu.asu.conceptpower.core.Constants;
import edu.asu.conceptpower.db4o.DBNames;
import edu.asu.conceptpower.db4o.DatabaseManager;
import edu.asu.conceptpower.db4o.DatabaseProvider;
import edu.asu.conceptpower.web.util.FacesUtil;
import edu.asu.conceptpower.web.wrapper.ConceptEntryWrapper;

@ManagedBean
@RequestScoped
public class ConceptDeleteBean extends DatabaseBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6207205237895370055L;

	private ConceptEntryWrapper concept;
	
	@ManagedProperty("#{loginController}")
	private LoginController loginController;
	
	@ManagedProperty("#{wordNetConfController}")
	private WordNetConfController configurationController;
	
	@PostConstruct
	public void init() {
		Object selected = FacesUtil.getSessionMapValue(Parameter.DELETE_CONCEPT);
		if (selected != null)
			concept = (ConceptEntryWrapper) selected;
	}
	
	public String returnToList() {
		FacesUtil.setSessionMapValue(Parameter.SELECTED_LIST, concept.getEntry().getConceptList());
		return "viewList";
	}
	
	public String deleteConcept() {
		
		DatabaseProvider provider = getDatabaseController().getDatabaseProvider();
		
		DatabaseManager manager = provider.getDatabaseManager(DBNames.WORDNET_CACHE);
		
		if (loginController.getUser() == null)
				return "failed";
		
		if (loginController.getUser() == null)
			return "failed";
		
		ConceptManager conceptManager;
		try {
			conceptManager = new ConceptManager(manager, provider.getDatabaseManager(DBNames.DICTIONARY_DB),
					configurationController.getWordNetConfiguration());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "failed";
		}
		
		concept.getEntry().setDeleted(true);
		
		String userId = loginController.getUser().getUser();
		String modified = concept.getEntry().getModified() != null ? concept.getEntry().getModified(): "";
		if (!modified.trim().isEmpty())
			modified += ", ";
		concept.getEntry().setModified(modified + userId + "@" + (new Date()).toString());
		
		conceptManager.storeModifiedConcept(concept.getEntry());
		conceptManager.close();
		
		FacesUtil.setSessionMapValue(Parameter.SELECTED_LIST, concept.getEntry().getConceptList());
		
		return "success";
	}

	public ConceptEntryWrapper getConcept() {
		return concept;
	}

	public void setConcept(ConceptEntryWrapper concept) {
		this.concept = concept;
	}

	public LoginController getLoginController() {
		return loginController;
	}

	public void setLoginController(LoginController loginController) {
		this.loginController = loginController;
	}

	public WordNetConfController getConfigurationController() {
		return configurationController;
	}

	public void setConfigurationController(WordNetConfController configurationController) {
		this.configurationController = configurationController;
	}
	
}
