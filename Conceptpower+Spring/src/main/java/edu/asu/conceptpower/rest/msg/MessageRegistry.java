package edu.asu.conceptpower.rest.msg;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class MessageRegistry implements IMessageRegistry {

    @Autowired
    private ApplicationContext ctx;

    @Autowired
    private IMessageConverter defaultMessageFactory;

    private Map<String, IMessageConverter> messageConverterMap;

    /**
     * Instantiates all the message converter services and loads into
     * messageConverterMap with key as media type and value as the message
     * converter service object.
     * 
     */
    @PostConstruct
    public void init() {

        messageConverterMap = new HashMap<>();
        Map<String, IMessageConverter> messageConverterCtxMap = ctx.getBeansOfType(IMessageConverter.class);
        Iterator<?> converterIter = messageConverterCtxMap.entrySet().iterator();

        while (converterIter.hasNext()) {
            Map.Entry mEntry = (Map.Entry) converterIter.next();
            IMessageConverter serviceObject = (IMessageConverter) mEntry.getValue();
            messageConverterMap.put(serviceObject.getMediaType(), serviceObject);
        }
    }

    /**
     * This method returns the message converter object based on the media type.
     * Valid values for media type are accept headers of XML and JSON. Accept
     * header of XML is application/xml and Accept header of JSON is
     * application/json.
     */
    public IMessageConverter getMessageFactory(String mediaType) {
        if (messageConverterMap.get(mediaType) != null) {
            return messageConverterMap.get(mediaType);
        }
        return defaultMessageFactory;
    }
}
