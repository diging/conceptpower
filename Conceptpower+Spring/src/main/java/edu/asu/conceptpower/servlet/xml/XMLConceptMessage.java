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
public class XMLConceptMessage extends AXMLMessage implements IConceptMessage {

    private URIHelper uriCreator;

    public XMLConceptMessage(URIHelper uriCreator) {
        this.uriCreator = uriCreator;
    }

    @Override
    public String getAllConceptMessage(Map<ConceptEntry, ConceptType> entries) {
        StringBuilder xmlEntries = new StringBuilder();
        for (ConceptEntry entry : entries.keySet())
            xmlEntries.append(getConceptMessage(entry, entries.get(entry)));

        return xmlEntries.toString();
    }

    public String getConceptMessage(ConceptEntry entry, ConceptType type) {
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

        sb.append("</" + XMLConstants.NAMESPACE_PREFIX + ":" + XMLConstants.CONCEPT_ENTRY + ">");

        return sb.toString();
    }

    public void appendDictionaries(List<ConceptList> lists) throws NotImplementedException {
        throw new NotImplementedException();
    }

}
