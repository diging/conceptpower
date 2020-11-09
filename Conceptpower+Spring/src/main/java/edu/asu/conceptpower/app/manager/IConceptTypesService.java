package edu.asu.conceptpower.app.manager;


public interface IConceptTypesService {
    
    /**
     * This enum contains the list of valid concept types.
     * 
     * @author karthikeyanmohan
     */
    public enum IdType {
        GENERIC_WORDNET_CONCEPT_ID, SPECIFIC_WORDNET_CONCEPT_ID, LOCAL_CONCEPT_ID
    }

    /**
     * This method returns the concept type based on the id parameter.
     * 
     * The different IdTypes that are returned by this method are
     * 
     * 1) GENERIC_WORDNET_CONCEPT_ID -> This id usually contains '??'.
     * 
     * 2) SPECIFIC_WORDNET_CONCEPT_ID -> This id usually starts with WID and
     * does not contain any ??.
     * 
     * 3) LOCAL_CONCEPT_ID -> This is usually the concept id local to concept
     * power.
     * 
     * @param id
     * @return IdType
     */
    public IdType getConceptTypeByConceptId(String id);
}
