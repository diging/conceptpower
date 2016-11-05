package edu.asu.conceptpower.servlet.xml;

import java.util.Map;

import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptType;

public interface IConceptMessage {

    public String getAllConceptMessage(Map<ConceptEntry, ConceptType> entries);

    public String getConceptMessage(ConceptEntry entry, ConceptType type);

}

