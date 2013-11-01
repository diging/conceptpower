package edu.asu.conceptpower.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import edu.asu.conceptpower.core.ConceptList;
import edu.asu.conceptpower.core.ConceptManager;
import edu.asu.conceptpower.db4o.DBNames;
import edu.asu.conceptpower.db4o.DatabaseManager;
import edu.asu.conceptpower.web.util.FacesUtil;

@ManagedBean
@RequestScoped
public class ConceptListManager {

	@ManagedProperty("#{wordNetConfController}")
	private WordNetConfController configurationController;
	
	@ManagedProperty("#{databaseController}")
	private DatabaseController controller;
	
	private List<ConceptList> conceptLists;
	
	private DataModel<ConceptList> datamodel;
	
	private ConceptList selectedList;
	
	public DataModel<ConceptList> getDatamodel() {
		return datamodel;
	}

	public void setDatamodel(DataModel<ConceptList> datamodel) {
		this.datamodel = datamodel;
	}

	@PostConstruct
	public void init() {
		DatabaseManager manager = controller.getDatabaseProvider()
		.getDatabaseManager(DBNames.WORDNET_CACHE);

		ConceptManager conceptManager;
		try {
			conceptManager = new ConceptManager(manager, controller.getDatabaseProvider().getDatabaseManager(DBNames.DICTIONARY_DB),
					configurationController.getWordNetConfiguration());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
		datamodel = new ListDataModel<ConceptList>();
		((ListDataModel<ConceptList>)datamodel).setWrappedData(conceptManager.getAllConceptLists());
		conceptManager.close();
	}

	public String selectConceptList() {
		selectedList = datamodel.getRowData();
		FacesUtil.setSessionMapValue(Parameter.SELECTED_LIST, selectedList);
		return "viewList";
	}
	
	public void setController(DatabaseController controller) {
		this.controller = controller;
	}

	public DatabaseController getController() {
		return controller;
	}

	public void setConfigurationController(WordNetConfController configurationController) {
		this.configurationController = configurationController;
	}

	public WordNetConfController getConfigurationController() {
		return configurationController;
	}

	public List<ConceptList> getConceptLists() {
		return conceptLists;
	}

	public void setConceptLists(List<ConceptList> conceptLists) {
		this.conceptLists = conceptLists;
	}
	
	public void setSelectedList(ConceptList list) {
		this.selectedList = list;
	}

	public ConceptList getSelectedList() {
		return selectedList;
	}
}
