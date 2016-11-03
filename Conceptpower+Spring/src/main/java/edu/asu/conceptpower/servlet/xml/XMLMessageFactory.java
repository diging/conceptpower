package edu.asu.conceptpower.servlet.xml;

import edu.asu.conceptpower.root.URIHelper;

public class XMLMessageFactory {

    private URIHelper uriCreator;

    public XMLMessageFactory(URIHelper uriCreator) {
        this.uriCreator = uriCreator;
    }

	public XMLConceptMessage createXMLConceptMessage() {
		return new XMLConceptMessage(uriCreator);
	}
	
	public XMLTypeMessage createXMLTypeMessage() {
		return new XMLTypeMessage(uriCreator);
	}
}
