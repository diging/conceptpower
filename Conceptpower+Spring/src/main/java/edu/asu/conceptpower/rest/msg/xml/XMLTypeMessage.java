package edu.asu.conceptpower.rest.msg.xml;

import org.apache.commons.lang3.StringEscapeUtils;

import edu.asu.conceptpower.app.util.URIHelper;
import edu.asu.conceptpower.core.ConceptType;
import edu.asu.conceptpower.rest.msg.ITypeMessage;

/**
 * This class is used to create XML responses for
 * type queries.
 * 
 * @author Julia Damerow
 *
 */
public class XMLTypeMessage implements ITypeMessage {

	private URIHelper uriCreator;
	
	public XMLTypeMessage(URIHelper uriCreator) {
		this.uriCreator = uriCreator;
	}

    public String getConceptTypeMessage(ConceptType type, ConceptType supertype) {

		StringBuffer sb = new StringBuffer();

        sb.append("<" + XMLConstants.CONCEPTPOWER_ANSWER + " xmlns:" + XMLConstants.NAMESPACE_PREFIX + "=\""
                + XMLConstants.NAMESPACE + "\">");

		// start entry
        sb.append("<" + XMLConstants.NAMESPACE_PREFIX + ":" + XMLConstants.TYPE_ENTRY + ">");

		// type uri, id
        sb.append("<" + XMLConstants.NAMESPACE_PREFIX + ":" + XMLConstants.TYPE
				+ " ");
		if (type != null) {
            sb.append(XMLConstants.TYPE_ID_ATTR + "=\"" + type.getTypeId()
					+ "\" ");
            sb.append(XMLConstants.TYPE_URI_ATTR + "=\""
					+ uriCreator.getTypeURI(type) + "\"");
		}
		sb.append(">");
		if (type != null)
			sb.append(StringEscapeUtils.escapeXml10(type.getTypeName()));
        sb.append("</" + XMLConstants.NAMESPACE_PREFIX + ":" + XMLConstants.TYPE + ">");

		// type description
        sb.append("<" + XMLConstants.NAMESPACE_PREFIX + ":" + XMLConstants.DESCRIPTION + ">");
		sb.append(StringEscapeUtils.escapeXml10(type.getDescription()));
        sb.append("</" + XMLConstants.NAMESPACE_PREFIX + ":" + XMLConstants.DESCRIPTION + ">");

		// creator id
        sb.append("<" + XMLConstants.NAMESPACE_PREFIX + ":" + XMLConstants.CREATOR_ID + ">");
		sb.append(StringEscapeUtils.escapeXml10(type.getCreatorId() != null ? type
				.getCreatorId().trim() : ""));
        sb.append("</" + XMLConstants.NAMESPACE_PREFIX + ":" + XMLConstants.CREATOR_ID + ">");

		// matches
        sb.append("<" + XMLConstants.NAMESPACE_PREFIX + ":" + XMLConstants.MATCHES + ">");
		sb.append(StringEscapeUtils.escapeXml10(type.getMatches() != null ? type
				.getMatches().trim() : ""));
        sb.append("</" + XMLConstants.NAMESPACE_PREFIX + ":" + XMLConstants.MATCHES + ">");

		// modified by
        sb.append("<" + XMLConstants.NAMESPACE_PREFIX + ":" + XMLConstants.MODIFIED_BY + ">");
		sb.append(StringEscapeUtils.escapeXml10(type.getModified() != null ? type
				.getModified().trim() : ""));
        sb.append("</" + XMLConstants.NAMESPACE_PREFIX + ":" + XMLConstants.MODIFIED_BY + ">");

		// supertype
		if (supertype != null) {
            sb.append("<" + XMLConstants.NAMESPACE_PREFIX + ":" + XMLConstants.SUPERTYPE + " ");

            sb.append(XMLConstants.TYPE_ID_ATTR + "=\"" + supertype.getTypeId()
					+ "\" ");
            sb.append(XMLConstants.TYPE_URI_ATTR + "=\""
					+ uriCreator.getTypeURI(supertype) + "\"");

			sb.append(">");
			sb.append(StringEscapeUtils.escapeXml10(supertype.getTypeName()));
            sb.append("</" + XMLConstants.NAMESPACE_PREFIX + ":" + XMLConstants.SUPERTYPE + ">");
		}

		// end entry
        sb.append("</" + XMLConstants.NAMESPACE_PREFIX + ":" + XMLConstants.TYPE_ENTRY + ">");

        sb.append("</" + XMLConstants.CONCEPTPOWER_ANSWER + ">");

        return sb.toString();
    }
}
