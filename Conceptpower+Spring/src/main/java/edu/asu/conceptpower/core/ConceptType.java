package edu.asu.conceptpower.core;

import java.io.Serializable;
/** 
 * @deprecated
 *      This class is only kept for migration purposes.
 * @author abhishekkumar
 *
 */
@Deprecated
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

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((typeId == null) ? 0 : typeId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ConceptType other = (ConceptType) obj;
		if (typeId == null) {
			if (other.typeId != null)
				return false;
		} else if (!typeId.equals(other.typeId))
			return false;
		return true;
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