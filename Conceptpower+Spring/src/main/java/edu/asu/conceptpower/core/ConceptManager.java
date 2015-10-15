package edu.asu.conceptpower.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.conceptpower.db4o.DBNames;
import edu.asu.conceptpower.db4o.IConceptDBManager;
import edu.asu.conceptpower.exceptions.DictionaryDoesNotExistException;
import edu.asu.conceptpower.exceptions.DictionaryEntryExistsException;
import edu.asu.conceptpower.exceptions.DictionaryExistsException;
import edu.asu.conceptpower.exceptions.DictionaryModifyException;
import edu.asu.conceptpower.wordnet.Constants;
import edu.asu.conceptpower.wordnet.WordNetManager;

/**
 * This class handles concepts in general. It uses a DB4O client that contains
 * all added concepts and concept wrappers, and a WordNet client that queries
 * WordNet.
 * 
 * @author Julia Damerow
 * 
 */
@Service
public class ConceptManager implements IConceptManager {

	@Autowired
	private WordNetManager wordnetManager;

	@Autowired
	private IConceptDBManager client;

	protected final String CONCEPT_PREFIX = "CON";
	protected final String LIST_PREFIX = "LIST";

	/* (non-Javadoc)
	 * @see edu.asu.conceptpower.core.IConceptManager#getConceptEntry(java.lang.String)
	 */
	@Override
	public ConceptEntry getConceptEntry(String id) {
		ConceptEntry entry = client.getEntry(id);
		if (entry != null) {
			fillConceptEntry(entry);
			return entry;
		}

		entry = wordnetManager.getConcept(id);
		if (entry != null) {
			// client.store(entry, DBNames.WORDNET_CACHE);
			return entry;
		}

		return null;
	}

	/* (non-Javadoc)
	 * @see edu.asu.conceptpower.core.IConceptManager#getWordnetConceptEntry(java.lang.String)
	 */
	@Override
	public ConceptEntry getWordnetConceptEntry(String wordnetId) {
		ConceptEntry entry = wordnetManager.getConcept(wordnetId);
		return entry;
	}

	/* (non-Javadoc)
	 * @see edu.asu.conceptpower.core.IConceptManager#getConceptListEntriesForWord(java.lang.String, java.lang.String)
	 */
	@Override
	public ConceptEntry[] getConceptListEntriesForWord(String word, String pos) {
		ConceptEntry[] entries = client.getEntriesForWord(word, pos);

		List<ConceptEntry> allEntries = new ArrayList<ConceptEntry>();
		for (ConceptEntry entry : entries) {
			fillConceptEntry(entry);
			allEntries.add(entry);
		}

		entries = wordnetManager.getEntriesForWord(word, pos);
		if (entries != null) {
			for (ConceptEntry entry : entries) {

				ConceptEntry foundEntry = getConceptEntry(entry.getId());
				if (entry.getId().equals(foundEntry.getId()))
					allEntries.add(entry);
			}
		}

		List<ConceptEntry> entriesNotDeleted = new ArrayList<ConceptEntry>();
		for (ConceptEntry entry : allEntries) {
			if (!entry.isDeleted())
				entriesNotDeleted.add(entry);
		}

		return entriesNotDeleted.toArray(new ConceptEntry[entriesNotDeleted
				.size()]);
	}

	/**
	 * If a concept entry does not wrap a concept from Wordnet then this method
	 * does nothing. If a concept does wrap concepts from Wordnet (id and
	 * wordnet id are not the same) then this method copies the synonym ids of
	 * the wrapped wordnet concepts into the synonym ids field of the wrapping
	 * concept entry.
	 * 
	 * @param entry
	 *            The concept entry that should be filled with the synonym ids
	 *            of wrapped wordnet concepts.
	 */
	protected void fillConceptEntry(ConceptEntry entry) {
		if (entry.getId() != null && entry.getWordnetId() != null
				&& !entry.getId().equals(entry.getWordnetId())) {
			// generate the synonym ids
			StringBuffer sb = new StringBuffer();

			if (entry.getSynonymIds() != null) {
				String wordnetIds = (entry.getWordnetId() != null ? entry
						.getWordnetId() : "");
				String[] ids = wordnetIds.trim().split(
						Constants.CONCEPT_SEPARATOR);
				if (ids != null) {
					for (String id : ids) {
						if (id != null && !id.trim().isEmpty()) {
							ConceptEntry wordnetEntry = wordnetManager
									.getConcept(id);
							if (wordnetEntry != null) {
								sb.append(wordnetEntry.getSynonymIds());
							}
						}
					}
				}
			}

			entry.setSynonymIds(entry.getSynonymIds()
					+ Constants.CONCEPT_SEPARATOR + sb.toString());
		}
	}

