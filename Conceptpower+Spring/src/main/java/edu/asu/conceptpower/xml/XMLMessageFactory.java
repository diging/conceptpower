package edu.asu.conceptpower.xml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.conceptpower.util.URIHelper;

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