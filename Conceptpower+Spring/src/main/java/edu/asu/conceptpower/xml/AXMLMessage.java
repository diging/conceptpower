package edu.asu.conceptpower.xml;

import java.util.ArrayList;
import java.util.List;

public class AXMLMessage {
	
	private List<String> entries;

	public AXMLMessage() {
		super();
		entries = new ArrayList<String>();
	}

	public String getXML() {
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
	
	public void addEntry(String entry) {
		entries.add(entry);
	}

}