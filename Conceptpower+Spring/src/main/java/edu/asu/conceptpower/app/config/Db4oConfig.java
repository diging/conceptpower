package edu.asu.conceptpower.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import edu.asu.conceptpower.app.db.DatabaseManager;

@Configuration
public class Db4oConfig {
    
    @Bean(initMethod = "init", destroyMethod = "close")
    public DatabaseManager userDatabaseManager() {
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.setDatabasePath("${db.path}/users.db");
        return databaseManager;
    }
    
    @Bean(initMethod = "init", destroyMethod = "close")
    public DatabaseManager conceptDatabaseManager() {
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.setDatabasePath("${db.path}/conceptLists.db");
        return databaseManager;
    }
    
    @Bean(initMethod = "init", destroyMethod = "close")
    public DatabaseManager typesDatabaseManager() {
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.setDatabasePath("${db.path}/conceptTypes.db");
        return databaseManager;
    }
    
    @Bean(initMethod = "init", destroyMethod = "close")
    public DatabaseManager luceneDatabaseManager() {
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.setDatabasePath("${db.path}/lucene.db");
        return databaseManager;
    }
    
    @Bean(initMethod = "init", destroyMethod = "close")
    public DatabaseManager conceptReviewDatabaseManager() {
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.setDatabasePath("${db.path}/conceptReview.db");
        return databaseManager;
    }

}
