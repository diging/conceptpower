package edu.asu.conceptpower.app.core.impl;

import org.springframework.stereotype.Service;

import edu.asu.conceptpower.app.core.IConceptTypesService;

/**
 * This helper class is used for identifying the concept type based on the
 * concept id.
 * 
 * @author karthikeyanmohan
 *
 */
@Deprecated
@Service
public class ConceptTypesService implements IConceptTypesService {
    
    /**
     * This method returns the concept types based on the id. If id is null then
     * null value is returned by the method.
     * 
     * Concept Type is determined based on the type of id.
     * 
     * If id contains ??, then that concept is a generic wordnet concept.
     * 
     * If id starts with WID, then that concept is a specifc wordnet concept.
     * 
     * If both of the above condition is not met, then the concept is local CCP
     * concept.
     * 
     * @param id
     * @return IdType
     */
    public IdType getConceptTypeByConceptId(String id) {
        if (id == null) {
            return null;
        }
        if (id.contains("??")) {
            return IdType.GENERIC_WORDNET_CONCEPT_ID;
        }
        if (id.startsWith("WID")) {
            return IdType.SPECIFIC_WORDNET_CONCEPT_ID;
        }
        return IdType.LOCAL_CONCEPT_ID;
    }

}
