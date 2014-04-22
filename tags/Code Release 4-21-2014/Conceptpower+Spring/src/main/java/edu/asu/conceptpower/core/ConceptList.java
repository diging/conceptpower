package edu.asu.conceptpower.core;

import java.io.Serializable;

/**
 * This class describes a concept list in Conceptpower. 
 * A concept list groups concept entries for organization
 * purposes.
 * 
 * @author Julia Damerow
 *
 */
public class ConceptList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2619043575898005700L;

	private String conceptListName;
	
	private String description;
	
	private String id;
	
	/**
	 * Return the name of a concept list.
	 * @return
	 */
	public String getConceptListName() {
		return conceptListName;
	}

	public void setConceptListName(String conceptName) {
		this.conceptListName = conceptName;
	}

	/**
	 * Returns a description of a concept list.
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Returns the id of a concept list.
	 */
	public String getId() {
		return id;
	}
	
	
}
