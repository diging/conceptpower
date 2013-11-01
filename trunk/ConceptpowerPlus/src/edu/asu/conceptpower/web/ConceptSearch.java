package edu.asu.conceptpower.web;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.el.ELContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptManager;
import edu.asu.conceptpower.core.ConceptTypesManager;
import edu.asu.conceptpower.db4o.DBNames;
import edu.asu.conceptpower.db4o.DatabaseManager;
import edu.asu.conceptpower.users.UsersManager;
import edu.asu.conceptpower.web.wrapper.ConceptEntryWrapper;
import edu.asu.conceptpower.web.wrapper.ConceptEntryWrapperCreator;

@ManagedBean
@ViewScoped
public class ConceptSearch implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2877411971378037173L;

	private String concept;
	private String pos;
	private List<ConceptEntryWrapper> foundConcepts;
	private ConceptEntryWrapper selectedConcept;
	private String selectedConceptId;

	private Object selection;

	@ManagedProperty("#{wordNetConfController}")
	private WordNetConfController configurationController;

	private List<ConceptEntryWrapper> selectedConcepts;

	public WordNetConfController getConfigurationController() {
		return configurationController;
	}

	public void setConfigurationController(WordNetConfController configuration) {
		this.configurationController = configuration;
	}

	public void setConcept(String concept) {
		this.concept = concept;
	}

	public String getConcept() {
		return concept;
	}

	public void setPos(String pos) {
		this.pos = pos;
	}

	public String getPos() {
		return pos;
	}

	public String search() {

		if (!concept.trim().isEmpty()) {
			ELContext context = FacesContext.getCurrentInstance()
					.getELContext();

			DatabaseController provider = (DatabaseController) context
					.getELResolver().getValue(context, null,
							"databaseController");

			DatabaseManager manager = provider.getDatabaseProvider()
					.getDatabaseManager(DBNames.WORDNET_CACHE);

			ConceptManager dictManager;
			try {
				dictManager = new ConceptManager(manager, provider
						.getDatabaseProvider().getDatabaseManager(
								DBNames.DICTIONARY_DB),
						configurationController.getWordNetConfiguration());
			} catch (IOException e) {
				// log("While initialzing DictionaryManager:",
				// e);
				return null;
			}

			ConceptTypesManager typeManager;
			try {
				typeManager = new ConceptTypesManager(provider
						.getDatabaseProvider()
						.getDatabaseManager(DBNames.TYPES_DB).getClient());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}

			DatabaseManager userDBManager = provider.getDatabaseProvider()
					.getDatabaseManager(DBNames.USER_DB);

			UsersManager usersManager = new UsersManager(userDBManager);

			ConceptEntry[] found = dictManager.getConceptListEntriesForWord(
					concept, pos);

			ConceptEntryWrapperCreator wrapperCreator = new ConceptEntryWrapperCreator();
			foundConcepts = wrapperCreator.createWrappers(found, usersManager,
					typeManager, dictManager);

			usersManager.close();
			typeManager.close();
			dictManager.close();

		}

		return null;
	}

	public void setFoundConcepts(List<ConceptEntryWrapper> concepts) {
		this.foundConcepts = concepts;
	}

	public List<ConceptEntryWrapper> getFoundConcepts() {
		return foundConcepts;
	}

	public void setSelectedConcept(ConceptEntryWrapper selectedConcept) {
		this.selectedConcept = selectedConcept;
	}

	public ConceptEntryWrapper getSelectedConcept() {
		return selectedConcept;
	}

	public String selectForShow() {
		for (ConceptEntryWrapper w : foundConcepts) {
			if (w.getEntry().getId().equals(selectedConceptId)) {
				selectedConcept = w;
				return "";
			}
		}

		return "";
	}

	public void setSelectedConceptId(String selectedConceptId) {
		this.selectedConceptId = selectedConceptId;
	}

	public String getSelectedConceptId() {
		return selectedConceptId;
	}

	public void setSelection(Object selection) {
		this.selection = selection;
	}

	public Object getSelection() {
		return selection;
	}

	public List<ConceptEntryWrapper> selectionChanged() {
		selectedConcepts = new ArrayList<ConceptEntryWrapper>();
		if (selection instanceof HashSet<?>) {
			Iterator<?> it = ((HashSet<?>) selection).iterator();
			while (it.hasNext()) {
				Object selectionIdx = it.next();
				if (selectionIdx instanceof Integer) {
					ConceptEntryWrapper entry = foundConcepts
							.get((Integer) selectionIdx);
					selectedConcepts.add(entry);
				}
			}
		}
		return selectedConcepts;
	}

}
