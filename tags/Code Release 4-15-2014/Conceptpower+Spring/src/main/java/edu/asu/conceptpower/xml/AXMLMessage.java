package edu.asu.conceptpower.xml;

import java.util.List;

public class AXMLMessage {

	public String getXML(List<String> entries) {
		StringBuffer sb = new StringBuffer();

		sb.append("<" + XMLConstants.CONCEPTPOWER_ANSWER + " xmlns:"
				+ XMLConstants.NAMESPACE_PREFIX + "=\""
				+ XMLConstants.NAMESPACE + "\">");

		for (String entry : entries) {
			sb.append(entry);
		}

		sb.append("</" + XMLConstants.CONCEPTPOWER_ANSWER + ">");

		return sb.toString();
	}

}