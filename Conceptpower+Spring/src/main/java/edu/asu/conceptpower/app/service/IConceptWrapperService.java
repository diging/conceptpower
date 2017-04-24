package edu.asu.conceptpower.app.service;

import java.util.Map;

public interface IConceptWrapperService {

    /**
     * This method fetches all the concept types within the Conceptpower system
     * and returns a map containing the concept type id as the key and concept
     * type name as the value.
     * 
     * @return
     */
    public Map<String, String> fetchAllConceptTypes();

    /**
     * This method fetches all the concept lists within the Conceptpower system
     * and returns a map containing the concept list name as the key and concept
     * list name as the value as well.
     * 
     * @return
     */
    public Map<String, String> fetchAllConceptLists();
}
