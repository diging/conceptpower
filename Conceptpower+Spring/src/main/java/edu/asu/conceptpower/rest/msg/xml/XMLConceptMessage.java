package edu.asu.conceptpower.rest.msg.xml;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.validation.ObjectError;

import edu.asu.conceptpower.app.util.URIHelper;
import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptType;
import edu.asu.conceptpower.rest.msg.IConceptMessage;
import edu.asu.conceptpower.rest.msg.Pagination;
import edu.asu.conceptpower.servlet.core.ChangeEvent;

/**
 * This class is used to build XML messages that are sent back as responses to
 * the client. The classes collections concepts and transforms them into XML.
 * 
 * @author Julia Damerow
 *
 */
public class XMLConceptMessage implements IConceptMessage {

    private URIHelper uriCreator;

    public XMLConceptMessage(URIHelper uriCreator) {
        this.uriCreator = uriCreator;
    }

    /**
     * This method fetches all the concept entries with the specified pagination
     * details. If pagination is not specified then only the concept entries
     * messages are returned.
     */
    @Override
    public String getAllConceptEntriesAndPaginationDetails(Map<ConceptEntry, ConceptType> entries,
            Pagination pagination) {
        StringBuffer sb = new StringBuffer("");
        sb.append("<" + XMLConstants.CONCEPTPOWER_ANSWER + " xmlns:" + XMLConstants.NAMESPACE_PREFIX + "=\""
                + XMLConstants.NAMESPACE + "\">");
        sb.append(getAllConceptEntries(entries));
        if (pagination != null) {
            sb.append(appendPaginationDetails(pagination.getTotalNumberOfRecords(), pagination.getPageNumber(), pagination.getTotalNumberofPages()));
        }
        sb.append("</" + XMLConstants.CONCEPTPOWER_ANSWER + ">");
        return sb.toString();
    }

    private String getAllConceptEntries(Map<ConceptEntry, ConceptType> entries) {
        if (entries == null || entries.isEmpty()) {
            return getErrorMessage("No concept entry found.");
        }
        StringBuilder xmlEntries = new StringBuilder();
        for (ConceptEntry entry : entries.keySet())
            xmlEntries.append(getConceptMessage(entry, entries.get(entry)));
        return xmlEntries.toString();
    }

