package edu.asu.conceptpower.app.core;

import java.util.List;

import edu.asu.conceptpower.app.model.ConceptType;

/**
 * This class is the manager class for concept types. It provides methods for
 * adding, updating, deleting, and retrieving concepts.
 * 
 * @author Keerthivasan Krishnamurthy
 *
 */
public interface IConceptTypeService {

    public void addConceptType(ConceptType type);

    public void storeModifiedConceptType(ConceptType type);

    public List<ConceptType> getAllTypes();

    /**
     * This method retrieves a type by the provided type ID. Returns null if
     * there is not such type.
     * 
     * @param id
     *            ID of the type to be retrieved.
     * @return The corresponding type or null if there is no type for th ID.
     */
    public ConceptType getType(String id);

    public void deleteType(String id);

    /**
     * Returns the total number of pages based on the default page size.
     * 
     * @return
     */
    public int getPageCount();

    /**
     * Returns the concept types based on the page number, page size, sortBy
     * column and sort direction.
     * 
     * @param pageNo
     * @param pageSize
     * @param sortBy
     * @param sortDirection
     * @return
     * @throws NoSuchFieldException
     * @throws SecurityException
     */
    public List<ConceptType> getConceptTypes(int pageNo, int pageSize, String sortBy, int sortDirection)
            throws NoSuchFieldException, SecurityException;

}
