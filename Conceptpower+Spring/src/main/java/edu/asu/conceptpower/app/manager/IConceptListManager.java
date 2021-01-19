package edu.asu.conceptpower.app.manager;

import java.util.List;

import edu.asu.conceptpower.app.exceptions.DictionaryExistsException;
import edu.asu.conceptpower.app.model.ConceptList;

public interface IConceptListManager {

	/**
	 * Adds a new concept list to the database.
	 * @param name Name of the new concept list
	 * @param description Description of the new concept list.
	 * @throws DictionaryExistsException
	 */
	public abstract void addConceptList(String name, String description);

	/**
	 * Deletes the dictionary with the given name from the database.
	 * @param name Name of the dictionary to delete.
	 */
	public abstract void deleteConceptList(String name);

	/**
	 * Get all concept lists from the database.
	 * @return List of all concept lists.
	 */
	public abstract List<ConceptList> getAllConceptLists();

	/**
	 * Retrieve the list with the specified id.
	 * @param name id of the concept list to be retrieved.
	 * @return Concept list or null.
	 */
	public abstract ConceptList getConceptListById(String id);


	/**
	 * Updates the concept list with the specified name with the information
	 * in the passed {@link ConceptList}.
	 * @param list {@link ConceptList} object that holds the new information.
	 */
	public abstract void storeModifiedConceptList(ConceptList list);

}