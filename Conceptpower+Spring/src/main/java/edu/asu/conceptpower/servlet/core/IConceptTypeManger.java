package edu.asu.conceptpower.servlet.core;

import edu.asu.conceptpower.core.ConceptType;

/**
 * This class is the manager class for concept types. It provides methods
 * for adding, updating, deleting, and retrieving concepts.
 * 
 * @author jdamerow
 *
 */
public interface IConceptTypeManger {

	public void addConceptType(ConceptType type);

	public void storeModifiedConceptType(ConceptType type);

	public ConceptType[] getAllTypes();

	public ConceptType getType(String id);

	public void deleteType(String id);

}