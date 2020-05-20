package edu.asu.conceptpower.app.migration.impl;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import edu.asu.conceptpower.app.core.IConceptTypeManger;
import edu.asu.conceptpower.app.core.model.impl.ReviewRequest;
import edu.asu.conceptpower.app.core.service.impl.ReviewRequestService;
import edu.asu.conceptpower.core.ConceptType;

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
    private ReviewRequestService reviewRequestService;
    
    @Async
    public Future<MigrationResult> migrateReviewRequestData(){
        ConceptType[] conceptTypeDump = typesManager.getAllTypes();
        if(conceptTypeDump != null && conceptTypeDump.length > 0) {
            List<ReviewRequest> r = reviewRequestService.findAll();
            System.out.println("Number of rows returned:"+r.size());
        }
        return new AsyncResult<MigrationResult>(new MigrationResult(0, ZonedDateTime.now()));
    }
}