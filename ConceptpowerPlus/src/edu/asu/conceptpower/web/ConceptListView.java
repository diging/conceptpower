package edu.asu.conceptpower.web;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.model.ListDataModel;

import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptList;
import edu.asu.conceptpower.core.ConceptManager;
import edu.asu.conceptpower.core.ConceptTypesManager;
import edu.asu.conceptpower.db4o.DBNames;
import edu.asu.conceptpower.db4o.DatabaseManager;
import edu.asu.conceptpower.users.UsersManager;
import edu.asu.conceptpower.web.util.FacesUtil;
import edu.asu.conceptpower.web.wrapper.ConceptEntryWrapper;
import edu.asu.conceptpower.web.wrapper.ConceptEntryWrapperCreator;

@ManagedBean
@ViewScoped
public class ConceptListView extends DatabaseBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7832358699757007730L;

	@ManagedProperty("#{userSuperAdminController}")
	private UserSuperAdminController adminController;
	
	@ManagedProperty("#{wordNetConfController}")
	private WordNetConfController configurationController;
	
	private String conceptIdForDelete;
	
	private int page;
	private String selectedConceptForEdit;
	private ConceptEntryWrapper selectedConceptForShow;
	private String selectedConceptIdForShow;
	
	public UserSuperAdminController getAdminController() {
		return adminController;
	}

	public void setAdminController(UserSuperAdminController adminController) {
		this.adminController = adminController;
	}

	private ConceptList conceptList;
	private List<ConceptEntryWrapper> entries;
	private transient ListDataModel<ConceptEntryWrapper> datamodel;


	@PostConstruct
	public void init() {
		//conceptList = listManager.getSelectedList();
		conceptList = null;
		DatabaseController controller = getDatabaseController();
		
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
		
		List<ConceptEntry> founds = null;
		if (conceptList != null)
			founds = conceptManager.getConceptListEntries(conceptList.getConceptListName());
		else if (FacesUtil.getSessionMapValue(Parameter.SELECTED_LIST) != null) {
			Object selectedListObj = FacesUtil.getSessionMapValue(Parameter.SELECTED_LIST);
			
			if (selectedListObj instanceof ConceptList)
				founds = conceptManager.getConceptListEntries(((ConceptList)selectedListObj).getConceptListName());
			else
			{
				founds = conceptManager.getConceptListEntries((String) selectedListObj);				
			}
		}
		
		ConceptTypesManager typeManager;
		try {
			typeManager = new ConceptTypesManager(controller.getDatabaseProvider().getDatabaseManager(DBNames.TYPES_DB).getClient());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
		DatabaseManager userDBManager = controller.getDatabaseProvider().getDatabaseManager(DBNames.USER_DB);

		UsersManager usersManager = new UsersManager(userDBManager);
		usersManager.setAdmins(adminController.getAdmins());
		
		ConceptEntryWrapperCreator wrapperCreator = new ConceptEntryWrapperCreator();
		entries = wrapperCreator.createWrappers(founds != null ? founds.toArray(new ConceptEntry[founds.size()]) : new ConceptEntry[0], usersManager, typeManager, conceptManager);
		
		conceptManager.close();
		typeManager.close();
		usersManager.close();
		
		
	}
	
	public String selectForEdit() {
		for (ConceptEntryWrapper w : entries) {
			if (w.getEntry().getId().equals(selectedConceptForEdit)) {
				ConceptEntry selected = w.getEntry();
				FacesUtil.setSessionMapValue(Parameter.SELECTED_CONCEPT, selected);			
			}
		}
		
		return "editConcept";
	}
	
	public String selectForShowDetails() {
		for (ConceptEntryWrapper w : entries) {
			if (w.getEntry().getId().equals(selectedConceptIdForShow)) {
				FacesUtil.setSessionMapValue(Parameter.SELECTED_CONCEPT, w);	
			}
		}
		
		return "showConceptDetails";
	}
	
	public String selectForDelete() {
		for (ConceptEntryWrapper w : entries) {
			if (w.getEntry().getId().equals(conceptIdForDelete)) {
				FacesUtil.setSessionMapValue(Parameter.DELETE_CONCEPT, w);	
			}
		}
		return "deleteConcept";
	}

	public void setConceptList(ConceptList conceptList) {
		this.conceptList = conceptList;
	}

	public ConceptList getConceptList() {
		return conceptList;
	}
	
	public void setConfigurationController(WordNetConfController configurationController) {
		this.configurationController = configurationController;
	}

	public WordNetConfController getConfigurationController() {
		return configurationController;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPage() {
		return page;
	}

	public void setEntries(List<ConceptEntryWrapper> entries) {
		this.entries = entries;
	}

	public List<ConceptEntryWrapper> getEntries() {
		return entries;
	}

	public void setSelectedConceptForEdit(String selectForEdit) {
		this.selectedConceptForEdit = selectForEdit;
	}

	public String getSelectedConceptForEdit() {
		return selectedConceptForEdit;
	}

	public void setSelectedConceptForShow(ConceptEntryWrapper selectedConceptForShow) {
		this.selectedConceptForShow = selectedConceptForShow;
	}

	public ConceptEntryWrapper getSelectedConceptForShow() {
		return selectedConceptForShow;
	}
	
	public String selectForShow() {
		for (ConceptEntryWrapper w : entries) {
			if (w.getEntry().getId().equals(selectedConceptIdForShow)) {
				selectedConceptForShow = w;
				return "";
			}
		}
		
		return "";
	}

	public void setSelectedConceptIdForShow(String selectedConceptIdForShow) {
		this.selectedConceptIdForShow = selectedConceptIdForShow;
	}

	public String getSelectedConceptIdForShow() {
		return selectedConceptIdForShow;
	}

	public String getConceptIdForDelete() {
		return conceptIdForDelete;
	}

	public void setConceptIdForDelete(String conceptIdForDelete) {
		this.conceptIdForDelete = conceptIdForDelete;
	}

	public ListDataModel<ConceptEntryWrapper> getDatamodel() {
		datamodel = new ListDataModel<ConceptEntryWrapper>();
		((ListDataModel<ConceptEntryWrapper>)datamodel).setWrappedData(entries);
		return datamodel;
	}

	public void setDatamodel(ListDataModel<ConceptEntryWrapper> datamodel) {
		this.datamodel = datamodel;
	}
}
