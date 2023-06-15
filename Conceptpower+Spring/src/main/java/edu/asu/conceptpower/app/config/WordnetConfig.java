package edu.asu.conceptpower.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import edu.asu.conceptpower.app.wordnet.WordNetConfiguration;

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
