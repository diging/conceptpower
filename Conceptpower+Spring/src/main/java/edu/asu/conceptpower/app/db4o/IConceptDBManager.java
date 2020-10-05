package edu.asu.conceptpower.app.db4o;

import java.util.List;

import edu.asu.conceptpower.app.model.ConceptEntry;
import edu.asu.conceptpower.core.ConceptList;

public interface IConceptDBManager {

    public static final int ASCENDING = 1;
    public static final int DESCENDING = -1;

	public abstract ConceptEntry getEntry(String id);

	public abstract List<Object> queryByExample(Object example);

	public abstract ConceptEntry[] getEntriesByFieldContains(String field,
			String containsString);

	public abstract ConceptEntry[] getEntriesForWord(String word, String pos);

	public abstract ConceptEntry[] getSynonymsPointingToId(String id);

	public abstract ConceptEntry[] getEntriesForWord(String word);

	public abstract ConceptList getConceptList(String name);

	/**
	 * Get all objects of a certain type from both databases
	 * 
	 * @param clazz
	 * @return
	 */
	public abstract List<?> getAllElementsOfType(Class<?> clazz);

    public abstract List<ConceptEntry> getAllEntriesFromList(String conceptList, int pageNo, int pageSize,
            String sortBy, int sortDirection);

	public abstract void store(Object element, String databasename);

	public abstract void update(ConceptEntry entry, String databasename);

	public abstract void deleteConceptList(String name);

	public abstract void update(ConceptList list, String listname,
			String databasename);

    public List<ConceptEntry> getAllEntriesFromList(String listname);

    public List<ConceptEntry> getAllEntriesByTypeId(String typeId);

    public List<ConceptEntry> getWrapperEntryByWordnetId(String wordnetId);
    
    public List<ConceptEntry> getAllConcepts();

}