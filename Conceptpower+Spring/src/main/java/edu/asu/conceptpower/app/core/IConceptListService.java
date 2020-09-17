package edu.asu.conceptpower.app.core;

import java.util.List;

import edu.asu.conceptpower.app.core.model.impl.ConceptList;
import edu.asu.conceptpower.app.exceptions.DictionaryExistsException;

/*
 * Service to manage Concept List logics and transactions
 * 
 * @author Keerthivasan Krishnamurthy
 * 
 * */

public interface IConceptListService {

    /**
     * Adds a new concept list to the database.
     * @param name Name of the new concept list
     * @param description Description of the new concept list.
     * @throws DictionaryExistsException
     */
    public void addConceptList(String name, String description);

    /**
     * Deletes the dictionary with the given name from the database.
     * @param name Name of the dictionary to delete.
     */
    public void deleteConceptList(String name);

    /**
     * Get all concept lists from the database.
     * @return List of all concept lists.
     */
    public List<ConceptList> getAllConceptLists();

    /**
     * Retrieve the list with the specified name.
     * @param name Name of the concept list to be retrieved.
     * @return Concept list or null.
     */
    public ConceptList getConceptList(String name);

    /**
     * Updates the concept list with the specified name with the information
     * in the passed {@link ConceptList}.
     * @param list {@link ConceptList} object that holds the new information.
     * @param listname Current/old name of the list that should be updated.
     */
    public void storeModifiedConceptList(ConceptList list, String listname);

    /**
     * Check if the conceptList exists with the same name.
     * 
     * @return boolean true if conceptListName exists.
     */
    public boolean checkExistingConceptList(String name);
}
