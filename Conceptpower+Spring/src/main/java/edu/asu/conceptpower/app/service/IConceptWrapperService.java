package edu.asu.conceptpower.app.service;

import java.util.Map;

public interface IConceptWrapperService {

    /**
     * This method fetches all the concept types within the Conceptpower system
     * and returns a map containing the concept type id as the key and concept
     * type name as the value.
     * 
     * If there are no ConceptTypes in the Conceptpower system, then an empty
     * map is returned.
     * 
     * @return
     */
    public Map<String, String> fetchAllConceptTypes();

    /**
     * This method fetches all the concept lists within the Conceptpower system
     * and returns a map containing the concept list name as the key and concept
     * list name as the value as well.
     *
     * If there are no ConceptLists in the Conceptpower system, then an empty
     * map is returned.
     * 
     * @return
     */
    public Map<String, String> fetchAllConceptLists();
}
