package edu.asu.conceptpower.servlet.util;

import edu.asu.conceptpower.servlet.core.ConceptEntry;
import edu.asu.conceptpower.servlet.core.ConceptType;

public interface IURIHelper {
    public String getURI(ConceptEntry entry);
    public String getTypeURI(ConceptType type);
    public String getTypeId(String typeUriOrId);
    
}
