package edu.asu.conceptpower.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Digital Innovation Group
 *
 */
@Configuration
public class XMLContext {

    private XMLConfig xmlConfig = new XMLConfig();

    @Bean
    public XMLConfig xmlConfiguration() {
        xmlConfig.setUriPrefix("http://www.digitalhps.org/concepts/");
        xmlConfig.setXmlNamespace("http://www.digitalhps.org/");

        return xmlConfig;
    }

}
