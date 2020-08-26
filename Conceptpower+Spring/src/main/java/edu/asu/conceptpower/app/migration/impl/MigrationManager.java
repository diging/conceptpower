package edu.asu.conceptpower.app.migration.impl;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * 
 * @author Keerthivasan
 * 
 */

@Service
@Component("migrationService")
public class MigrationManager{
    
    @Autowired                                                                                                                                                                                                                                
    private MigrateToSql migrateToSql;
    
    private Future<MigrationResult> migrationResult;
        
    public void runMigrations() {
        migrationResult = migrateToSql.migrateConceptTypeTable();
    }
    
    public MigrationResult migrationStatus() throws InterruptedException, ExecutionException {
        if(migrationResult == null) {
            return new MigrationResult(0, null);
        }
        if(migrationResult.isDone()) {
            return migrationResult.get();
        }
        return null;
    }
}