    private String getConceptMessage(ConceptEntry entry, ConceptType type) {
        StringBuffer sb = new StringBuffer();

        sb.append("<" + XMLConstants.NAMESPACE_PREFIX + ":" + XMLConstants.CONCEPT_ENTRY + ">");

        // id
        sb.append("<" + XMLConstants.NAMESPACE_PREFIX + ":" + XMLConstants.ID + " ");
        sb.append(XMLConstants.CONCEPT_ID + "=\"" + entry.getId() + "\" ");
        sb.append(XMLConstants.CONCEPT_URI + "=\"" + uriCreator.getURI(entry) + "\"");
        sb.append(">");
        sb.append(uriCreator.getURI(entry));
        sb.append("</" + XMLConstants.NAMESPACE_PREFIX + ":" + XMLConstants.ID + ">");

        // lemma
        sb.append("<" + XMLConstants.NAMESPACE_PREFIX + ":" + XMLConstants.LEMMA + ">");
        sb.append(StringEscapeUtils.escapeXml10(entry.getWord()));
        sb.append("</" + XMLConstants.NAMESPACE_PREFIX + ":" + XMLConstants.LEMMA + ">");

        // pos
        sb.append("<" + XMLConstants.NAMESPACE_PREFIX + ":" + XMLConstants.POS + ">");
        sb.append(entry.getPos());
        sb.append("</" + XMLConstants.NAMESPACE_PREFIX + ":" + XMLConstants.POS + ">");

        // description
        sb.append("<" + XMLConstants.NAMESPACE_PREFIX + ":" + XMLConstants.DESCRIPTION + ">");
        sb.append(StringEscapeUtils.escapeXml10(entry.getDescription()));
        sb.append("</" + XMLConstants.NAMESPACE_PREFIX + ":" + XMLConstants.DESCRIPTION + ">");

        // concept list
        sb.append("<" + XMLConstants.NAMESPACE_PREFIX + ":" + XMLConstants.CONCEPT_LIST + ">");
        sb.append(StringEscapeUtils.escapeXml10(entry.getConceptList()));
        sb.append("</" + XMLConstants.NAMESPACE_PREFIX + ":" + XMLConstants.CONCEPT_LIST + ">");

        // creator id
        if (entry.getChangeEvents() != null && !entry.getChangeEvents().isEmpty()) {
            List<ChangeEvent> changeEvents = entry.getChangeEvents();
            Collections.sort(changeEvents);
            sb.append("<" + XMLConstants.NAMESPACE_PREFIX + ":" + XMLConstants.CREATOR_ID + ">");
            sb.append(StringEscapeUtils.escapeXml10(
                    changeEvents.get(0).getUserName() != null ? changeEvents.get(0).getUserName().trim() : ""));
            sb.append("</" + XMLConstants.NAMESPACE_PREFIX + ":" + XMLConstants.CREATOR_ID + ">");
        } else {
            // creator id
            sb.append("<" + XMLConstants.NAMESPACE_PREFIX + ":" + XMLConstants.CREATOR_ID + ">");
            sb.append(StringEscapeUtils.escapeXml10(entry.getCreatorId() != null ? entry.getCreatorId().trim() : ""));
            sb.append("</" + XMLConstants.NAMESPACE_PREFIX + ":" + XMLConstants.CREATOR_ID + ">");
        }

        // equal to
        sb.append("<" + XMLConstants.NAMESPACE_PREFIX + ":" + XMLConstants.EQUAL_TO + ">");
        sb.append(StringEscapeUtils.escapeXml10(entry.getEqualTo() != null ? entry.getEqualTo().trim() : ""));
        sb.append("</" + XMLConstants.NAMESPACE_PREFIX + ":" + XMLConstants.EQUAL_TO + ">");

        // modified by
        sb.append("<" + XMLConstants.NAMESPACE_PREFIX + ":" + XMLConstants.MODIFIED_BY + ">");
        sb.append(StringEscapeUtils.escapeXml10(entry.getModified() != null ? entry.getModified().trim() : ""));
        sb.append("</" + XMLConstants.NAMESPACE_PREFIX + ":" + XMLConstants.MODIFIED_BY + ">");

        // similar to
        sb.append("<" + XMLConstants.NAMESPACE_PREFIX + ":" + XMLConstants.SIMILAR_TO + ">");
        sb.append(StringEscapeUtils.escapeXml10(entry.getSimilarTo() != null ? entry.getSimilarTo().trim() : ""));
        sb.append("</" + XMLConstants.NAMESPACE_PREFIX + ":" + XMLConstants.SIMILAR_TO + ">");

        // synonym ids
        sb.append("<" + XMLConstants.NAMESPACE_PREFIX + ":" + XMLConstants.SYNONYM_IDS + ">");
        sb.append(StringEscapeUtils.escapeXml10(entry.getSynonymIds() != null ? entry.getSynonymIds().trim() : ""));
        sb.append("</" + XMLConstants.NAMESPACE_PREFIX + ":" + XMLConstants.SYNONYM_IDS + ">");

        // type
        sb.append("<" + XMLConstants.NAMESPACE_PREFIX + ":" + XMLConstants.TYPE + " ");
        if (type != null) {
            sb.append(XMLConstants.TYPE_ID_ATTR + "=\"" + type.getTypeId() + "\" ");
            sb.append(XMLConstants.TYPE_URI_ATTR + "=\"" + uriCreator.getTypeURI(type) + "\"");
        }
        sb.append(">");
        if (type != null)
            sb.append(StringEscapeUtils.escapeXml10(type.getTypeName()));
        sb.append("</" + XMLConstants.NAMESPACE_PREFIX + ":" + XMLConstants.TYPE + ">");

        // is deleted
        sb.append("<" + XMLConstants.NAMESPACE_PREFIX + ":" + XMLConstants.IS_DELETED + ">");
        sb.append(entry.isDeleted() + "");
        sb.append("</" + XMLConstants.NAMESPACE_PREFIX + ":" + XMLConstants.IS_DELETED + ">");

        // wordnet id
        sb.append("<" + XMLConstants.NAMESPACE_PREFIX + ":" + XMLConstants.WORDNET_ID + ">");
        sb.append(StringEscapeUtils.escapeXml10(entry.getWordnetId() != null ? entry.getWordnetId().trim() : ""));
        sb.append("</" + XMLConstants.NAMESPACE_PREFIX + ":" + XMLConstants.WORDNET_ID + ">");

        // Adding alternative ids and their corresponding uris
        if (entry.getAlternativeIds() != null && !entry.getAlternativeIds().isEmpty()) {
            Map<String, String> uriMap = uriCreator.getUrisBasedOnIds(entry.getAlternativeIds());
            if (uriMap != null && !uriMap.isEmpty()) {
                sb.append("<" + XMLConstants.NAMESPACE_PREFIX + ":" + XMLConstants.ALTERNATIVE_IDS + ">");
                for (Map.Entry<String, String> uri : uriMap.entrySet()) {
                    sb.append("<" + XMLConstants.NAMESPACE_PREFIX + ":" + XMLConstants.ID + " ");
                    sb.append(XMLConstants.CONCEPT_ID + "=\"" + uri.getKey() + "\" ");
                    sb.append(XMLConstants.CONCEPT_URI + "=\"" + uri.getValue() + "\"");
                    sb.append(">");
                    sb.append(uri.getValue());
                    sb.append("</" + XMLConstants.NAMESPACE_PREFIX + ":" + XMLConstants.ID + ">");
                }
                sb.append("</" + XMLConstants.NAMESPACE_PREFIX + ":" + XMLConstants.ALTERNATIVE_IDS + ">");
            }

        }

        // Adding merged ids and their corresponding uris.
        if (entry.getMergedIds() != null && !entry.getMergedIds().isEmpty()) {
            sb.append("<" + XMLConstants.NAMESPACE_PREFIX + ":" + XMLConstants.MERGED_IDS + ">");
            String[] mergedIds = entry.getMergedIds().split(",");
            Map<String, String> uriMap = uriCreator.getUrisBasedOnIds(new HashSet<String>(Arrays.asList(mergedIds)));

            for (Map.Entry<String, String> uri : uriMap.entrySet()) {
                sb.append("<" + XMLConstants.NAMESPACE_PREFIX + ":" + XMLConstants.ID + " ");
                sb.append(XMLConstants.CONCEPT_ID + "=\"" + uri.getKey() + "\" ");
                sb.append(XMLConstants.CONCEPT_URI + "=\"" + uri.getValue() + "\"");
                sb.append(">");
                sb.append(uri.getValue());
                sb.append("</" + XMLConstants.NAMESPACE_PREFIX + ":" + XMLConstants.ID + ">");
            }

            sb.append("</" + XMLConstants.NAMESPACE_PREFIX + ":" + XMLConstants.MERGED_IDS + ">");
        }

        sb.append("</" + XMLConstants.NAMESPACE_PREFIX + ":" + XMLConstants.CONCEPT_ENTRY + ">");

        return sb.toString();
    }

