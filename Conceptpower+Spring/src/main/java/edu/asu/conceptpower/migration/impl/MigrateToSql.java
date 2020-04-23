package edu.asu.conceptpower.migration.impl;

import java.util.concurrent.Future;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

/**
 * 
 * @author Keerthivasan
 * 
 */
@Service
public class MigrateToSql {
    
    @Async
    public Future<MigrationResult> migrateReviewRequestData(){
        return new AsyncResult<MigrationResult>(new MigrationResult());
    }
}