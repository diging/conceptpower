package edu.asu.conceptpower.servlet.json;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import edu.asu.conceptpower.root.URIHelper;
import edu.asu.conceptpower.servlet.xml.IConceptMessage;
import edu.asu.conceptpower.servlet.xml.IMessageConverter;
import edu.asu.conceptpower.servlet.xml.ITypeMessage;

@Component
public class JsonMessageFactory implements IMessageConverter {

    @Autowired
    private URIHelper uriCreator;

    public IConceptMessage createConceptMessage() {
        return new JsonConceptMessage(uriCreator);
    }

    @Override
    public ITypeMessage createTypeMessage() {
        return new JsonTypeMessage(uriCreator);
    }

    @Override
    public String getMediaType() {
        return MediaType.APPLICATION_JSON_VALUE;
    }

}
