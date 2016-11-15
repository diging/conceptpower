package edu.asu.conceptpower.app.xml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class XMLMessageFactory {

	@Autowired
	private URIHelper uriCreator;
	
	public XMLConceptMessage createXMLConceptMessage() {
		return new XMLConceptMessage(uriCreator);
	}
	
	public XMLTypeMessage createXMLTypeMessage() {
		return new XMLTypeMessage(uriCreator);
	}
}