	/* (non-Javadoc)
	 * @see edu.asu.conceptpower.core.IConceptManager#searchForConceptsConnectedByOr(java.util.Map)
	 */
	@Override
	public ConceptEntry[] searchForConceptsConnectedByOr(
			Map<String, String> fieldMap) {

		List<ConceptEntry> results = new ArrayList<ConceptEntry>();
		List<String> ids = new ArrayList<String>();

		for (String fieldName : fieldMap.keySet()) {
			String searchString = fieldMap.get(fieldName);

			ConceptEntry[] entries = client.getEntriesByFieldContains(
					fieldName, searchString);

			for (ConceptEntry e : entries) {
				if (e != null) {
					if (!ids.contains(e.getId())) {
						fillConceptEntry(e);
						results.add(e);
						ids.add(e.getId());
					}
				}
			}
		}

		return results.toArray(new ConceptEntry[results.size()]);
	}

	/* (non-Javadoc)
	 * @see edu.asu.conceptpower.core.IConceptManager#searchForConceptsConnectedByAnd(java.util.Map)
	 */
	@Override
	public ConceptEntry[] searchForConceptsConnectedByAnd(
			Map<String, String> fieldMap) {

		List<ConceptEntry> results = null;
		List<String> ids = new ArrayList<String>();

		for (String fieldName : fieldMap.keySet()) {
			String searchString = fieldMap.get(fieldName);

			ConceptEntry[] entries = client.getEntriesByFieldContains(
					fieldName, searchString);

			if (results == null) {
				results = new ArrayList<ConceptEntry>();
				for (ConceptEntry e : entries) {
					if (e != null) {
						fillConceptEntry(e);
						results.add(e);
						ids.add(e.getId());
					}
				}
				continue;
			}

			List<ConceptEntry> filteredResults = new ArrayList<ConceptEntry>();
			for (ConceptEntry e : entries) {
				if (e != null) {
					if (ids.contains(e.getId()))
						filteredResults.add(e);
				}
			}
			results = filteredResults;
		}

		return results.toArray(new ConceptEntry[results.size()]);
	}

	/* (non-Javadoc)
	 * @see edu.asu.conceptpower.core.IConceptManager#getSynonymsForConcept(java.lang.String)
	 */
	@Override
	public ConceptEntry[] getSynonymsForConcept(String id) {

		ConceptEntry concept = getConceptEntry(id);
		List<ConceptEntry> allEntries = new ArrayList<ConceptEntry>();
		if (concept == null)
			return allEntries.toArray(new ConceptEntry[allEntries.size()]);

		fillConceptEntry(concept);

		List<String> ids = new ArrayList<String>();

		String synonymIds = concept.getSynonymIds();
		if (synonymIds != null && !synonymIds.isEmpty()) {
			String[] synIds = synonymIds.split(",");

			for (String synId : synIds) {
				ConceptEntry synonym = getConceptEntry(synId);
				if (synonym != null && !ids.contains(synId)) {
					allEntries.add(synonym);
					ids.add(synId);
				}
			}
		}

		ConceptEntry[] entries = client.getSynonymsPointingToId(id);

		for (ConceptEntry entry : entries) {
			fillConceptEntry(entry);
			if (!ids.contains(entry.getId())) {
				allEntries.add(entry);
				ids.add(entry.getId());
			}
		}

		return allEntries.toArray(new ConceptEntry[allEntries.size()]);
	}

	/* (non-Javadoc)
	 * @see edu.asu.conceptpower.core.IConceptManager#getConceptListEntriesForWord(java.lang.String)
	 */
	@Override
	public ConceptEntry[] getConceptListEntriesForWord(String word) {
		ConceptEntry[] entries = client.getEntriesForWord(word);

		List<ConceptEntry> allEntries = new ArrayList<ConceptEntry>();
		for (ConceptEntry entry : entries) {
			fillConceptEntry(entry);
			allEntries.add(entry);
		}

		entries = wordnetManager.getEntriesForWord(word);
		if (entries != null) {
			for (ConceptEntry entry : entries) {
				// client.store(entry, DBNames.WORDNET_CACHE);
				allEntries.add(entry);
			}
		}

		return allEntries.toArray(new ConceptEntry[allEntries.size()]);
	}

	/* (non-Javadoc)
	 * @see edu.asu.conceptpower.core.IConceptManager#getConceptListEntries(java.lang.String)
	 */
	@Override
	public List<ConceptEntry> getConceptListEntries(String conceptList) {
		List<ConceptEntry> entries = client.getAllEntriesFromList(conceptList);
		Collections.sort(entries, new Comparator<ConceptEntry>() {

			public int compare(ConceptEntry o1, ConceptEntry o2) {
				if (o1.getWord() == o2.getWord()) {
					return 0;
				} else if (o1.getWord() == null) {
					return -1;
				} else if (o2.getWord() == null) {
					return 1;
				} else {
					return o1.getWord().compareToIgnoreCase(o2.getWord());
				}
			}

		});
		List<ConceptEntry> notDeletedEntries = new ArrayList<ConceptEntry>();
		for (ConceptEntry entry : entries) {
			if (!entry.isDeleted()) {
				fillConceptEntry(entry);
				notDeletedEntries.add(entry);
			}
		}
		return notDeletedEntries;
	}

