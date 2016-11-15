package edu.asu.conceptpower.app.xml;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class MessageRegistry {

    @Autowired
    private ApplicationContext ctx;

    @Autowired
    private IMessageConverter defaultMessageFactory;

    private Map<String, IMessageConverter> messageConverterMap;

    /**
     * instantiates all the services under IMessageConverter interface
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

    public IMessageConverter getMessageFactory(String mediaType) {
        if (messageConverterMap.get(mediaType) != null) {
            return messageConverterMap.get(mediaType);
        }
        return defaultMessageFactory;
    }
}
