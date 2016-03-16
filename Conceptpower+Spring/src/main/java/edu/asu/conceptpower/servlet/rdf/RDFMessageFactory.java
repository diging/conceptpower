package edu.asu.conceptpower.servlet.rdf;

import org.springframework.beans.factory.annotation.Autowired;

import edu.asu.conceptpower.root.URIHelper;
import edu.asu.conceptpower.servlet.xml.XMLTypeMessage;

public class RDFMessageFactory {


    @Autowired
    private URIHelper uriCreator;
    
    public RDFConceptMessage createRDFConceptMessage() {
        return new RDFConceptMessage(uriCreator);
    }
    
    public XMLTypeMessage createRDFTypeMessage() {
        return new XMLTypeMessage(uriCreator);
    }

}