	/* (non-Javadoc)
	 * @see edu.asu.conceptpower.core.IConceptManager#getConceptList(java.lang.String)
	 */
	@Override
	public ConceptList getConceptList(String name) {
		return client.getConceptList(name);
	}

	/* (non-Javadoc)
	 * @see edu.asu.conceptpower.core.IConceptManager#addConceptList(java.lang.String, java.lang.String)
	 */
	@Override
	public void addConceptList(String name, String description)
			throws DictionaryExistsException {
		if (name.equals(Constants.WORDNET_DICTIONARY))
			throw new DictionaryExistsException();

		ConceptList dict = client.getConceptList(name);
		if (dict != null)
			throw new DictionaryExistsException();

		dict = new ConceptList();
		dict.setConceptListName(name);
		dict.setDescription(description);
		dict.setId(generateId(CONCEPT_LIST, LIST_PREFIX));
		client.store(dict, DBNames.DICTIONARY_DB);
	}

	/* (non-Javadoc)
	 * @see edu.asu.conceptpower.core.IConceptManager#addConceptListEntry(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void addConceptListEntry(String word, String pos,
			String description, String conceptListName, String typeId,
			String equalTo, String similarTo, String synonymIds,
			String synsetIds, String narrows, String broadens)
			throws DictionaryDoesNotExistException, DictionaryModifyException,
			DictionaryEntryExistsException {
		ConceptList dict = client.getConceptList(conceptListName);
		if (dict == null)
			throw new DictionaryDoesNotExistException();

		if (conceptListName.equals(Constants.WORDNET_DICTIONARY)) {
			throw new DictionaryModifyException();
		}

		ConceptEntry entry = new ConceptEntry();
		entry.setWord(word);
		entry.setDescription(description);
		entry.setPos(pos);
		entry.setConceptList(conceptListName);
		entry.setTypeId(typeId);
		entry.setEqualTo(equalTo);
		entry.setSimilarTo(similarTo);
		entry.setSynonymIds(synonymIds);
		entry.setSynsetIds(synsetIds);
		entry.setNarrows(narrows);
		entry.setBroadens(broadens);

		entry.setId(generateId(CONCEPT_ENTRY, CONCEPT_PREFIX));

		client.store(entry, DBNames.DICTIONARY_DB);
	}

	/* (non-Javadoc)
	 * @see edu.asu.conceptpower.core.IConceptManager#addConceptListEntry(edu.asu.conceptpower.core.ConceptEntry)
	 */
	@Override
	public String addConceptListEntry(ConceptEntry entry)
			throws DictionaryDoesNotExistException, DictionaryModifyException {
		ConceptList dict = client.getConceptList(entry.getConceptList());
		if (dict == null)
			throw new DictionaryDoesNotExistException();

		if (entry.getConceptList().equals(Constants.WORDNET_DICTIONARY)) {
			throw new DictionaryModifyException();
		}
		
		String id = generateId(CONCEPT_ENTRY, CONCEPT_PREFIX);
		entry.setId(id);

		client.store(entry, DBNames.DICTIONARY_DB);
		return id;
	}

	/* (non-Javadoc)
	 * @see edu.asu.conceptpower.core.IConceptManager#storeModifiedConcept(edu.asu.conceptpower.core.ConceptEntry)
	 */
	@Override
	public void storeModifiedConcept(ConceptEntry entry) {
		client.update(entry, DBNames.DICTIONARY_DB);
	}

	/* (non-Javadoc)
	 * @see edu.asu.conceptpower.core.IConceptManager#storeModifiedConceptList(edu.asu.conceptpower.core.ConceptList, java.lang.String)
	 */
	@Override
	public void storeModifiedConceptList(ConceptList list, String listname) {
		client.update(list, listname, DBNames.DICTIONARY_DB);
	}

	/* (non-Javadoc)
	 * @see edu.asu.conceptpower.core.IConceptManager#getAllConceptLists()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<ConceptList> getAllConceptLists() {
		List<?> results = client.getAllElementsOfType(ConceptList.class);
		if (results != null)
			return (List<ConceptList>) results;
		return null;
	}

	protected String generateId(int type, String prefix) {
		String id = prefix + UUID.randomUUID().toString();

		while (true) {
			Object example = null;
			if (type == CONCEPT_ENTRY) {
				example = new ConceptEntry();
				((ConceptEntry) example).setId(id);
			} else if (type == CONCEPT_LIST) {
				example = new ConceptList();
				((ConceptList) example).setId(id);
			}
			// if there doesn't exist an object with this id return id
			List<Object> results = client.queryByExample(example);
			if (results == null || results.size() == 0)
				return id;

			// try other id
			id = prefix + UUID.randomUUID().toString();
		}
	}

	/* (non-Javadoc)
	 * @see edu.asu.conceptpower.core.IConceptManager#deleteConceptList(java.lang.String)
	 */
	@Override
	public void deleteConceptList(String name) {
		client.deleteConceptList(name);
	}
}
