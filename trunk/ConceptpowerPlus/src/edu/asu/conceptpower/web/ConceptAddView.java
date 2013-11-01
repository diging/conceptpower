package edu.asu.conceptpower.web;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptList;
import edu.asu.conceptpower.core.ConceptManager;
import edu.asu.conceptpower.core.ConceptType;
import edu.asu.conceptpower.core.ConceptTypesManager;
import edu.asu.conceptpower.core.Constants;
import edu.asu.conceptpower.db4o.DBNames;
import edu.asu.conceptpower.db4o.DatabaseManager;
import edu.asu.conceptpower.db4o.DatabaseProvider;
import edu.asu.conceptpower.exceptions.DictionaryDoesNotExistException;
import edu.asu.conceptpower.exceptions.DictionaryModifyException;
import edu.asu.conceptpower.web.util.FacesUtil;

@ManagedBean
@ViewScoped
public class ConceptAddView implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4233434062795240735L;
	private ConceptEntry conceptEntry;
	private List<ConceptEntry> synonyms;
	private ConceptType type;
	private String searchPhrase;
	private List<ConceptEntry> searchResults;
	private int page = 1;
	private ConceptEntry addedSynonym;
	
	private String synyonymToBeRemoved;
	
	private List<ConceptType> availableTypes;
	
	@ManagedProperty("#{wordNetConfController}")
	private WordNetConfController configurationController;
	
	@ManagedProperty("#{loginController}")
	private LoginController loginController;
	
	
	private List<ConceptList> conceptLists;
	
	public ConceptAddView() {
		
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
	
	protected DatabaseController getDatabaseController() {
		ELContext context = FacesContext.getCurrentInstance().getELContext(); 
		
		DatabaseController provider = (DatabaseController) context.getELResolver().getValue(context, null, "databaseController");
		return provider;
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
		conceptEntry = new ConceptEntry();
		synonyms = new ArrayList<ConceptEntry>();
		availableTypes = new ArrayList<ConceptType>();
		conceptLists = new ArrayList<ConceptList>();
		
		ConceptManager conceptManager = getConceptManager();
		
		setConceptLists(conceptManager.getAllConceptLists());
		
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

	public List<ConceptEntry> autocomplete(String sugg) {
		List<ConceptEntry> founds = new ArrayList<ConceptEntry>();
		
		DatabaseProvider provider = getDatabaseController().getDatabaseProvider();
		DatabaseManager manager = provider.getDatabaseManager(DBNames.WORDNET_CACHE);
		
		ConceptManager conceptManager;
		try {
			conceptManager = new ConceptManager(manager, provider.getDatabaseManager(DBNames.DICTIONARY_DB),
					configurationController.getWordNetConfiguration());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return founds;
		}
		ConceptEntry[] entries = conceptManager.getConceptListEntriesForWord(sugg.trim());
		conceptManager.close();
		
		if (entries != null)
			for (ConceptEntry e : entries)
				founds.add(e);
		return founds;
	}
	
	public String createConcept() {
		
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
		
		conceptEntry.setCreatorId(loginController.getUser().getUser());
		
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
		conceptEntry.setCreatorId(userId);
		
		ConceptList list = conceptManager.getConceptList(conceptList);
		
		FacesUtil.setSessionMapValue(Parameter.SELECTED_LIST, list);
		
		try {
			conceptManager.addConceptListEntry(conceptEntry);
		} catch (DictionaryDoesNotExistException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "failed";
		} catch (DictionaryModifyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "failed";
		} finally {
			conceptManager.close();
		}
		
		return "success";
	}
	
	public List<ConceptEntry> getSynonyms() {
		return synonyms;
	}
	
	public void setSynonyms(List<ConceptEntry> s) {
		
		synonyms = s;
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

	public void setSynyonymToBeRemoved(String synyonymToBeRemoved) {
		this.synyonymToBeRemoved = synyonymToBeRemoved;
	}

	public String getSynyonymToBeRemoved() {
		return synyonymToBeRemoved;
	}

	
}
