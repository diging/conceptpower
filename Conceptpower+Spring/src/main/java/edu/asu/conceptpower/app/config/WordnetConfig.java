package edu.asu.conceptpower.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import edu.asu.conceptpower.app.wordnet.WordNetConfiguration;

/**
 * @author Digital Innovation Group
 *
 */
@Configuration
@PropertySource("classpath:config.properties")
public class WordnetConfig {

    @Autowired
    private Environment env;

    @Bean
    public WordNetConfiguration wordNetConfiguration() {
        WordNetConfiguration wordNetConfig = new WordNetConfiguration();
        wordNetConfig.setWordnetPath(env.getProperty("wordnet.install.path"));
        wordNetConfig.setDictFolder("dict");
        return wordNetConfig;
    }
}
