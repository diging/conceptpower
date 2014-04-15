package edu.asu.conceptpower.core;

import java.io.Serializable;

public class ConceptType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2354156550263013826L;
	
	private String typeId;
	private String typeName;
	private String description;
	private String matches;
	private String creatorId;
	private String modified;
	private String supertypeId;
	
	public ConceptType() {
		
	}
	
	public ConceptType(String id) {
		this.typeId = id;
	}
	
	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getMatches() {
		return matches;
	}
	public void setMatches(String matches) {
		this.matches = matches;
	}

	@Override
	public boolean equals(Object arg0) {
		if (!(arg0 instanceof ConceptType))
			return false;
		
		return ((ConceptType)arg0).getTypeId().equals(getTypeId());
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public String getCreatorId() {
		return creatorId;
	}

	public void setModified(String modified) {
		this.modified = modified;
	}

	public String getModified() {
		return modified;
	}

	public void setSupertypeId(String supertypeId) {
		this.supertypeId = supertypeId;
	}

	public String getSupertypeId() {
		return supertypeId;
	}
	
	
}
