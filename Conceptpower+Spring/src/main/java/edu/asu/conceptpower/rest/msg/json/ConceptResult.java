package edu.asu.conceptpower.rest.msg.json;

import java.util.List;

import edu.asu.conceptpower.rest.msg.Pagination;

/**
 * This class will be serialized through Jackson for Json objects. This class
 * will contain all the objects that need to be serialized. Currently, apart
 * from ConceptMessage we have Pagination object, but in future if we introduce
 * any new utilities that needs to be added to JSON, we can create that object
 * and add it to the ConceptResult class.
 * 
 * @author karthikeyanmohan
 *
 */
public class ConceptResult {

    private List<ConceptEntryMessage> conceptEntries;
    private Pagination pagination;

    public List<ConceptEntryMessage> getConceptEntries() {
        return conceptEntries;
    }

    public void setConceptEntries(List<ConceptEntryMessage> conceptEntries) {
        this.conceptEntries = conceptEntries;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }
}
