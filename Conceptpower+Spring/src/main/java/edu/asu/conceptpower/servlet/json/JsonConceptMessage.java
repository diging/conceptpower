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
import edu.asu.conceptpower.servlet.xml.IConceptMessage;
import edu.asu.conceptpower.servlet.xml.NotImplementedException;
import edu.asu.conceptpower.servlet.xml.XMLJsonConstants;

public class JsonConceptMessage implements IConceptMessage {

    private URIHelper uriCreator;

    public JsonConceptMessage(URIHelper uriCreator) {
        this.uriCreator = uriCreator;
    }

    public String getAllConceptMessage(Map<ConceptEntry, ConceptType> entries) {
        StringBuilder jsonEntries = new StringBuilder("");
        jsonEntries.append("{\"entries\":");
        jsonEntries.append("[");
        int i = 1;
        for (ConceptEntry entry : entries.keySet()) {
            jsonEntries.append(getConceptMessage(entry, entries.get(entry)));
            if (i < entries.size()) {
                jsonEntries.append(",");
                i++;
            }
        }
        jsonEntries.append("]");
        jsonEntries.append("}");
        return jsonEntries.toString();
    }

    public String getConceptMessage(ConceptEntry entry, ConceptType type) {
        StringBuffer sb = new StringBuffer();

        // id
        sb.append("{");
        sb.append("\"" + XMLJsonConstants.ID + "\"" + ":");
        sb.append("\"" + uriCreator.getURI(entry) + "\"");
        sb.append(",");

        // lemma
        sb.append("\"" + XMLJsonConstants.LEMMA + "\"" + ":");
        sb.append("\"" + StringEscapeUtils.escapeXml10(entry.getWord()) + "\"");
        sb.append(",");

        // pos
        sb.append("\"" + XMLJsonConstants.POS + "\"" + ":");
        sb.append("\"" + entry.getPos() + "\"");
        sb.append(",");

        // description
        sb.append("\"" + XMLJsonConstants.DESCRIPTION + "\"" + ":");
        sb.append("\"" + StringEscapeUtils.escapeXml10(entry.getDescription()) + "\"");
        sb.append(",");

        // concept list
        sb.append("\"" + XMLJsonConstants.CONCEPT_LIST + "\"" + ":");
        sb.append("\"" + StringEscapeUtils.escapeXml10(entry.getConceptList()) + "\"");
        sb.append(",");

        // creator id
        if (entry.getChangeEvents() != null && !entry.getChangeEvents().isEmpty()) {
            List<ChangeEvent> changeEvents = entry.getChangeEvents();
            Collections.sort(changeEvents);
            sb.append("\"" + XMLJsonConstants.CREATOR_ID + "\"" + ":");
            sb.append("\""
                    + StringEscapeUtils.escapeXml10(
                            changeEvents.get(0).getUserName() != null ? changeEvents.get(0).getUserName().trim() : "")
                    + "\"");
            sb.append(",");

        } else {
            // creator id
            sb.append("\"" + XMLJsonConstants.CREATOR_ID + "\"" + ":");
            sb.append("\""
                    + StringEscapeUtils.escapeXml10(entry.getCreatorId() != null ? entry.getCreatorId().trim() : "")
                    + "\"");
            sb.append(",");
        }

        // equal to
        sb.append("\"" + XMLJsonConstants.EQUAL_TO + "\"" + ":");
        sb.append("\"" + StringEscapeUtils.escapeXml10(entry.getEqualTo() != null ? entry.getEqualTo().trim() : "")
                + "\"");
        sb.append(",");

        // modified by
        sb.append("\"" + XMLJsonConstants.MODIFIED_BY + "\"" + ":");
        sb.append("\"" + StringEscapeUtils.escapeXml10(entry.getModified() != null ? entry.getModified().trim() : "")
                + "\"");
        sb.append(",");

        // similar to
        sb.append("\"" + XMLJsonConstants.SIMILAR_TO + "\"" + ":");
        sb.append("\"" + StringEscapeUtils.escapeXml10(entry.getSimilarTo() != null ? entry.getSimilarTo().trim() : "")
                + "\"");
        sb.append(",");

        // synonym ids
        sb.append("\"" + XMLJsonConstants.SYNONYM_IDS + "\"" + ":");
        sb.append(
                "\"" + StringEscapeUtils.escapeXml10(entry.getSynonymIds() != null ? entry.getSynonymIds().trim() : "")
                        + "\"");
        sb.append(",");

        // type
        if (type != null) {
            sb.append("\"" + XMLJsonConstants.TYPE + "\"" + ":");
            sb.append("{");
            sb.append("\"" + XMLJsonConstants.TYPE_ID_ATTR + "\"");
            sb.append(":");
            sb.append("\"" + type.getTypeId() + "\"");
            sb.append(",");
            sb.append("\"" + XMLJsonConstants.TYPE_URI_ATTR + "\"");
            sb.append(":");
            sb.append("\"" + uriCreator.getTypeURI(type) + "\"");
            sb.append(",");
            sb.append("\"" + XMLJsonConstants.TYPE_NAME + "\"");
            sb.append(":");
            sb.append("\"" + StringEscapeUtils.escapeXml10(type.getTypeName()) + "\"");
            sb.append("}");
            sb.append(",");
        }

        // is deleted
        sb.append("\"" + XMLJsonConstants.IS_DELETED + "\"" + ":");
        sb.append("\"" + entry.isDeleted() + "\"");
        sb.append(",");

        // wordnet id
        sb.append("\"" + XMLJsonConstants.WORDNET_ID + "\"" + ":");
        sb.append("\"" + StringEscapeUtils.escapeXml10(entry.getWordnetId() != null ? entry.getWordnetId().trim() : "")
                + "\"");

        // Adding alternative ids and their corresponding uris
        if (entry.getAlternativeIds() != null && !entry.getAlternativeIds().isEmpty()) {
            Map<String, String> uriMap = uriCreator.getUrisBasedOnIds(entry.getAlternativeIds());
            if (uriMap != null && !uriMap.isEmpty()) {

                sb.append(",");
                sb.append("\"" + XMLJsonConstants.ALTERNATIVE_IDS + "\"" + ":");
                sb.append("[{");
                boolean addComma = false;
                for (Map.Entry<String, String> uri : uriMap.entrySet()) {
                    if (addComma) {
                        sb.append(",");
                    }
                    sb.append("\"" + XMLJsonConstants.CONCEPT_ID + "\" : ");
                    sb.append("\"" + uri.getKey() + "\"");
                    sb.append(",");
                    sb.append("\""+ XMLJsonConstants.CONCEPT_URI +"\" : ");
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