    @Override
    public String getErrorMessages(List<ObjectError> errors) {

        StringBuilder sb = new StringBuilder();
        sb.append("<" + XMLConstants.CONCEPTPOWER_ANSWER + " xmlns:" + XMLConstants.NAMESPACE_PREFIX + "=\""
                + XMLConstants.NAMESPACE + "\">");
        for (ObjectError error : errors) {
            sb.append("<" + XMLConstants.NAMESPACE_PREFIX + ":" + XMLConstants.ERROR + " ");
            sb.append(XMLConstants.ERROR_MESSAGE + "=\"" + error.getDefaultMessage() + "\" ");
            sb.append(">");
            sb.append("</" + XMLConstants.NAMESPACE_PREFIX + ":" + XMLConstants.ERROR + ">");
        }
        sb.append("</" + XMLConstants.CONCEPTPOWER_ANSWER + ">");
        return sb.toString();

    }

    @Override
    public String getErrorMessage(String errorMessage) {
        StringBuilder sb = new StringBuilder();
        sb.append("<" + XMLConstants.CONCEPTPOWER_ANSWER + " xmlns:" + XMLConstants.NAMESPACE_PREFIX + "=\""
                + XMLConstants.NAMESPACE + "\">");
        sb.append("<" + XMLConstants.NAMESPACE_PREFIX + ":" + XMLConstants.ERROR + " ");
        sb.append(XMLConstants.ERROR_MESSAGE + "=\"" + errorMessage + "\" ");
        sb.append(">");
        sb.append("</" + XMLConstants.NAMESPACE_PREFIX + ":" + XMLConstants.ERROR + ">");
        sb.append("</" + XMLConstants.CONCEPTPOWER_ANSWER + ">");
        return sb.toString();
    }

    private String appendPaginationDetails(int numberOfRecords, int pageNumber, int numberOfPages) {
        StringBuilder sb = new StringBuilder();
        sb.append("<" + XMLConstants.NAMESPACE_PREFIX + ":" + XMLConstants.PAGINATION + " ");
        sb.append(XMLConstants.NUMBER_OF_RECORDS + "=\"" + numberOfRecords + "\" ");
        sb.append(XMLConstants.PAGE_NUMBER + "=\"" + pageNumber + "\" ");
        sb.append(XMLConstants.NUMBER_OF_PAGES + "=\"" + numberOfPages + "\" ");
        sb.append(">");
        sb.append("</" + XMLConstants.NAMESPACE_PREFIX + ":" + XMLConstants.PAGINATION + ">");
        return sb.toString();
    }
}
