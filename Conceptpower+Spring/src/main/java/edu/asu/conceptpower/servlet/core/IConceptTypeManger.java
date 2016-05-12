package edu.asu.conceptpower.servlet.core;

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

	/**
     * This method retrieves a type by the provided type ID. Returns null if there
     * is not such type.
     * @param id ID of the type to be retrieved.
     * @return The corresponding type or null if there is no type for th ID.
     */
	public ConceptType getType(String id);

	public void deleteType(String id);

}