package edu.asu.conceptpower.app.manager;

import java.util.List;

import edu.asu.conceptpower.app.model.ConceptEntry;
import edu.asu.conceptpower.app.model.ConceptList;
import edu.asu.conceptpower.app.model.ReviewStatus;

public interface IConceptDBManager {

    public static final int ASCENDING = 1;
    public static final int DESCENDING = -1;

	public abstract ConceptEntry getEntry(String id);

	public abstract ConceptEntry[] getEntriesByFieldContains(String field,
			String containsString);

	public abstract ConceptEntry[] getEntriesForWord(String word, String pos);

	public abstract ConceptEntry[] getSynonymsPointingToId(String id);

	public abstract ConceptEntry[] getEntriesForWord(String word);

	public abstract ConceptList getConceptList(String name);
	
	public abstract ConceptList getConceptListById(String id);
	
	public abstract List<ConceptEntry> getAllEntriesFromList(String conceptList, int pageNo, int pageSize,
            String sortBy, int sortDirection);

	public abstract void store(ConceptEntry element);

	public abstract void update(ConceptEntry entry);
	
	public abstract void deleteConceptList(String id);
	
	public abstract void update(ConceptList list);
	
	public List<ConceptEntry> getAllEntriesFromList(String listname);
	
	public List<ConceptEntry> getAllEntriesByTypeId(String typeId);
	
	public List<ConceptEntry> getWrapperEntryByWordnetId(String wordnetId);
	
	public Iterable<ConceptEntry> getAllConcepts();
	
	public List<ConceptList> getAllConceptLists();
	
	public void storeConceptList(ConceptList element);
	
	public boolean checkIfConceptListExists(String id);
	
	public List<ConceptEntry> getConceptsByStatus(ReviewStatus status, Integer page, Integer pageSize);
	
	public Integer getNumberOfConceptsByStatus(ReviewStatus status);

}