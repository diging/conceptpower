package edu.asu.conceptpower.web;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import edu.asu.conceptpower.core.ConceptType;
import edu.asu.conceptpower.core.ConceptTypesManager;
import edu.asu.conceptpower.db4o.DBNames;
import edu.asu.conceptpower.db4o.DatabaseManager;
import edu.asu.conceptpower.web.util.FacesUtil;
import edu.asu.conceptpower.web.wrapper.ConceptTypeWrapper;

@ManagedBean
@ViewScoped
public class TypesView implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5383848562962721885L;

	private List<ConceptTypeWrapper> datamodel;
	
	private ConceptType selectedList;
	private String selectedTypeForEdit;
	private int page;
	
	@PostConstruct
	public void init() {
		DatabaseManager manager = getDatabaseController().getDatabaseProvider()
		.getDatabaseManager(DBNames.TYPES_DB);

		ConceptTypesManager typesManager;
		try {
			typesManager = new ConceptTypesManager(manager.getClient());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
		datamodel = new ArrayList<ConceptTypeWrapper>();
		
		for (ConceptType type : typesManager.getAllTypes()) {
			ConceptTypeWrapper wrapper = new ConceptTypeWrapper();
			wrapper.setType(type);
			if (type.getSupertypeId() != null && !type.getSupertypeId().isEmpty()) {
				ConceptType supert = typesManager.getType(type.getSupertypeId().trim());
				if (supert != null)
					wrapper.setSupertype(supert);
			}
			
			datamodel.add(wrapper);
		}
		
		Collections.sort(datamodel, new Comparator<ConceptTypeWrapper>() {

			public int compare(ConceptTypeWrapper o1, ConceptTypeWrapper o2) {
				return o1.getType().getTypeName().compareTo(o2.getType().getTypeName());				
			}
		});
			
		typesManager.close();
	}
	
	public String selectForEdit() {
		Iterator<ConceptTypeWrapper> typeIT = datamodel.iterator();
		while (typeIT.hasNext()) {
			ConceptTypeWrapper w = typeIT.next();
			if (w.getType().getTypeId().equals(selectedTypeForEdit)) {
				ConceptType selected = w.getType();
				FacesUtil.setSessionMapValue(Parameter.SELECTED_TYPE, selected);			
			}
		}
		
		return "editType";
	}

	public void setDatamodel(List<ConceptTypeWrapper> datamodel) {
		this.datamodel = datamodel;
	}

	public List<ConceptTypeWrapper> getDatamodel() {
		return datamodel;
	}

	public void setSelectedList(ConceptType selectedList) {
		this.selectedList = selectedList;
	}

	public ConceptType getSelectedList() {
		return selectedList;
	}

	public void setSelectedTypeForEdit(String selectedTypeForEdit) {
		this.selectedTypeForEdit = selectedTypeForEdit;
	}

	public String getSelectedTypeForEdit() {
		return selectedTypeForEdit;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPage() {
		return page;
	}
	
	protected DatabaseController getDatabaseController() {
		ELContext context = FacesContext.getCurrentInstance().getELContext(); 
		
		DatabaseController provider = (DatabaseController) context.getELResolver().getValue(context, null, "databaseController");
		return provider;
	}
}
