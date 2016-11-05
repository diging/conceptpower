package edu.asu.conceptpower.servlet.json;

import edu.asu.conceptpower.root.URIHelper;
import edu.asu.conceptpower.servlet.xml.IConceptMessage;
import edu.asu.conceptpower.servlet.xml.MessageConverters;
import edu.asu.conceptpower.servlet.xml.XMLTypeMessage;

public class JsonMessageFactory implements MessageConverters {

    private URIHelper uriCreator;

    public JsonMessageFactory(URIHelper uriCreator) {
        this.uriCreator = uriCreator;
    }

    public IConceptMessage createConceptMessage() {
        return new JsonConceptMessage(uriCreator);
    }

    @Override
    public XMLTypeMessage createTypeMessage() {
        // TODO Auto-generated method stub
        return null;
    }

}
