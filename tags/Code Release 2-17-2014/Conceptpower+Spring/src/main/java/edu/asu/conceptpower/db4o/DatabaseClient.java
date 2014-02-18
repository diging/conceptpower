package edu.asu.conceptpower.db4o;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Predicate;

import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptList;
import edu.asu.conceptpower.reflect.SearchField;

@Component
public class DatabaseClient {

	ObjectContainer wordnetCacheClient;
	ObjectContainer dictionaryClient;
	
	@Autowired
	@Qualifier("wordnetCacheDatabaseManager")
	private DatabaseManager wordnetCache;
	
	@Autowired
	@Qualifier("conceptDatabaseManager")
	private DatabaseManager dictionary;
	
	@PostConstruct
	public void init() {
		this.wordnetCacheClient = wordnetCache.getClient();
		this.dictionaryClient = dictionary.getClient();
	}

	public ConceptEntry getEntry(String id) {
		ConceptEntry exampleEntry = new ConceptEntry();

		// check if there is a wrapper for wordnet entry
		exampleEntry.setId(null);
		exampleEntry.setWordnetId(id);

		ConceptEntry[] dictionaryResults = getEntriesByFieldContains("wordnetid", id);
		// ObjectSet<ConceptEntry> results = dictionaryClient
		// .queryByExample(exampleEntry);

		// there should only be exactly one object with this id
		if (dictionaryResults.length == 1)
			return dictionaryResults[0];

		exampleEntry.setId(id);
		exampleEntry.setWordnetId(null);
		/*
		 * check if there is a concept in the wordnet cache
		 */
		ObjectSet<ConceptEntry> results = wordnetCacheClient
				.queryByExample(exampleEntry);

		// there should only be exactly one object with this id
		if (results.size() == 1)
			return results.get(0);

		/*
		 * check if there is a concept with this id
		 */
		results = dictionaryClient.queryByExample(exampleEntry);
		// there should only be exactly one object with this id
		if (results.size() == 1)
			return results.get(0);

		return null;
	}

	public List<Object> queryByExample(Object example) {
		ObjectSet<Object> results = wordnetCacheClient.queryByExample(example);
		ObjectSet<Object> results2 = dictionaryClient.queryByExample(example);

		List<Object> allResults = new ArrayList<Object>();
		for (Object r : results) {
			allResults.add(r);
		}

		for (Object r : results2) {
			allResults.add(r);
		}

		return allResults;

	}

	public ConceptEntry[] getEntriesByFieldContains(String field,
			String containsString) {
		if (containsString == null || field == null)
			return new ConceptEntry[0];
		final String fField = field;
		final String fSearchFor = containsString;

		ObjectSet<ConceptEntry> results = dictionaryClient
				.query(new Predicate<ConceptEntry>() {
					public boolean match(ConceptEntry con) {

						// go through all fields of class to find field that
						// should be searched
						Field[] fields = con.getClass().getDeclaredFields();
						for (Field field : fields) {
							SearchField searchFieldAnnotation = field
									.getAnnotation(SearchField.class);
							if (searchFieldAnnotation != null) {
								// if we found the field that should be search
								// through
								if (searchFieldAnnotation.fieldName().equals(
										fField)) {
									String fieldContent = null;
									// check content
									try {
										field.setAccessible(true);
										Object contentOfField = field.get(con);
										if (contentOfField instanceof String)
											fieldContent = contentOfField
													.toString();
									} catch (IllegalArgumentException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
										continue;
									} catch (IllegalAccessException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
										continue;
									}

									if (fieldContent != null) {
										if (fieldContent
												.trim()
												.toLowerCase()
												.contains(
														fSearchFor.trim()
																.toLowerCase()))
											return true;
									}
									return false;
								}
							}
						}

						return false;
					}
				});

		return results.toArray(new ConceptEntry[results.size()]);
	}

	public ConceptEntry[] getEntriesForWord(String word, String pos) {
		ConceptEntry[] allConcepts = getEntriesForWord(word);

		List<ConceptEntry> entries = new ArrayList<ConceptEntry>();
		for (ConceptEntry entry : allConcepts) {
			if (entry.getPos().toLowerCase().equals(pos.toLowerCase())
					&& !entry.isDeleted())
				entries.add(entry);
		}

		return entries.toArray(new ConceptEntry[entries.size()]);
	}

