package edu.asu.conceptpower.app.migration.impl;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import edu.asu.conceptpower.app.core.IConceptListManager;
import edu.asu.conceptpower.app.core.IConceptTypeManger;
import edu.asu.conceptpower.app.core.IRequestsManager;
import edu.asu.conceptpower.app.core.service.IConceptEntryService;
import edu.asu.conceptpower.app.core.service.IConceptListService;
import edu.asu.conceptpower.app.core.service.IConceptTypeService;
import edu.asu.conceptpower.app.core.service.IReviewRequestService;
import edu.asu.conceptpower.app.db4o.IConceptDBManager;
import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptList;
import edu.asu.conceptpower.core.ConceptType;
import edu.asu.conceptpower.core.ReviewRequest;

/**
 * 
 * @author Keerthivasan
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
    private IReviewRequestService reviewRequestService;
    
    @Autowired
    private IConceptTypeService conceptTypeService;
    
    @Autowired
    private IConceptListService conceptListService;
    
    @Autowired
    private IConceptEntryService conceptEntryService; 
    
    @Async
    public Future<MigrationResult> migrateTable(ConceptpowerTable table){
        int count = 0;
        
        switch(table) {
            case REVIEW_REQUEST:
                count = migrateReviewRequestTable();
                break;
            case CONCEPT_ENTRY:
                count = migrateConceptEntryTable();
                break;
            case CONCEPT_LIST:
                count = migrateConceptListTable();
                break;
            case CONCEPT_TYPE:
                count = migrateConceptTypeTable();
                break;
            default:
                break;
        }
        
        return new AsyncResult<>(new MigrationResult(count, ZonedDateTime.now()));
    }
    
    public int migrateReviewRequestTable(){
        List<ReviewRequest> reviewRequestsDump = reviewRequestManager.getAllReviews();
        int count = 0;
        if(reviewRequestsDump != null && !reviewRequestsDump.isEmpty()) {
            List<edu.asu.conceptpower.app.core.model.impl.ReviewRequest> mappedReviewRequest = ModelMapperUtil.reviewRequestMapper(reviewRequestsDump);
            for(edu.asu.conceptpower.app.core.model.impl.ReviewRequest r : mappedReviewRequest) {
                reviewRequestService.create(r);
                count++;
            }
        }
        
        return count;
    }
    
    
    @Async
    public int migrateConceptTypeTable() {
        ConceptType[] conceptTypeDump = typesManager.getAllTypes();
        int count = 0;
        
        if(conceptTypeDump != null && conceptTypeDump.length > 0) {
            List<edu.asu.conceptpower.app.core.model.impl.ConceptType> mappedConceptType = ModelMapperUtil.conceptTypeMapper(conceptTypeDump);
            
            for(edu.asu.conceptpower.app.core.model.impl.ConceptType c : mappedConceptType) {
                conceptTypeService.create(c);
                count++;
            }
        }
        
        return count;
    }
    
    @Async
    public int migrateConceptListTable() {
        List<ConceptList> conceptListDump = listManager.getAllConceptLists();
        int count = 0;
        
        if(conceptListDump != null && !conceptListDump.isEmpty()) {
            List<edu.asu.conceptpower.app.core.model.impl.ConceptList> mappedConceptList = ModelMapperUtil.conceptListMapper(conceptListDump);
            
            for(edu.asu.conceptpower.app.core.model.impl.ConceptList c : mappedConceptList) {
                conceptListService.create(c);
                count++;
            }
        }
        
        return count;
    }
    
    @Async
    public int migrateConceptEntryTable(){
        List<ConceptEntry> conceptEntryDump = conceptManager.getAllConcepts();
        int count = 0;
        
        if(conceptEntryDump != null && !conceptEntryDump.isEmpty()) {
            List<edu.asu.conceptpower.app.core.model.impl.ConceptEntry> mappedConceptEntries = ModelMapperUtil.conceptEntryMapper(conceptEntryDump);
            
            for(edu.asu.conceptpower.app.core.model.impl.ConceptEntry c : mappedConceptEntries) {
                conceptEntryService.create(c);
                count++;
            }
        }
        
        return count;
    }
    
    
}