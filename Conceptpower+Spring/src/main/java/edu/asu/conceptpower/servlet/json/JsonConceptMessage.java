package edu.asu.conceptpower.servlet.json;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;

import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptList;
import edu.asu.conceptpower.core.ConceptType;
import edu.asu.conceptpower.root.URIHelper;
import edu.asu.conceptpower.servlet.core.ChangeEvent;
import edu.asu.conceptpower.servlet.xml.NotImplementedException;
import edu.asu.conceptpower.servlet.xml.XMLConstants;

public class JsonConceptMessage {

    private URIHelper uriCreator;

    public JsonConceptMessage(URIHelper uriCreator) {
        this.uriCreator = uriCreator;
    }

    public String getJsonArray(Map<ConceptEntry, ConceptType> entries) {
        StringBuilder jsonEntries = new StringBuilder("");
        jsonEntries.append("{\"entries\":");
        jsonEntries.append("[");
        int i = 1;
        for (ConceptEntry entry : entries.keySet()) {
            jsonEntries.append(getJson(entry, entries.get(entry)));
            if (i < entries.size()) {
                jsonEntries.append(",");
                i++;
            }
        }
        jsonEntries.append("]");
        jsonEntries.append("}");
        return jsonEntries.toString();
    }

    public String getJson(ConceptEntry entry, ConceptType type) {
        StringBuffer sb = new StringBuffer();

        // id
        sb.append("{");
        sb.append("\"" + XMLConstants.ID + "\"" + ":");
        sb.append("\"" + uriCreator.getURI(entry) + "\"");
        sb.append(",");

        // lemma
        sb.append("\"" + XMLConstants.LEMMA + "\"" + ":");
        sb.append("\"" + StringEscapeUtils.escapeXml10(entry.getWord()) + "\"");
        sb.append(",");

        // pos
        sb.append("\"" + XMLConstants.POS + "\"" + ":");
        sb.append("\"" + entry.getPos() + "\"");
        sb.append(",");

        // description
        sb.append("\"" + XMLConstants.DESCRIPTION + "\"" + ":");
        sb.append("\"" + StringEscapeUtils.escapeXml10(entry.getDescription()) + "\"");
        sb.append(",");

        // concept list
        sb.append("\"" + XMLConstants.CONCEPT_LIST + "\"" + ":");
        sb.append("\"" + StringEscapeUtils.escapeXml10(entry.getConceptList()) + "\"");
        sb.append(",");

        // creator id
        if (entry.getChangeEvents() != null && !entry.getChangeEvents().isEmpty()) {
            List<ChangeEvent> changeEvents = entry.getChangeEvents();
            Collections.sort(changeEvents);
            sb.append("\"" + XMLConstants.CREATOR_ID + "\"" + ":");
            sb.append("\""
                    + StringEscapeUtils.escapeXml10(
                            changeEvents.get(0).getUserName() != null ? changeEvents.get(0).getUserName().trim() : "")
                    + "\"");
            sb.append(",");

        } else {
            // creator id
            sb.append("\"" + XMLConstants.CREATOR_ID + "\"" + ":");
            sb.append("\""
                    + StringEscapeUtils.escapeXml10(entry.getCreatorId() != null ? entry.getCreatorId().trim() : "")
                    + "\"");
            sb.append(",");
        }

        // equal to
        sb.append("\"" + XMLConstants.EQUAL_TO + "\"" + ":");
        sb.append("\"" + StringEscapeUtils.escapeXml10(entry.getEqualTo() != null ? entry.getEqualTo().trim() : "")
                + "\"");
        sb.append(",");

        // modified by
        sb.append("\"" + XMLConstants.MODIFIED_BY + "\"" + ":");
        sb.append("\"" + StringEscapeUtils.escapeXml10(entry.getModified() != null ? entry.getModified().trim() : "")
                + "\"");
        sb.append(",");

        // similar to
        sb.append("\"" + XMLConstants.SIMILAR_TO + "\"" + ":");
        sb.append("\"" + StringEscapeUtils.escapeXml10(entry.getSimilarTo() != null ? entry.getSimilarTo().trim() : "")
                + "\"");
        sb.append(",");

        // synonym ids
        sb.append("\"" + XMLConstants.SYNONYM_IDS + "\"" + ":");
        sb.append(
                "\"" + StringEscapeUtils.escapeXml10(entry.getSynonymIds() != null ? entry.getSynonymIds().trim() : "")
                        + "\"");
        sb.append(",");

        // type
        if (type != null) {
            sb.append("\"" + XMLConstants.TYPE + "\"" + ":");
            sb.append("{");
            sb.append("\"" + XMLConstants.TYPE_ID_ATTR + "\"");
            sb.append(":");
            sb.append("\"" + type.getTypeId() + "\"");
            sb.append(",");
            sb.append("\"" + XMLConstants.TYPE_URI_ATTR + "\"");
            sb.append(":");
            sb.append("\"" + uriCreator.getTypeURI(type) + "\"");
            sb.append(",");
            sb.append("\"" + XMLConstants.TYPE_NAME + "\"");
            sb.append(":");
            sb.append("\"" + StringEscapeUtils.escapeXml10(type.getTypeName()) + "\"");
            sb.append("}");
            sb.append(",");
        }

        // is deleted
        sb.append("\"" + XMLConstants.IS_DELETED + "\"" + ":");
        sb.append("\"" + entry.isDeleted() + "\"");
        sb.append(",");

        // wordnet id
        sb.append("\"" + XMLConstants.WORDNET_ID + "\"" + ":");
        sb.append("\"" + StringEscapeUtils.escapeXml10(entry.getWordnetId() != null ? entry.getWordnetId().trim() : "")
                + "\"");

        // Adding alternative ids and their corresponding uris
        if (entry.getAlternativeIds() != null && !entry.getAlternativeIds().isEmpty()) {
            Map<String, String> uriMap = uriCreator.getUrisBasedOnIds(entry.getAlternativeIds());
            if (uriMap != null && !uriMap.isEmpty()) {

                sb.append(",");
                sb.append("\"" + XMLConstants.ALTERNATIVE_IDS + "\"" + ":");
                sb.append("[{");
                boolean addComma = false;
                for (Map.Entry<String, String> uri : uriMap.entrySet()) {
                    if (addComma) {
                        sb.append(",");
                    }
                    sb.append("\"" + XMLConstants.CONCEPT_ID + "\" : ");
                    sb.append("\"" + uri.getKey() + "\"");
                    sb.append(",");
                    sb.append("\""+ XMLConstants.CONCEPT_URI +"\" : ");
                    sb.append("\"" + uri.getValue() + "\"");
                    sb.append("}");
                    addComma = true;
                }
                sb.append("]");
            }

        }

        sb.append("}");
        return sb.toString();
    }

    public void appendDictionaries(List<ConceptList> lists) throws NotImplementedException {
        throw new NotImplementedException();
    }

}
