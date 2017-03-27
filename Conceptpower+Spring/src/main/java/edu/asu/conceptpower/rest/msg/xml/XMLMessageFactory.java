package edu.asu.conceptpower.rest.msg.xml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import edu.asu.conceptpower.app.util.URIHelper;
import edu.asu.conceptpower.rest.msg.IConceptMessage;
import edu.asu.conceptpower.rest.msg.IMessageConverter;

@Primary
@Component
public class XMLMessageFactory implements IMessageConverter {

    @Autowired
    private URIHelper uriCreator;

    public IConceptMessage createConceptMessage() {
		return new XMLConceptMessage(uriCreator);
	}
	
    public XMLTypeMessage createTypeMessage() {
		return new XMLTypeMessage(uriCreator);
	}

    @Override
    public String getMediaType() {
        return MediaType.APPLICATION_XML_VALUE;
    }
}
