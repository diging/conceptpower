package edu.asu.conceptpower.web;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptList;
import edu.asu.conceptpower.core.ConceptManager;
import edu.asu.conceptpower.core.ConceptType;
import edu.asu.conceptpower.core.ConceptTypesManager;
import edu.asu.conceptpower.core.Constants;
import edu.asu.conceptpower.db4o.DBNames;
import edu.asu.conceptpower.db4o.DatabaseManager;
import edu.asu.conceptpower.db4o.DatabaseProvider;
import edu.asu.conceptpower.web.util.FacesUtil;

@ManagedBean
@ViewScoped
public class EditConceptView extends DatabaseBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4233434062795240735L;
	private ConceptEntry conceptEntry;
	private ConceptType type;
	private String searchPhrase;
	private List<ConceptEntry> searchResults;
	private int page = 1;
	private ConceptEntry addedSynonym;
	private List<ConceptEntry> synonyms;
	
	private String synyonymToBeRemoved;
	
	private List<ConceptType> availableTypes;
	
	@ManagedProperty("#{wordNetConfController}")
	private WordNetConfController configurationController;
	
	@ManagedProperty("#{loginController}")
	private LoginController loginController;
	
	private List<ConceptList> conceptLists;
	
	public EditConceptView() {
		
	}
	
	protected ConceptManager getConceptManager() {
		DatabaseController provider = getDatabaseController();
		
		DatabaseManager manager = provider.getDatabaseProvider()
		.getDatabaseManager(DBNames.WORDNET_CACHE);
		
		ConceptManager conceptManager;
		try {
			conceptManager = new ConceptManager(manager, provider.getDatabaseProvider().getDatabaseManager(DBNames.DICTIONARY_DB),
					configurationController.getWordNetConfiguration());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		return conceptManager;
	}
	
	
	public WordNetConfController getConfigurationController() {
		return configurationController;
	}

	public void setConfigurationController(
			WordNetConfController configurationController) {
		this.configurationController = configurationController;
	}


	@PostConstruct
	public void init() {
		Object selected = FacesUtil.getSessionMapValue(Parameter.SELECTED_CONCEPT);
		if (selected != null)
			conceptEntry = (ConceptEntry) selected;
		
		ConceptManager conceptManager = getConceptManager();
		
		availableTypes = new ArrayList<ConceptType>();
		conceptLists = new ArrayList<ConceptList>();

		setConceptLists(conceptManager.getAllConceptLists());
		
		synonyms = new ArrayList<ConceptEntry>();
		String synonymIds = conceptEntry.getSynonymIds();
		if (synonymIds != null) {
			String[] ids = synonymIds.trim().split(Constants.SYNONYM_SEPARATOR);
			if (ids != null) {
				for (String id : ids) {
					if (id == null || id.isEmpty())
						continue;
					ConceptEntry synonym = conceptManager.getConceptEntry(id);
					if (synonym != null)
						synonyms.add(synonym);
				}
			}
		}
		conceptManager.close();
		
		ConceptTypesManager typeManager;
		try {
			typeManager = new ConceptTypesManager(getDatabaseController().getDatabaseProvider().getDatabaseManager(DBNames.TYPES_DB).getClient());
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
	
	public String editConcept() {
		
		StringBuffer sb = new StringBuffer();
		for (ConceptEntry synonym : synonyms)
			sb.append(synonym.getId() + Constants.SYNONYM_SEPARATOR);
		
		conceptEntry.setSynonymIds(sb.toString());
		
		DatabaseProvider provider = getDatabaseController().getDatabaseProvider();
		
		DatabaseManager manager = provider.getDatabaseManager(DBNames.WORDNET_CACHE);
		
		String conceptList = conceptEntry.getConceptList();
		
		if (loginController.getUser() == null)
				return "failed";
		
		if (loginController.getUser() == null)
			return "failed";
		
		//conceptEntry.setCreatorId(loginController.getUser().getUser());
		
		ConceptManager conceptManager;
		try {
			conceptManager = new ConceptManager(manager, provider.getDatabaseManager(DBNames.DICTIONARY_DB),
					configurationController.getWordNetConfiguration());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "failed";
		}
		
		
		String userId = loginController.getUser().getUser();
		String modified = conceptEntry.getModified() != null ? conceptEntry.getModified(): "";
		if (!modified.trim().isEmpty())
			modified += ", ";
		conceptEntry.setModified(modified + userId + "@" + (new Date()).toString());
		
		ConceptList list = conceptManager.getConceptList(conceptList);
		
		FacesUtil.setSessionMapValue(Parameter.SELECTED_LIST, list);
		
		conceptManager.storeModifiedConcept(conceptEntry);
		conceptManager.close();
		
		return "success";
	}
	
	public String returnToList() {
		FacesUtil.setSessionMapValue(Parameter.SELECTED_LIST, conceptEntry.getConceptList());
		return "viewList";
	}
	
	public String getSynyonymToBeRemoved() {
		return synyonymToBeRemoved;
	}

	public void setSynyonymToBeRemoved(String synyonymToBeRemoved) {
		this.synyonymToBeRemoved = synyonymToBeRemoved;
	}

	public void setConceptEntry(ConceptEntry conceptEntry) {
		this.conceptEntry = conceptEntry;
	}

	public ConceptEntry getConceptEntry() {
		return conceptEntry;
	}

	public void setConceptLists(List<ConceptList> conceptLists) {
		this.conceptLists = conceptLists;
	}

	public List<ConceptList> getConceptLists() {
		return conceptLists;
	}


	public void setType(ConceptType type) {
		this.type = type;
	}


	public ConceptType getType() {
		return type;
	}


	public void setAvailableTypes(List<ConceptType> availableTypes) {
		this.availableTypes = availableTypes;
	}


	public List<ConceptType> getAvailableTypes() {
		return availableTypes;
	}


	public void setSearchPhrase(String searchPhrase) {
		this.searchPhrase = searchPhrase;
	}


	public String getSearchPhrase() {
		return searchPhrase;
	}

	public void searchForSynonyms() {
		ConceptManager conceptManager = getConceptManager();
		if (conceptManager == null)
			return;
		
		ConceptEntry[] entries = conceptManager.getConceptListEntriesForWord(searchPhrase.trim());
		List<ConceptEntry> founds = new ArrayList<ConceptEntry>();
		
		if (entries != null)
			for (ConceptEntry e : entries)
				founds.add(e);
		
		searchResults = founds;
		conceptManager.close();
	}
	
	public void removeSynonym() {
		if (synyonymToBeRemoved != null && !synyonymToBeRemoved.trim().isEmpty()) {
			ConceptEntry entry = null;
			for (ConceptEntry e : synonyms) {
				if (e.getId().endsWith(synyonymToBeRemoved.trim()))
					entry = e;
			}
			
			synonyms.remove(entry);
		}
	}


	public void setSearchResults(List<ConceptEntry> searchResults) {
		this.searchResults = searchResults;
	}


	public List<ConceptEntry> getSearchResults() {
		return searchResults;
	}


	public void setPage(int page) {
		this.page = page;
	}


	public int getPage() {
		return page;
	}
	
	public void addSynonymToList() {
		synonyms.add(addedSynonym);
		addedSynonym = null;
	}


	public void setAddedSynonym(ConceptEntry addedSynonym) {
		this.addedSynonym = addedSynonym;
	}


	public ConceptEntry getAddedSynonym() {
		return addedSynonym;
	}

	public void setLoginController(LoginController loginController) {
		this.loginController = loginController;
	}

	public LoginController getLoginController() {
		return loginController;
	}

	public void setSynonyms(List<ConceptEntry> synonyms) {
		this.synonyms = synonyms;
	}

	public List<ConceptEntry> getSynonyms() {
		return synonyms;
	}

}
