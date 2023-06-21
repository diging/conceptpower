package edu.asu.conceptpower.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class XMLContext {
    
    @Autowired
    private XMLConfig xmlConfig;
    
    @Bean
    public XMLConfig xmlConfiguration() {
        xmlConfig.setUriPrefix("http://www.digitalhps.org/concepts/");
        xmlConfig.setXmlNamespace("http://www.digitalhps.org/");
        
        return xmlConfig;
    }

}
