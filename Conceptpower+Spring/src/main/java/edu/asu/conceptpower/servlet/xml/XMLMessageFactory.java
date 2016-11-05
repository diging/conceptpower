package edu.asu.conceptpower.servlet.xml;

import edu.asu.conceptpower.root.URIHelper;

public class XMLMessageFactory implements MessageConverters {

    private URIHelper uriCreator;

    public XMLMessageFactory(URIHelper uriCreator) {
        this.uriCreator = uriCreator;
    }

    public IConceptMessage createConceptMessage() {
		return new XMLConceptMessage(uriCreator);
	}
	
    public XMLTypeMessage createTypeMessage() {
		return new XMLTypeMessage(uriCreator);
	}
}
