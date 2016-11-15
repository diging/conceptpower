package edu.asu.conceptpower.app.xml;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;

import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptType;

public interface IConceptMessage {

    public String getAllConceptMessage(Map<ConceptEntry, ConceptType> entries) throws JsonProcessingException;

}

