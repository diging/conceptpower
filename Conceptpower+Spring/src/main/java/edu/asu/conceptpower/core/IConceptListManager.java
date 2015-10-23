package edu.asu.conceptpower.core;

import java.util.List;

import edu.asu.conceptpower.exceptions.DictionaryExistsException;

public interface IConceptListManager {

	/**
	 * Adds a new concept list to the database.
	 * @param name Name of the new concept list
	 * @param description Description of the new concept list.
	 * @throws DictionaryExistsException
	 */
	public abstract void addConceptList(String name, String description)
			throws DictionaryExistsException;

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
	 * Retrieve the list with the specified name.
	 * @param name Name of the concept list to be retrieved.
	 * @return Concept list or null.
	 */
	public abstract ConceptList getConceptList(String name);

	/**
	 * Updates the concept list with the specified name with the information
	 * in the passed {@link ConceptList}.
	 * @param list {@link ConceptList} object that holds the new information.
	 * @param listname Current/old name of the list that should be updated.
	 */
	public abstract void storeModifiedConceptList(ConceptList list,
			String listname);

}