package edu.asu.conceptpower.servlet.json;

import org.apache.commons.lang3.StringEscapeUtils;

import edu.asu.conceptpower.core.ConceptType;
import edu.asu.conceptpower.root.URIHelper;
import edu.asu.conceptpower.servlet.xml.ITypeMessage;
import edu.asu.conceptpower.servlet.xml.XMLJsonConstants;

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
        sb.append("\"" + XMLJsonConstants.TYPE + "\" : [{");
        if (type != null) {
            sb.append("\"" + XMLJsonConstants.TYPE_ID_ATTR + "\"" + ":" + "\"" + type.getTypeId() + "\"");
            sb.append(",");
            sb.append("\"" + XMLJsonConstants.TYPE_URI_ATTR + "\"" + ":" + "\"" + uriCreator.getTypeURI(type) + "\"");
        }
        sb.append("}]");
        sb.append(",");

        // type description
        sb.append("\"" + XMLJsonConstants.DESCRIPTION + "\"" + ":");
        sb.append("\"" + StringEscapeUtils.escapeXml10(type.getDescription()) + "\"");
        sb.append(",");

        // creator id
        sb.append("\"" + XMLJsonConstants.CREATOR_ID + "\"" + ":");
        sb.append("\"" + StringEscapeUtils.escapeXml10(type.getCreatorId() != null ? type.getCreatorId().trim() : "")
                + "\"");
        sb.append(",");

        // matches
        sb.append("\"" + XMLJsonConstants.MATCHES + "\"" + ":");
        sb.append(
                "\"" + StringEscapeUtils.escapeXml10(type.getMatches() != null ? type.getMatches().trim() : "") + "\"");
        sb.append(",");

        // modified by
        sb.append("\"" + XMLJsonConstants.MODIFIED_BY + "\"" + ":");
        sb.append("\"" + StringEscapeUtils.escapeXml10(type.getModified() != null ? type.getModified().trim() : "")
                + "\"");


        // supertype
        if (supertype != null) {
            sb.append(",");
            sb.append("\"" + XMLJsonConstants.SUPERTYPE + "\"");
            sb.append(": [{");
            sb.append("\"" + XMLJsonConstants.TYPE_ID_ATTR + "\"" + ":");
            sb.append("\"" + supertype.getTypeId() + "\"");
            sb.append(",");
            sb.append("\"" + XMLJsonConstants.TYPE_URI_ATTR + "\"" + ":");
            sb.append("\"" + uriCreator.getTypeURI(supertype) + "\"");
            sb.append(",");
            sb.append("\"" + XMLJsonConstants.TYPE_NAME + "\"" + ":");
            sb.append("\"" + StringEscapeUtils.escapeXml10(supertype.getTypeName()) + "\"");
            sb.append("}]");
        }

        // end entry
        sb.append("}");

        return sb.toString();
    }

}
