package edu.asu.conceptpower.servlet.xml;

import org.apache.commons.lang3.StringEscapeUtils;

import edu.asu.conceptpower.core.ConceptType;
import edu.asu.conceptpower.root.URIHelper;

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

        sb.append("<" + XMLJsonConstants.CONCEPTPOWER_ANSWER + " xmlns:" + XMLJsonConstants.NAMESPACE_PREFIX + "=\""
                + XMLJsonConstants.NAMESPACE + "\">");

		// start entry
		sb.append("<" + XMLJsonConstants.NAMESPACE_PREFIX + ":"
				+ XMLJsonConstants.TYPE_ENTRY + ">");

		// type uri, id
		sb.append("<" + XMLJsonConstants.NAMESPACE_PREFIX + ":" + XMLJsonConstants.TYPE
				+ " ");
		if (type != null) {
			sb.append(XMLJsonConstants.TYPE_ID_ATTR + "=\"" + type.getTypeId()
					+ "\" ");
			sb.append(XMLJsonConstants.TYPE_URI_ATTR + "=\""
					+ uriCreator.getTypeURI(type) + "\"");
		}
		sb.append(">");
		if (type != null)
			sb.append(StringEscapeUtils.escapeXml10(type.getTypeName()));
		sb.append("</" + XMLJsonConstants.NAMESPACE_PREFIX + ":"
				+ XMLJsonConstants.TYPE + ">");

		// type description
		sb.append("<" + XMLJsonConstants.NAMESPACE_PREFIX + ":"
				+ XMLJsonConstants.DESCRIPTION + ">");
		sb.append(StringEscapeUtils.escapeXml10(type.getDescription()));
		sb.append("</" + XMLJsonConstants.NAMESPACE_PREFIX + ":"
				+ XMLJsonConstants.DESCRIPTION + ">");

		// creator id
		sb.append("<" + XMLJsonConstants.NAMESPACE_PREFIX + ":"
				+ XMLJsonConstants.CREATOR_ID + ">");
		sb.append(StringEscapeUtils.escapeXml10(type.getCreatorId() != null ? type
				.getCreatorId().trim() : ""));
		sb.append("</" + XMLJsonConstants.NAMESPACE_PREFIX + ":"
				+ XMLJsonConstants.CREATOR_ID + ">");

		// matches
		sb.append("<" + XMLJsonConstants.NAMESPACE_PREFIX + ":"
				+ XMLJsonConstants.MATCHES + ">");
		sb.append(StringEscapeUtils.escapeXml10(type.getMatches() != null ? type
				.getMatches().trim() : ""));
		sb.append("</" + XMLJsonConstants.NAMESPACE_PREFIX + ":"
				+ XMLJsonConstants.MATCHES + ">");

		// modified by
		sb.append("<" + XMLJsonConstants.NAMESPACE_PREFIX + ":"
				+ XMLJsonConstants.MODIFIED_BY + ">");
		sb.append(StringEscapeUtils.escapeXml10(type.getModified() != null ? type
				.getModified().trim() : ""));
		sb.append("</" + XMLJsonConstants.NAMESPACE_PREFIX + ":"
				+ XMLJsonConstants.MODIFIED_BY + ">");

		// supertype
		if (supertype != null) {
			sb.append("<" + XMLJsonConstants.NAMESPACE_PREFIX + ":"
					+ XMLJsonConstants.SUPERTYPE + " ");

			sb.append(XMLJsonConstants.TYPE_ID_ATTR + "=\"" + supertype.getTypeId()
					+ "\" ");
			sb.append(XMLJsonConstants.TYPE_URI_ATTR + "=\""
					+ uriCreator.getTypeURI(supertype) + "\"");

			sb.append(">");
			sb.append(StringEscapeUtils.escapeXml10(supertype.getTypeName()));
			sb.append("</" + XMLJsonConstants.NAMESPACE_PREFIX + ":"
					+ XMLJsonConstants.SUPERTYPE + ">");
		}

		// end entry
		sb.append("</" + XMLJsonConstants.NAMESPACE_PREFIX + ":"
				+ XMLJsonConstants.TYPE_ENTRY + ">");

        sb.append("</" + XMLJsonConstants.CONCEPTPOWER_ANSWER + ">");

        return sb.toString();
	}
}
