package edu.asu.conceptpower.servlet.core.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.conceptpower.core.ConceptList;
import edu.asu.conceptpower.servlet.core.IConceptListManager;
import edu.asu.conceptpower.servlet.db4o.DBNames;
import edu.asu.conceptpower.servlet.db4o.IConceptDBManager;

/**
 * Manager class for concept lists.
 * 
 * Note: Concept lists are currently identfied by their names. This needs to be
 * changed.
 * 
 * @author jdamerow
 *
 */
@Service
public class ConceptListManager implements IConceptListManager {
	
	protected final String LIST_PREFIX = "LIST";
	
	@Autowired
	private IConceptDBManager client;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.asu.conceptpower.core.impl.IConceptListManager#addConceptList(java.
	 * lang.String, java.lang.String)
	 */
	@Override
	public void addConceptList(String name, String description) {

		ConceptList dict = new ConceptList();
		dict.setConceptListName(name);
		dict.setDescription(description);
		dict.setId(generateId(LIST_PREFIX));
		client.store(dict, DBNames.DICTIONARY_DB);
	}
	
	/* (non-Javadoc)
	 * @see edu.asu.conceptpower.core.impl.IConceptListManager#deleteConceptList(java.lang.String)
	 */
	@Override
	public void deleteConceptList(String name) {
		client.deleteConceptList(name);
	}
	
	/* (non-Javadoc)
	 * @see edu.asu.conceptpower.core.impl.IConceptListManager#getAllConceptLists()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<ConceptList> getAllConceptLists() {
		List<?> results = client.getAllElementsOfType(ConceptList.class);
		if (results != null)
			return (List<ConceptList>) results;
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.asu.conceptpower.core.impl.IConceptListManager#getConceptList(java.lang.String)
	 */
	@Override
	public ConceptList getConceptList(String name) {
		return client.getConceptList(name);
	}
	
	/* (non-Javadoc)
	 * @see edu.asu.conceptpower.core.impl.IConceptListManager#storeModifiedConceptList(edu.asu.conceptpower.core.ConceptList, java.lang.String)
	 */
	@Override
	public void storeModifiedConceptList(ConceptList list, String listname) {
		client.update(list, listname, DBNames.DICTIONARY_DB);
	}
	
	
	/*
	 * =================================================================
	 * Protected/Private methods
	 * =================================================================
	 */
	protected String generateId(String prefix) {
		String id = prefix + UUID.randomUUID().toString();

		while (true) {
			ConceptList example = null;
			
			example = new ConceptList();
			example.setId(id);
			
			// if there doesn't exist an object with this id return id
			List<Object> results = client.queryByExample(example);
			if (results == null || results.size() == 0)
				return id;

			// try other id
			id = prefix + UUID.randomUUID().toString();
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.asu.conceptpower.core.impl.IConceptListManager#
	 * checkExistingConceptList(java. lang.String, java.lang.String)
	 */
	@Override
	public boolean checkExistingConceptList(String name) {
		ConceptList dict = client.getConceptList(name);
		if (dict != null) {
			return true;
		}
		return false;
	}
}
