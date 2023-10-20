package edu.asu.conceptpower.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import edu.asu.conceptpower.app.db.DatabaseManager;

/**
 * @author Digital Innovation Group
 *
 */
@Configuration
@PropertySource("classpath:config.properties")
public class Db4oConfig {

    private Environment env;
    private String dbPath;

    @Autowired
    public Db4oConfig(Environment env) {
        this.env = env;
        dbPath = this.env.getProperty("db.path");
    }

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

}
