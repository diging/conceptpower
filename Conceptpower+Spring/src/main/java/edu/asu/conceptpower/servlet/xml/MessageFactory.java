package edu.asu.conceptpower.servlet.xml;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import edu.asu.conceptpower.root.URIHelper;
import edu.asu.conceptpower.servlet.json.JsonMessageFactory;

@Component
public class MessageFactory {

    @Autowired
    private URIHelper uriCreator;

    public MessageConverters getMessageFactory(String mediaType) {
        if (MediaType.APPLICATION_XML_VALUE.equalsIgnoreCase(mediaType)) {
            return new XMLMessageFactory(uriCreator);
        }
        return new JsonMessageFactory(uriCreator);

    }
}
