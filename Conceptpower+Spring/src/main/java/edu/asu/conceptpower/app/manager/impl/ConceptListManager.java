package edu.asu.conceptpower.app.manager.impl;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.conceptpower.app.manager.IConceptDBManager;
import edu.asu.conceptpower.app.manager.IConceptListManager;
import edu.asu.conceptpower.app.model.ConceptList;

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
	    client.storeConceptList(dict);
	}
	
	/* (non-Javadoc)
	 * @see edu.asu.conceptpower.core.impl.IConceptListManager#deleteConceptList(java.lang.String)
	 */
	@Override
	public void deleteConceptList(String id) {
	    client.deleteConceptList(id);
	}
	
	/* (non-Javadoc)
	 * @see edu.asu.conceptpower.core.impl.IConceptListManager#getAllConceptLists()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<ConceptList> getAllConceptLists() {
	    List<ConceptList> results = client.getAllConceptLists();
	    if (results != null && !results.isEmpty()) {
	        return results;
	    }
	    
	    return Collections.emptyList();
	}

	/* (non-Javadoc)
	 * @see edu.asu.conceptpower.core.impl.IConceptListManager#getConceptList(java.lang.String)
	 */
	@Override
	public ConceptList getConceptList(String name) {
	    return client.getConceptList(name);
	}

	/* (non-Javadoc)
	 * @see edu.asu.conceptpower.core.impl.IConceptListManager#getConceptList(java.lang.String)
	 */
	@Override
	public ConceptList getConceptListById(String id) {
	    return client.getConceptListById(id);
	}
	
	/* (non-Javadoc)
	 * @see edu.asu.conceptpower.core.impl.IConceptListManager#storeModifiedConceptList(edu.asu.conceptpower.core.ConceptList, java.lang.String)
	 */
	@Override
	public void storeModifiedConceptList(ConceptList list) {
	    client.update(list);
	}
	
	
	/*
	 * =================================================================
	 * Protected/Private methods
	 * =================================================================
	 */
	protected String generateId(String prefix) {
	    String id = prefix + UUID.randomUUID().toString();
	    while (true) {
	        if (!client.checkIfConceptListExists(id)){
	            return id;
	        }
	        // try other id 
	        id = prefix + UUID.randomUUID().toString();
		}
	}
}
