package edu.asu.conceptpower.core;

import java.io.Serializable;

public class ConceptList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2619043575898005700L;

	private String conceptListName;
	
	private String description;
	
	private String id;
	
	public String getConceptListName() {
		return conceptListName;
	}

	public void setConceptListName(String conceptName) {
		this.conceptListName = conceptName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
	
	
}
