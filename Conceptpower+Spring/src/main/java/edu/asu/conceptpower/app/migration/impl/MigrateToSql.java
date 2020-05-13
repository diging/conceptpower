package edu.asu.conceptpower.app.migration.impl;

import java.util.Arrays;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import edu.asu.conceptpower.app.core.IConceptTypeManger;
import edu.asu.conceptpower.app.repository.impl.ConceptTypeRepository;
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
    private ConceptTypeRepository conceptTypeRepository;
    
    @Async
    public Future<MigrationResult> migrateReviewRequestData(){
        ConceptType[] conceptTypeDump = typesManager.getAllTypes();
        if(conceptTypeDump != null && conceptTypeDump.length > 0) {
            conceptTypeRepository.saveAll(Arrays.asList(conceptTypeDump));
        }
        
        return null;//new AsyncResult<MigrationResult>(new MigrationResult());
    }
}