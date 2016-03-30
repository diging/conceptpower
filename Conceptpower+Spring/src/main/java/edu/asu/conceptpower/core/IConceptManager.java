package edu.asu.conceptpower.core;

import java.util.List;
import java.util.Map;

import edu.asu.conceptpower.exceptions.DictionaryDoesNotExistException;
import edu.asu.conceptpower.exceptions.DictionaryEntryExistsException;
import edu.asu.conceptpower.exceptions.DictionaryModifyException;

public interface IConceptManager {

	/**
	 * Return entry given its ID. First the additional concepts are queried,
	 * then WordNet.
	 * 
	 * @param id
	 *            of entry
	 * @return Entry for ID or null.
	 */
	public abstract ConceptEntry getConceptEntry(String id);

	/**
	 * Get an entry from WordNet given its WordNet ID.
	 * 
	 * @param wordnetId
	 *            Id of concept in WordNet.
	 * @return concept for ID or null
	 */
	public abstract ConceptEntry getWordnetConceptEntry(String wordnetId);

	/**
	 * Get all entries for a word and its POS.
	 * 
	 * @param word
	 *            Word looked for. Additional concepts will be queried with
	 *            "contains", WordNet concepts will be queried using
	 *            "equals exactly".
	 * @param pos
	 *            Part of speech of word (noun, verb, adjective, adverb).
	 * @return matching concepts
	 */
	public abstract ConceptEntry[] getConceptListEntriesForWord(String word,
			String pos);

	/**
	 * Searches in all additional concepts for in the given fields for the given
	 * values. The results for each field/value pair are joined with "or".
	 * 
	 * @param fieldMap
	 *            map of field/value pairs
	 * @return matching concepts
	 */
	public abstract ConceptEntry[] searchForConceptsConnectedByOr(
			Map<String, String> fieldMap);

	/**
	 * Searches in all additional concepts for in the given fields for the given
	 * values. The results for each field/value pair are joined with "and".
	 * 
	 * @param fieldMap
	 *            map of field/value pairs
	 * @return matching concepts
	 */
	public abstract ConceptEntry[] searchForConceptsConnectedByAnd(
			Map<String, String> fieldMap);

	/**
	 * Given a concept id this method returns an array of concepts that are
	 * listed as synonyms for the concept with the given id.
	 * 
	 * @param id
	 *            The id of the concept that synonyms should be retrieved for.
	 * @return An array of concept entries that are synonyms for the concept
	 *         with the given id. This method will never return null only filled
	 *         or empty arrays.
	 */
	public abstract ConceptEntry[] getSynonymsForConcept(String id);

	/**
	 * This method returns an array of concept entries that have words that
	 * contain the given word. E.g. if the given word is "horse" this method
	 * would return concepts such as "horse" or "horseback riding".
	 * 
	 * @param word
	 *            The word that should be contained in the word field of a
	 *            concept.
	 * @return An array list with matching concepts. This method never returns
	 *         null only empty or filled arrays.
	 */
	public abstract ConceptEntry[] getConceptListEntriesForWord(String word);

	public abstract List<ConceptEntry> getConceptListEntries(String conceptList);

	public abstract void addConceptListEntry(String word, String pos,
			String description, String conceptListName, String typeId,
			String equalTo, String similarTo, String synonymIds,
			String synsetIds, String narrows, String broadens)
			throws DictionaryDoesNotExistException, DictionaryModifyException,
			DictionaryEntryExistsException;

	/**
	 * Stores the given entry in the database and return the new id
	 * that was generated for the entry.
	 * @param entry the {@link ConceptEntry} to be stored.
	 * @return Id of stored entry.
	 * @throws DictionaryDoesNotExistException If the entry has a concept list 
	 * specified that doesn't exist.
	 * @throws DictionaryModifyException If the specified concept list in the
	 * concept is the Wordnet list. 
	 */
	public abstract String addConceptListEntry(ConceptEntry entry, String userName)
			throws DictionaryDoesNotExistException, DictionaryModifyException;

	public abstract void storeModifiedConcept(ConceptEntry entry, String userName);
	
	public abstract void deleteConcept(String id, String userName);
	
}