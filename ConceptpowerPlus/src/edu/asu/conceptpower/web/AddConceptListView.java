package edu.asu.conceptpower.web;

import java.io.IOException;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import edu.asu.conceptpower.core.ConceptManager;
import edu.asu.conceptpower.db4o.DBNames;
import edu.asu.conceptpower.db4o.DatabaseManager;
import edu.asu.conceptpower.exceptions.DictionaryExistsException;

@ManagedBean
@RequestScoped
public class AddConceptListView {

	private String name;
	private String description;
	
	@ManagedProperty("#{wordNetConfController}")
	private WordNetConfController configurationController;
	
	@ManagedProperty("#{databaseController}")
	private DatabaseController databaseController;
	
	
	public String createList() {
		DatabaseManager manager = databaseController.getDatabaseProvider()
		.getDatabaseManager(DBNames.WORDNET_CACHE);
		
		ConceptManager conceptManager;
		try {
			conceptManager = new ConceptManager(manager, databaseController.getDatabaseProvider().getDatabaseManager(DBNames.DICTIONARY_DB),
					configurationController.getWordNetConfiguration());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "failure";
		}
		
		try {
			conceptManager.addConceptList(name, description);
		} catch (DictionaryExistsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "failure";
		} finally {
			conceptManager.close();
		}
		
		return "success";
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDescription() {
		return description;
	}

	public void setConfigurationController(WordNetConfController configurationController) {
		this.configurationController = configurationController;
	}

	public WordNetConfController getConfigurationController() {
		return configurationController;
	}

	public void setDatabaseController(DatabaseController databaseController) {
		this.databaseController = databaseController;
	}

	public DatabaseController getDatabaseController() {
		return databaseController;
	}
}
