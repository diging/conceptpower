package edu.asu.conceptpower.app.json;

import java.util.List;

import edu.asu.conceptpower.app.xml.Pagination;

/**
 * This class contains list of concept entries message and the pagination
 * details. In future we can add other utility classes similar to pagination and
 * frame the final message using this class.
 * 
 * @author karthikeyanmohan
 *
 */
public class ConceptMessagePagination {

    private List<ConceptMessage> conceptEntries;
    private Pagination pagination;

    public List<ConceptMessage> getConceptEntries() {
        return conceptEntries;
    }

    public void setConceptEntries(List<ConceptMessage> conceptEntries) {
        this.conceptEntries = conceptEntries;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }
}
