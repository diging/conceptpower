package edu.asu.conceptpower.wrapper;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;

import edu.asu.conceptpower.core.ConceptType;
import edu.asu.conceptpower.util.URICreator;

/**
 * 
 * This class describes the concept type wrapper in the concept power. It
 * provides properties which are not available for the concept types to be
 * wrapped
 * 
 * @author Julia Damerow
 * 
 */
public class ConceptTypeWrapper implements Serializable {

	private static final long serialVersionUID = -402125365605646049L;
	private ConceptType type;
	private ConceptType supertype;

	@Autowired
	URICreator URICreator;

	public void setType(ConceptType type) {
		this.type = type;
	}

	public ConceptType getType() {
		return type;
	}

	public void setSupertype(ConceptType supertype) {
		this.supertype = supertype;
	}

	public ConceptType getSupertype() {
		return supertype;
	}

	public String getTypeURI() {
		return URICreator.getTypeURI(type);
	}

}
