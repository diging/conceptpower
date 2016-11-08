package edu.asu.conceptpower.servlet.json;

import org.apache.commons.lang3.StringEscapeUtils;

import edu.asu.conceptpower.core.ConceptType;
import edu.asu.conceptpower.root.URIHelper;
import edu.asu.conceptpower.servlet.xml.ITypeMessage;
import edu.asu.conceptpower.servlet.xml.XMLConstants;

public class JsonTypeMessage implements ITypeMessage {

    private URIHelper uriCreator;

    public JsonTypeMessage(URIHelper uriCreator) {
        this.uriCreator = uriCreator;
    }

    @Override
    public String getConceptTypeMessage(ConceptType type, ConceptType supertype) {

        StringBuffer sb = new StringBuffer();

        // start entry
        sb.append("{");

        // type uri, id
        sb.append("\"" + XMLConstants.TYPE + "\" : [{");
        if (type != null) {
            sb.append("\"" + XMLConstants.TYPE_ID_ATTR + "\"" + ":" + "\"" + type.getTypeId() + "\"");
            sb.append(",");
            sb.append("\"" + XMLConstants.TYPE_URI_ATTR + "\"" + ":" + "\"" + uriCreator.getTypeURI(type) + "\"");
        }
        sb.append("}]");
        sb.append(",");

        // type description
        sb.append("\"" + XMLConstants.DESCRIPTION + "\"" + ":");
        sb.append("\"" + StringEscapeUtils.escapeXml10(type.getDescription()) + "\"");
        sb.append(",");

        // creator id
        sb.append("\"" + XMLConstants.CREATOR_ID + "\"" + ":");
        sb.append("\"" + StringEscapeUtils.escapeXml10(type.getCreatorId() != null ? type.getCreatorId().trim() : "")
                + "\"");
        sb.append(",");

        // matches
        sb.append("\"" + XMLConstants.MATCHES + "\"" + ":");
        sb.append(
                "\"" + StringEscapeUtils.escapeXml10(type.getMatches() != null ? type.getMatches().trim() : "") + "\"");
        sb.append(",");

        // modified by
        sb.append("\"" + XMLConstants.MODIFIED_BY + "\"" + ":");
        sb.append("\"" + StringEscapeUtils.escapeXml10(type.getModified() != null ? type.getModified().trim() : "")
                + "\"");


        // supertype
        if (supertype != null) {
            sb.append(",");
            sb.append("\"" + XMLConstants.SUPERTYPE + "\"");
            sb.append(": [{");
            sb.append("\"" + XMLConstants.TYPE_ID_ATTR + "\"" + ":");
            sb.append("\"" + supertype.getTypeId() + "\"");
            sb.append(",");
            sb.append("\"" + XMLConstants.TYPE_URI_ATTR + "\"" + ":");
            sb.append("\"" + uriCreator.getTypeURI(supertype) + "\"");
            sb.append(",");
            sb.append("\"" + XMLConstants.TYPE_NAME + "\"" + ":");
            sb.append("\"" + StringEscapeUtils.escapeXml10(supertype.getTypeName()) + "\"");
            sb.append("}]");
        }

        // end entry
        sb.append("}");

        return sb.toString();
    }

}
