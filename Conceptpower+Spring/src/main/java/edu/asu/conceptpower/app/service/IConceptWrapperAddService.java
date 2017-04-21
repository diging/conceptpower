package edu.asu.conceptpower.app.service;

import java.util.Map;

public interface IConceptWrapperAddService {

    public Map<String, String> fetchAllConceptTypes();

    public Map<String, String> fetchAllConceptLists();
}
