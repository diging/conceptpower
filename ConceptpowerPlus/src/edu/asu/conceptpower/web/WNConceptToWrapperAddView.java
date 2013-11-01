package edu.asu.conceptpower.web;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import edu.asu.conceptpower.core.ConceptList;
import edu.asu.conceptpower.core.ConceptManager;
import edu.asu.conceptpower.db4o.DBNames;
import edu.asu.conceptpower.db4o.DatabaseManager;
import edu.asu.conceptpower.db4o.DatabaseProvider;
import edu.asu.conceptpower.web.util.FacesUtil;
import edu.asu.conceptpower.web.wrapper.ConceptEntryWrapper;

@ManagedBean
@ViewScoped
public class WNConceptToWrapperAddView extends DatabaseBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4521090299980971384L;
	
	private ConceptEntryWrapper selectedWordnetConcept;
	private ConceptEntryWrapper selectedConceptWrapper;
	
	@ManagedProperty("#{conceptSearch}")
	private ConceptSearch conceptSearch;
	
	@ManagedProperty("#{loginController}")
	private LoginController loginController;
	
	@ManagedProperty("#{wordNetConfController}")
	private WordNetConfController configurationController;
	
	
	public LoginController getLoginController() {
		return loginController;
	}

	public void setLoginController(LoginController loginController) {
		this.loginController = loginController;
	}

	public WordNetConfController getConfigurationController() {
		return configurationController;
	}

	public void setConfigurationController(
			WordNetConfController configurationController) {
		this.configurationController = configurationController;
	}

	public ConceptSearch getConceptSearch() {
		return conceptSearch;
	}

	public void setConceptSearch(ConceptSearch conceptSearch) {
		this.conceptSearch = conceptSearch;
	}

	public void selectionChanged() {
		// ConceptEntryWrapper selected = conceptSearch.selectionChanged();
		// if (selected.getEntry().getWordnetId() != null &&
		// !selected.getEntry().getWordnetId().trim().isEmpty()) {
		// if
		// (selected.getEntry().getWordnetId().trim().equals(selected.getEntry().getId().trim()))
		// selectedWordnetConcept = selected;
		// }
		// else
		// selectedWordnetConcept = null;
	}
	
	public String addWordnetConcept() {
		if (selectedConceptWrapper == null || selectedWordnetConcept == null) {
			return "emptySelection";
		}
		
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
		
		selectedConceptWrapper.getEntry().setWordnetId(selectedWordnetConcept.getEntry().getId());
		selectedConceptWrapper.setWrappedWordnetEntry(selectedWordnetConcept.getEntry());
		
		String userId = loginController.getUser().getUser();
		String modified = selectedConceptWrapper.getEntry().getModified() != null ? selectedConceptWrapper.getEntry().getModified(): "";
		if (!modified.trim().isEmpty())
			modified += ", ";
		selectedConceptWrapper.getEntry().setModified(modified + userId + "@" + (new Date()).toString());
		
		conceptManager.storeModifiedConcept(selectedConceptWrapper.getEntry());
		conceptManager.close();
		
		FacesUtil.setSessionMapValue(Parameter.SELECTED_CONCEPT, selectedConceptWrapper);	
		
		return "success";
	}
	
	@PostConstruct
	public void init() {
		Object selected = FacesUtil.getSessionMapValue(Parameter.SELECTED_CONCEPT);
		if (selected != null)
			selectedConceptWrapper = (ConceptEntryWrapper) selected;
	}

	public ConceptEntryWrapper getSelectedWordnetConcept() {
		return selectedWordnetConcept;
	}

	public void setSelectedWordnetConcept(ConceptEntryWrapper selectedWordnetConcept) {
		this.selectedWordnetConcept = selectedWordnetConcept;
	}

	public ConceptEntryWrapper getSelectedConceptWrapper() {
		return selectedConceptWrapper;
	}

	public void setSelectedConceptWrapper(ConceptEntryWrapper selectedConceptWrapper) {
		this.selectedConceptWrapper = selectedConceptWrapper;
	}

	
}