	public ConceptEntry[] getSynonymsPointingToId(String id) {
		List<ConceptEntry> entries = new ArrayList<ConceptEntry>();
		final String wordId = id;

		ObjectSet<ConceptEntry> results = dictionaryClient
				.query(new Predicate<ConceptEntry>() {
					public boolean match(ConceptEntry con) {
						return con.getSynonymIds().contains(wordId)
								&& !con.isDeleted();
					}
				});

		if (results.size() > 0) {
			entries.addAll(results);
		}

		return entries.toArray(new ConceptEntry[entries.size()]);
	}

	public ConceptEntry[] getEntriesForWord(String word) {

		List<ConceptEntry> entries = new ArrayList<ConceptEntry>();

		// check user build dictionary
		final String fWord = word;
		ObjectSet<ConceptEntry> results = dictionaryClient
				.query(new Predicate<ConceptEntry>() {
					public boolean match(ConceptEntry con) {
						return con.getWord().replace("_", " ").toLowerCase()
								.contains(fWord.toLowerCase())
								&& !con.isDeleted();
					}
				});

		if (results.size() > 0) {
			entries.addAll(results);
		}

		return entries.toArray(new ConceptEntry[entries.size()]);
	}

	@SuppressWarnings("serial")
	public ConceptList getConceptList(String name) {
		final String fName = name;
		List<ConceptList> dicts = dictionaryClient
				.query(new Predicate<ConceptList>() {
					public boolean match(ConceptList dict) {
						return dict.getConceptListName().equals(fName);
					}
				});

		if (dicts.size() == 1)
			return dicts.get(0);
		return null;
	}

	/**
	 * Get all objects of a certain type from both databases
	 * 
	 * @param clazz
	 * @return
	 */
	public List<?> getAllElementsOfType(Class<?> clazz) {
		List<?> results = wordnetCacheClient.query(clazz);
		List<?> dictResults = dictionaryClient.query(clazz);
		List<Object> allResults = new ArrayList<Object>();
		allResults.addAll(results);
		allResults.addAll(dictResults);
		return allResults;
	}

	public List<ConceptEntry> getAllEntriesFromList(String listname) {
		ConceptEntry entry = new ConceptEntry();
		entry.setConceptList(listname);

		List<ConceptEntry> results = wordnetCacheClient.queryByExample(entry);
		List<ConceptEntry> dictResults = dictionaryClient.queryByExample(entry);

		List<ConceptEntry> allResults = new ArrayList<ConceptEntry>();
		allResults.addAll(results);
		allResults.addAll(dictResults);
		return allResults;
	}

	public void store(Object element, String databasename) {
		// FIXME no caching?
		// if (databasename.equals(DBNames.WORDNET_CACHE)) {
		// wordnetCacheClient.store(element);
		// wordnetCacheClient.commit();
		// return;
		// }
		if (databasename.equals(DBNames.DICTIONARY_DB)) {
			dictionaryClient.store(element);
			dictionaryClient.commit();
		}
	}

	public void update(ConceptEntry entry, String databasename) {
		if (databasename.equals(DBNames.DICTIONARY_DB)) {
			ConceptEntry toBeUpdated = getEntry(entry.getId());
			toBeUpdated.setBroadens(entry.getBroadens());
			toBeUpdated.setConceptList(entry.getConceptList());
			toBeUpdated.setDescription(entry.getDescription());
			toBeUpdated.setEqualTo(entry.getEqualTo());
			toBeUpdated.setModified(entry.getModified());
			toBeUpdated.setNarrows(entry.getNarrows());
			toBeUpdated.setPos(entry.getPos());
			toBeUpdated.setSimilarTo(entry.getSimilarTo());
			toBeUpdated.setSynonymIds(entry.getSynonymIds());
			toBeUpdated.setSynsetIds(entry.getSynsetIds());
			toBeUpdated.setTypeId(entry.getTypeId());
			toBeUpdated.setWord(entry.getWord());
			toBeUpdated.setWordnetId(entry.getWordnetId());
			toBeUpdated.setDeleted(entry.isDeleted());
			dictionaryClient.store(toBeUpdated);
			dictionaryClient.commit();
		}
	}
}
