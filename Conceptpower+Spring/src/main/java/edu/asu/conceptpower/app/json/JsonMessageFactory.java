package edu.asu.conceptpower.app.json;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import edu.asu.conceptpower.app.xml.IConceptMessage;
import edu.asu.conceptpower.app.xml.IMessageConverter;
import edu.asu.conceptpower.app.xml.ITypeMessage;
import edu.asu.conceptpower.app.xml.URIHelper;

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
