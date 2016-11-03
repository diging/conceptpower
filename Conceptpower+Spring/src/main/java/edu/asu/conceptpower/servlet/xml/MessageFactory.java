package edu.asu.conceptpower.servlet.xml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.asu.conceptpower.root.URIHelper;
import edu.asu.conceptpower.servlet.json.JsonMessageFactory;

@Component
public class MessageFactory {

    @Autowired
    private URIHelper uriCreator;

    public XMLMessageFactory getXMLMessageFactory() {
        return new XMLMessageFactory(uriCreator);
    }

    public JsonMessageFactory getJsonMessageFactory() {
        return new JsonMessageFactory(uriCreator);
    }
}
