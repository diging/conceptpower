package edu.asu.conceptpower.app.config;

import java.io.IOException;
import java.util.Collections;
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
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import edu.asu.conceptpower.app.db.DatabaseManager;
import edu.asu.conceptpower.app.lucene.impl.LuceneDAO;
import edu.asu.conceptpower.app.wordnet.WordNetConfiguration;

@Configuration
@ComponentScan(basePackages = "edu.asu.conceptpower.app")
@Import({ RestConfig.class, XMLContext.class })
@PropertySource("classpath:config.properties")
@EnableWebMvc
@EnableWebSecurity
public class AppConfig {

    @Autowired
    private Environment env;
    
    @Autowired
    private UserDetailsService userDetailsService;

    private String dbPath;

    @Autowired
    public AppConfig(Environment env) {
        this.env = env;
        dbPath = env.getProperty("db.path");
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer properties() throws IOException {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        configurer.setLocations(new ClassPathResource("locale/messages_en_US.properties"));
        return configurer;
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
        messageSource.setBasenames("validatormessages", "pos");
        return messageSource;
    }

    // DB4o config
    @Bean(initMethod = "init", destroyMethod = "close")
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

    //SecurityContext
 // @formatter:off                                           
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {        
        http
          .authorizeHttpRequests((authorizeRequests) -> 
              authorizeRequests
                  .requestMatchers("/").permitAll()
              .requestMatchers("/auth/user/**", "/auth/index/**").hasRole("CP_ADMIN").requestMatchers("/auth/**")
              .authenticated().requestMatchers("/rest/concept/add").authenticated()
              .requestMatchers("/**").permitAll()
          )
          .formLogin((formLogin) -> 
              formLogin
                  .loginPage("/")
              .loginProcessingUrl("/login")
                  .permitAll()
          )
          .logout((logout) -> 
              logout
                  .logoutSuccessUrl("/").logoutUrl("/signout")
                  .permitAll()
          )
          .sessionManagement((sessionManagement) -> 
              sessionManagement.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
          )
          .exceptionHandling((exceptionHanlder) -> 
              exceptionHanlder.accessDeniedPage("/forbidden")
          )
          .csrf((csrf) ->
              csrf.csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
          );
          
        return http.build();
    }
    // @formatter:on

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(bCryptPasswordEncoder());
        return authProvider;
    }

    @Bean
    public RequestMatcher csrfRequestMatcher() {
        return new CsrfSecurityRequestMatcher();
    }

    @Bean
    public DefaultWebSecurityExpressionHandler webExpressionHandler() {
        return new DefaultWebSecurityExpressionHandler();
    }

    @Bean
    public HandlerMappingIntrospector mvcHandlerMappingIntrospector() {
        return new HandlerMappingIntrospector();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return new ProviderManager(Collections.singletonList(authenticationProvider()));
    }

    @Bean
    public HttpStatusReturningLogoutSuccessHandler logoutSuccessHandler() {
        return new HttpStatusReturningLogoutSuccessHandler();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    //WordnetConfig
    
    @Bean
    public WordNetConfiguration wordNetConfiguration() {
        WordNetConfiguration wordNetConfig = new WordNetConfiguration();
        wordNetConfig.setWordnetPath(env.getProperty("wordnet.install.path"));
        wordNetConfig.setDictFolder("dict");
        return wordNetConfig;
    }
    
    
    
}
