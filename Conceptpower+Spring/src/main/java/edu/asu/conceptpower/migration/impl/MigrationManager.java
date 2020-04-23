package edu.asu.conceptpower.migration.impl;

import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * @author Keerthivasan
 * 
 */

@Service
public class MigrationManager{
    
    @Autowired                                                                                                                                                                                                                                
    private MigrateToSql migrateToSql;
    
    private Future<MigrationResult> migrationResult;
        
    public void runMigrations(String username) {
        
        
    }
}