package edu.asu.conceptpower.app.core.impl;

import org.springframework.stereotype.Service;

import edu.asu.conceptpower.app.core.IConceptTypesService;

/**
 * This helper class is used for identifying the concept types based on the
 * concept id.
 * 
 * @author karthikeyanmohan
 *
 */
@Service
public class ConceptTypesService implements IConceptTypesService {
    
    /**
     * This enum contains the list of valid concept types.
     * 
     * @author karthikeyanmohan
     *
     */
    public enum ConceptTypes {
        GENERIC_WORDNET_CONCEPT, SPECIFIC_WORDNET_CONCEPT, LOCAL_CONCEPT
    }
    
    /**
     * This method returns the concept types based on the id. If id is null then
     * null value is returned by the method.
     * 
     * @param id
     * @return ConceptTypes
     */
    public ConceptTypes getConceptTypeByConceptId(String id) {
        if (id == null) {
            return null;
        }
        if (id.contains("??")) {
            return ConceptTypes.GENERIC_WORDNET_CONCEPT;
        }
        if (id.startsWith("WID")) {
            return ConceptTypes.SPECIFIC_WORDNET_CONCEPT;
        }
        return ConceptTypes.LOCAL_CONCEPT;
    }

}
