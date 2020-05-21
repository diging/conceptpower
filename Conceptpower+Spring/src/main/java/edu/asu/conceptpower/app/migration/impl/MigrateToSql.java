package edu.asu.conceptpower.app.migration.impl;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import edu.asu.conceptpower.app.core.IConceptListManager;
import edu.asu.conceptpower.app.core.IConceptTypeManger;
import edu.asu.conceptpower.app.core.model.impl.ReviewRequest;
import edu.asu.conceptpower.app.core.service.impl.ConceptTypeService;
import edu.asu.conceptpower.app.core.service.impl.ReviewRequestService;
import edu.asu.conceptpower.core.ConceptList;
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
    private IConceptListManager listManager;
    
    @Autowired
    private ReviewRequestService reviewRequestService;
    
    @Autowired
    private ConceptTypeService conceptTypeService;
    
    @Async
    public Future<MigrationResult> migrateReviewRequestData(){
        ConceptType[] conceptTypeDump = typesManager.getAllTypes();
        if(conceptTypeDump != null && conceptTypeDump.length > 0) {
            List<ReviewRequest> r = reviewRequestService.findAll();
            System.out.println("Number of rows returned:"+r.size());
        }
        return new AsyncResult<MigrationResult>(new MigrationResult(0, ZonedDateTime.now()));
    }
    
    
    public Future<MigrationResult> migrateConceptType() {
        
        ConceptType[] conceptTypeDump = typesManager.getAllTypes();
        if(conceptTypeDump != null && conceptTypeDump.length > 0) {
            List<edu.asu.conceptpower.app.core.model.impl.ConceptType> mappedConceptType = mapConceptType(conceptTypeDump);
            for(edu.asu.conceptpower.app.core.model.impl.ConceptType c : mappedConceptType) {
                conceptTypeService.create(c);
            }
        }
        
        return new AsyncResult<MigrationResult>(new MigrationResult(0, ZonedDateTime.now()));
    }
    
    public Future<MigrationResult> migrateConceptList() {
    
        List<ConceptList> conceptListDump = listManager.getAllConceptLists();
        
        if(conceptListDump != null && conceptListDump.size() > 0) {

        }
        
        return new AsyncResult<MigrationResult>(new MigrationResult(0, ZonedDateTime.now()));
    }
    
    
    public List<edu.asu.conceptpower.app.core.model.impl.ConceptType> mapConceptType(ConceptType[] conceptTypeList){
        
        List<edu.asu.conceptpower.app.core.model.impl.ConceptType> mappedConceptType = new ArrayList<>();
        
        for(ConceptType conceptType : conceptTypeList) {
            edu.asu.conceptpower.app.core.model.impl.ConceptType temp = new edu.asu.conceptpower.app.core.model.impl.ConceptType();
            temp.setTypeId(conceptType.getTypeId());
            temp.setCreatorId(conceptType.getCreatorId());
            temp.setTypeName(conceptType.getTypeName());
            temp.setDescription(conceptType.getDescription());
            temp.setMatches(conceptType.getMatches());
            temp.setModified(conceptType.getModified());
            temp.setSupertypeId(conceptType.getSupertypeId());
            
            mappedConceptType.add(temp);
        }
        return mappedConceptType;
    }
}