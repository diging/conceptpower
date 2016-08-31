package edu.asu.conceptpower.servlet.db.objectdb;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptList;
import edu.asu.conceptpower.servlet.db.IDatabaseManager;
import edu.asu.conceptpower.servlet.db4o.IConceptDBManager;

public class ConceptDBManager implements IConceptDBManager {

	@Autowired
	private IDatabaseManager dbManager;

	@Override
	public ConceptEntry getEntry(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Object> queryByExample(Object example) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ConceptEntry[] getEntriesByFieldContains(String field,
			String containsString) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ConceptEntry[] getEntriesForWord(String word, String pos) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ConceptEntry[] getSynonymsPointingToId(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ConceptEntry[] getEntriesForWord(String word) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ConceptList getConceptList(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<?> getAllElementsOfType(Class<?> clazz) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ConceptEntry> getAllEntriesFromList(String listname) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void store(Object element, String databasename) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(ConceptEntry entry, String databasename) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteConceptList(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(ConceptList list, String listname, String databasename) {
		// TODO Auto-generated method stub
		
	}

    public List<ConceptEntry> getConceptByWordnetId(String wordnetId) {
        return null;
    }
	
	
}
