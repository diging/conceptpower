package edu.asu.conceptpower.app.xml;

import com.fasterxml.jackson.core.JsonProcessingException;

import edu.asu.conceptpower.core.ConceptType;

public interface ITypeMessage {

    public String getConceptTypeMessage(ConceptType type, ConceptType supertype) throws JsonProcessingException;

}
