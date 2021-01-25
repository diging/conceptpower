package edu.asu.conceptpower.app.migration.impl;

import java.time.ZonedDateTime;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import edu.asu.conceptpower.app.db.DatabaseManager;
import edu.asu.conceptpower.app.repository.IConceptEntryRepository;
import edu.asu.conceptpower.app.repository.IConceptListRepository;
import edu.asu.conceptpower.app.repository.IConceptTypeRepository;
import edu.asu.conceptpower.app.repository.IReviewRequestRepository;
import edu.asu.conceptpower.app.repository.IUserRepository;
import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptList;
import edu.asu.conceptpower.core.ConceptType;
import edu.asu.conceptpower.core.ReviewRequest;
import edu.asu.conceptpower.users.User;

/**
 * Service to perform SQL migration for all the tables in Conceptpower
 * 
 * @author Keerthivasan Krishnamurthy
 * 
 */

@Service
public class MigrateToSql {
    
    @Autowired
    @Qualifier("typesDatabaseManager")
    private DatabaseManager typeDatabase;
    
    @Autowired
    @Qualifier("conceptDatabaseManager")
    private DatabaseManager dictionary;
    
    @Autowired
    @Qualifier("conceptReviewDatabaseManager")
    private DatabaseManager dbManager;
    
    @Qualifier("userDatabaseManager")
    private DatabaseManager userDatabase;
    
    @Autowired
    private IReviewRequestRepository reviewRequestRepository;
    
    @Autowired
    private IConceptTypeRepository conceptTypeRepository;
    
    @Autowired
    private IConceptListRepository conceptListRepository;
     
    @Autowired
    private IConceptEntryRepository conceptEntryRepository;
    
    @Autowired
    private IUserRepository userRepository;
    
    @Autowired
    private ModelMapperUtil modelMapperUtil;
   
    @Async
    public Future<MigrationResult> migrateTable(){
        /*counter reference to keep track of cumulative number of row insertions*/
        int fileCount = 0;
     
        /* Review Request Table */
        fileCount += migrateReviewRequestTable();
        
        /* Concept Entry Table */
        fileCount += migrateConceptEntryTable();
         
        /* Concept List Table */
        fileCount += migrateConceptListTable();
        
        /* Concept Type Table */
        fileCount += migrateConceptTypeTable();
        
        /*User Table*/
        fileCount += migrateUserTable();

        return new AsyncResult<>(new MigrationResult(fileCount, ZonedDateTime.now()));
    }
    
    @Async
    public int migrateReviewRequestTable(){
        int count = 0;
        
        for(ReviewRequest r: dbManager.getClient().query(ReviewRequest.class)) {
            /* Re map and save the model to the database */
            reviewRequestRepository.save(modelMapperUtil.mapReviewRequest(r));
            count++;
        }
        
        return count;
    }
    
    
    @Async
    public int migrateConceptTypeTable() {
        int count = 0;
        
        for(ConceptType c : typeDatabase.getClient().query(ConceptType.class)) {
            conceptTypeRepository.save(modelMapperUtil.mapConceptType(c));
            count++;
        }
        
        return count;
    }
    
    @Async
    public int migrateConceptListTable() {
        int count = 0;
        
        for(ConceptList c: dictionary.getClient().query(ConceptList.class)) {
            conceptListRepository.save(modelMapperUtil.mapConceptList(c));
            count++;
        }
        
        return count;
    }
    
    @Async
    public int migrateConceptEntryTable(){
        int count = 0;
        
        for(ConceptEntry c : dictionary.getClient().query(ConceptEntry.class)) {
            conceptEntryRepository.save(modelMapperUtil.mapConceptEntry(c));
            count++;
        }
        
        return count;
    }
    
    @Async
    public int migrateUserTable() {
        int count = 0;
        
        for(User u: userDatabase.getClient().query(User.class)) {
            userRepository.save(modelMapperUtil.mapUser(u));
        }
        
        return count;
    }
}