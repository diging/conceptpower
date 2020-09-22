package edu.asu.conceptpower.app.migration.impl;

import java.time.ZonedDateTime;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import edu.asu.conceptpower.app.core.IConceptListManager;
import edu.asu.conceptpower.app.core.IConceptTypeManger;
import edu.asu.conceptpower.app.core.IRequestsManager;
import edu.asu.conceptpower.app.db4o.IConceptDBManager;
import edu.asu.conceptpower.app.repository.IConceptEntryRepository;
import edu.asu.conceptpower.app.repository.IConceptListRepository;
import edu.asu.conceptpower.app.repository.IConceptTypeRepository;
import edu.asu.conceptpower.app.repository.IReviewRequestRepository;
import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptList;
import edu.asu.conceptpower.core.ConceptType;
import edu.asu.conceptpower.core.ReviewRequest;

/**
 * Service to perform SQL migration for all the tables in Conceptpower
 * 
 * @author Keerthivasan Krishnamurthy
 * 
 */

@Service
public class MigrateToSql {
    
    @Autowired
    private IConceptTypeManger typesManager;
    
    @Autowired
    private IConceptListManager listManager;
    
    @Autowired
    private IRequestsManager reviewRequestManager;
    
    @Autowired
    private IConceptDBManager conceptManager;
    
    @Autowired
    private IReviewRequestRepository reviewRequestRepository;
    
    @Autowired
    private IConceptTypeRepository conceptTypeRepository;
    
    @Autowired
    private IConceptListRepository conceptListRepository;
     
    @Autowired
    private IConceptEntryRepository conceptEntryRepository;
    
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

        return new AsyncResult<>(new MigrationResult(fileCount, ZonedDateTime.now()));
    }
    
    @Async
    public int migrateReviewRequestTable(){
        int count = 0;
        
        for(ReviewRequest r: reviewRequestManager.getAllReviews()) {
            /* Re map and save the model to the database */
            reviewRequestRepository.save(modelMapperUtil.mapReviewRequest(r));
            count++;
        }
        
        return count;
    }
    
    
    @Async
    public int migrateConceptTypeTable() {
        int count = 0;
        
        for(ConceptType c : typesManager.getAllTypes()) {
          conceptTypeRepository.save(modelMapperUtil.mapConceptType(c));
          count++;
        }
        
        return count;
    }
    
    @Async
    public int migrateConceptListTable() {
        int count = 0;
        
        for(ConceptList c:listManager.getAllConceptLists()) {
            conceptListRepository.save(modelMapperUtil.mapConceptList(c));
            count++;
        }
        
        return count;
    }
    
    @Async
    public int migrateConceptEntryTable(){
        int count = 0;
        
        for(ConceptEntry c : conceptManager.getAllConcepts()) {
            conceptEntryRepository.save(modelMapperUtil.mapConceptEntry(c));
            count++;
        }
        
        return count;
    }
}