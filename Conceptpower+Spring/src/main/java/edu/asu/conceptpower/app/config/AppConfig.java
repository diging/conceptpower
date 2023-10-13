package edu.asu.conceptpower.app.config;

import java.io.IOException;
import java.util.Properties;

import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import edu.asu.conceptpower.app.db.DatabaseManager;
import edu.asu.conceptpower.app.lucene.impl.LuceneDAO;
import edu.asu.conceptpower.app.wordnet.WordNetConfiguration;

@Configuration
@ComponentScan(basePackages = "edu.asu.conceptpower.app")
@Import({ SecurityContext.class, RestConfig.class, XMLContext.class })
@PropertySource("classpath:config.properties")
@EnableWebMvc
public class AppConfig {

    @Autowired
    private Environment env;
    
    private String dbPath;
    
    @Autowired
    public AppConfig(Environment env) {
        this.env = env;
        dbPath = env.getProperty("db.path");
    }

    @Bean
    public TaskExecutor indexExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        return executor;
    }

    @Bean
    public LuceneDAO luceneDAO() {
        LuceneDAO luceneDao = new LuceneDAO();
        luceneDao.setDbPath(env.getProperty("db.path") + "/lucene.db");
        return luceneDao;
    }

    @Bean
    public StandardAnalyzer analyzer() {
        return new StandardAnalyzer();
    }

    @Bean
    public WhitespaceAnalyzer whiteSpaceAnalyzer() {
        return new WhitespaceAnalyzer();
    }

    @Bean
    public KeywordAnalyzer keywordAnalyzer() {
        return new KeywordAnalyzer();
    }

    @Bean
    public JavaMailSenderImpl mailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(env.getProperty("email.host"));
        mailSender.setPort(env.getProperty("email.port", Integer.class));
        mailSender.setUsername(env.getProperty("email.user"));
        mailSender.setPassword(env.getProperty("email.pw"));

        Properties properties = new Properties();
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.debug", "true");

        mailSender.setJavaMailProperties(properties);

        return mailSender;
    }

    @Bean
    public SimpleMailMessage preConfiguredMessage() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(env.getProperty("email.from"));
        return message;
    }

    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("validatormessages,pos");
        return messageSource;
    }

    @Bean
    public Properties messages() {
        Properties messages = new Properties();
        try {
            messages.load(new ClassPathResource("locale/messages_en_US.properties").getInputStream());
        } catch (IOException e) {
            System.out.println("An exception occured while loading messages" + e);
        }
        return messages;
    }
    
    //DB4o config
    @Bean(initMethod = "init", destroyMethod = "close", name="userDatabaseManager")
    public DatabaseManager userDatabaseManager() {
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.setDatabasePath(dbPath + "users.db");
        return databaseManager;
    }
    
    @Bean(initMethod = "init", destroyMethod = "close")
    public DatabaseManager conceptDatabaseManager() {
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.setDatabasePath(dbPath + "conceptLists.db");
        return databaseManager;
    }
    
    @Bean(initMethod = "init", destroyMethod = "close")
    public DatabaseManager typesDatabaseManager() {
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.setDatabasePath(dbPath + "conceptTypes.db");
        return databaseManager;
    }
    
    @Bean(initMethod = "init", destroyMethod = "close")
    public DatabaseManager luceneDatabaseManager() {
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.setDatabasePath(dbPath + "lucene.db");
        return databaseManager;
    }
    
    @Bean(initMethod = "init", destroyMethod = "close")
    public DatabaseManager conceptReviewDatabaseManager() {
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.setDatabasePath(dbPath + "conceptReview.db");
        return databaseManager;
    }

    // WordnetConfig

    @Bean
    public WordNetConfiguration wordNetConfiguration() {
        WordNetConfiguration wordNetConfig = new WordNetConfiguration();
        wordNetConfig.setWordnetPath(env.getProperty("wordnet.install.path"));
        wordNetConfig.setDictFolder("dict");
        return wordNetConfig;
    }

}
