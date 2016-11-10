package edu.asu.conceptpower.servlet.xml;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;

import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptList;
import edu.asu.conceptpower.core.ConceptType;
import edu.asu.conceptpower.root.URIHelper;
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

    @Override
    public String getAllConceptMessage(Map<ConceptEntry, ConceptType> entries) {
        StringBuilder xmlEntries = new StringBuilder();

        xmlEntries.append("<" + XMLJsonConstants.CONCEPTPOWER_ANSWER + " xmlns:" + XMLJsonConstants.NAMESPACE_PREFIX + "=\""
                + XMLJsonConstants.NAMESPACE + "\">");

        for (ConceptEntry entry : entries.keySet())
            xmlEntries.append(getConceptMessage(entry, entries.get(entry)));

        xmlEntries.append("</" + XMLJsonConstants.CONCEPTPOWER_ANSWER + ">");

        return xmlEntries.toString();
    }

    private String getConceptMessage(ConceptEntry entry, ConceptType type) {
        StringBuffer sb = new StringBuffer();

        sb.append("<" + XMLJsonConstants.NAMESPACE_PREFIX + ":" + XMLJsonConstants.CONCEPT_ENTRY + ">");

        // id
        sb.append("<" + XMLJsonConstants.NAMESPACE_PREFIX + ":" + XMLJsonConstants.ID + " ");
        sb.append(XMLJsonConstants.CONCEPT_ID + "=\"" + entry.getId() + "\" ");
        sb.append(XMLJsonConstants.CONCEPT_URI + "=\"" + uriCreator.getURI(entry) + "\"");
        sb.append(">");
        sb.append(uriCreator.getURI(entry));
        sb.append("</" + XMLJsonConstants.NAMESPACE_PREFIX + ":" + XMLJsonConstants.ID + ">");

        // lemma
        sb.append("<" + XMLJsonConstants.NAMESPACE_PREFIX + ":" + XMLJsonConstants.LEMMA + ">");
        sb.append(StringEscapeUtils.escapeXml10(entry.getWord()));
        sb.append("</" + XMLJsonConstants.NAMESPACE_PREFIX + ":" + XMLJsonConstants.LEMMA + ">");

        // pos
        sb.append("<" + XMLJsonConstants.NAMESPACE_PREFIX + ":" + XMLJsonConstants.POS + ">");
        sb.append(entry.getPos());
        sb.append("</" + XMLJsonConstants.NAMESPACE_PREFIX + ":" + XMLJsonConstants.POS + ">");

        // description
        sb.append("<" + XMLJsonConstants.NAMESPACE_PREFIX + ":" + XMLJsonConstants.DESCRIPTION + ">");
        sb.append(StringEscapeUtils.escapeXml10(entry.getDescription()));
        sb.append("</" + XMLJsonConstants.NAMESPACE_PREFIX + ":" + XMLJsonConstants.DESCRIPTION + ">");

        // concept list
        sb.append("<" + XMLJsonConstants.NAMESPACE_PREFIX + ":" + XMLJsonConstants.CONCEPT_LIST + ">");
        sb.append(StringEscapeUtils.escapeXml10(entry.getConceptList()));
        sb.append("</" + XMLJsonConstants.NAMESPACE_PREFIX + ":" + XMLJsonConstants.CONCEPT_LIST + ">");

        // creator id
        if (entry.getChangeEvents() != null && !entry.getChangeEvents().isEmpty()) {
            List<ChangeEvent> changeEvents = entry.getChangeEvents();
            Collections.sort(changeEvents);
            sb.append("<" + XMLJsonConstants.NAMESPACE_PREFIX + ":" + XMLJsonConstants.CREATOR_ID + ">");
            sb.append(StringEscapeUtils.escapeXml10(
                    changeEvents.get(0).getUserName() != null ? changeEvents.get(0).getUserName().trim() : ""));
            sb.append("</" + XMLJsonConstants.NAMESPACE_PREFIX + ":" + XMLJsonConstants.CREATOR_ID + ">");
        } else {
            // creator id
            sb.append("<" + XMLJsonConstants.NAMESPACE_PREFIX + ":" + XMLJsonConstants.CREATOR_ID + ">");
            sb.append(StringEscapeUtils.escapeXml10(entry.getCreatorId() != null ? entry.getCreatorId().trim() : ""));
            sb.append("</" + XMLJsonConstants.NAMESPACE_PREFIX + ":" + XMLJsonConstants.CREATOR_ID + ">");
        }

        // equal to
        sb.append("<" + XMLJsonConstants.NAMESPACE_PREFIX + ":" + XMLJsonConstants.EQUAL_TO + ">");
        sb.append(StringEscapeUtils.escapeXml10(entry.getEqualTo() != null ? entry.getEqualTo().trim() : ""));
        sb.append("</" + XMLJsonConstants.NAMESPACE_PREFIX + ":" + XMLJsonConstants.EQUAL_TO + ">");

        // modified by
        sb.append("<" + XMLJsonConstants.NAMESPACE_PREFIX + ":" + XMLJsonConstants.MODIFIED_BY + ">");
        sb.append(StringEscapeUtils.escapeXml10(entry.getModified() != null ? entry.getModified().trim() : ""));
        sb.append("</" + XMLJsonConstants.NAMESPACE_PREFIX + ":" + XMLJsonConstants.MODIFIED_BY + ">");

        // similar to
        sb.append("<" + XMLJsonConstants.NAMESPACE_PREFIX + ":" + XMLJsonConstants.SIMILAR_TO + ">");
        sb.append(StringEscapeUtils.escapeXml10(entry.getSimilarTo() != null ? entry.getSimilarTo().trim() : ""));
        sb.append("</" + XMLJsonConstants.NAMESPACE_PREFIX + ":" + XMLJsonConstants.SIMILAR_TO + ">");

        // synonym ids
        sb.append("<" + XMLJsonConstants.NAMESPACE_PREFIX + ":" + XMLJsonConstants.SYNONYM_IDS + ">");
        sb.append(StringEscapeUtils.escapeXml10(entry.getSynonymIds() != null ? entry.getSynonymIds().trim() : ""));
        sb.append("</" + XMLJsonConstants.NAMESPACE_PREFIX + ":" + XMLJsonConstants.SYNONYM_IDS + ">");

        // type
        sb.append("<" + XMLJsonConstants.NAMESPACE_PREFIX + ":" + XMLJsonConstants.TYPE + " ");
        if (type != null) {
            sb.append(XMLJsonConstants.TYPE_ID_ATTR + "=\"" + type.getTypeId() + "\" ");
            sb.append(XMLJsonConstants.TYPE_URI_ATTR + "=\"" + uriCreator.getTypeURI(type) + "\"");
        }
        sb.append(">");
        if (type != null)
            sb.append(StringEscapeUtils.escapeXml10(type.getTypeName()));
        sb.append("</" + XMLJsonConstants.NAMESPACE_PREFIX + ":" + XMLJsonConstants.TYPE + ">");

        // is deleted
        sb.append("<" + XMLJsonConstants.NAMESPACE_PREFIX + ":" + XMLJsonConstants.IS_DELETED + ">");
        sb.append(entry.isDeleted() + "");
        sb.append("</" + XMLJsonConstants.NAMESPACE_PREFIX + ":" + XMLJsonConstants.IS_DELETED + ">");

        // wordnet id
        sb.append("<" + XMLJsonConstants.NAMESPACE_PREFIX + ":" + XMLJsonConstants.WORDNET_ID + ">");
        sb.append(StringEscapeUtils.escapeXml10(entry.getWordnetId() != null ? entry.getWordnetId().trim() : ""));
        sb.append("</" + XMLJsonConstants.NAMESPACE_PREFIX + ":" + XMLJsonConstants.WORDNET_ID + ">");

        // Adding alternative ids and their corresponding uris
        if (entry.getAlternativeIds() != null && !entry.getAlternativeIds().isEmpty()) {
            Map<String, String> uriMap = uriCreator.getUrisBasedOnIds(entry.getAlternativeIds());
            if (uriMap != null && !uriMap.isEmpty()) {
                sb.append("<" + XMLJsonConstants.NAMESPACE_PREFIX + ":" + XMLJsonConstants.ALTERNATIVE_IDS + ">");
                for (Map.Entry<String, String> uri : uriMap.entrySet()) {
                    sb.append("<" + XMLJsonConstants.NAMESPACE_PREFIX + ":" + XMLJsonConstants.ID + " ");
                    sb.append(XMLJsonConstants.CONCEPT_ID + "=\"" + uri.getKey() + "\" ");
                    sb.append(XMLJsonConstants.CONCEPT_URI + "=\"" + uri.getValue() + "\"");
                    sb.append(">");
                    sb.append(uri.getValue());
                    sb.append("</" + XMLJsonConstants.NAMESPACE_PREFIX + ":" + XMLJsonConstants.ID + ">");
                }
                sb.append("</" + XMLJsonConstants.NAMESPACE_PREFIX + ":" + XMLJsonConstants.ALTERNATIVE_IDS + ">");
            }

        }

        sb.append("</" + XMLJsonConstants.NAMESPACE_PREFIX + ":" + XMLJsonConstants.CONCEPT_ENTRY + ">");

        return sb.toString();
    }

    public void appendDictionaries(List<ConceptList> lists) throws NotImplementedException {
        throw new NotImplementedException();
    }

}
