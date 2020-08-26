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
import edu.asu.conceptpower.app.core.IRequestsManager;
import edu.asu.conceptpower.app.core.model.impl.ReviewRequest;
import edu.asu.conceptpower.app.core.service.impl.ConceptListService;
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
    private IRequestsManager reviewRquestManager;
    
    @Autowired
    private ReviewRequestService reviewRequestService;
    
    @Autowired
    private ConceptTypeService conceptTypeService;
    
    @Autowired
    private ConceptListService conceptListService;
    
    @Async
    public Future<MigrationResult> migrateReviewRequestTable(){
        ConceptType[] conceptTypeDump = typesManager.getAllTypes();
        
        if(conceptTypeDump != null && conceptTypeDump.length > 0) {
            List<ReviewRequest> r = reviewRequestService.findAll();
            System.out.println("Number of rows returned:"+r.size());
        }
        
        return new AsyncResult<>(new MigrationResult(0, ZonedDateTime.now()));
    }
    
    
    public Future<MigrationResult> migrateConceptTypeTable() {
        ConceptType[] conceptTypeDump = typesManager.getAllTypes();
        if(conceptTypeDump != null && conceptTypeDump.length > 0) {
            List<edu.asu.conceptpower.app.core.model.impl.ConceptType> mappedConceptType = conceptTypeMapper(conceptTypeDump);
            for(edu.asu.conceptpower.app.core.model.impl.ConceptType c : mappedConceptType) {
                conceptTypeService.create(c);
            }
        }
        
        return new AsyncResult<>(new MigrationResult(0, ZonedDateTime.now()));
    }
    
    public Future<MigrationResult> migrateConceptListTable() {
        List<ConceptList> conceptListDump = listManager.getAllConceptLists();
        
        if(conceptListDump != null && !conceptListDump.isEmpty()) {
            List<edu.asu.conceptpower.app.core.model.impl.ConceptList> tempConceptList = conceptListMapper(conceptListDump);
            
            for(edu.asu.conceptpower.app.core.model.impl.ConceptList c : tempConceptList) {
                conceptListService.create(c);
            }
        }
        
        return new AsyncResult<>(new MigrationResult(0, ZonedDateTime.now()));
    }
    
    
    private List<edu.asu.conceptpower.app.core.model.impl.ConceptType> conceptTypeMapper(ConceptType[] conceptTypeList){
        
        List<edu.asu.conceptpower.app.core.model.impl.ConceptType> mappedConceptType = new ArrayList<>();
        
        for(ConceptType conceptType : conceptTypeList) {
            edu.asu.conceptpower.app.core.model.impl.ConceptType tempConceptType = new edu.asu.conceptpower.app.core.model.impl.ConceptType();
            tempConceptType.setTypeId(conceptType.getTypeId());
            tempConceptType.setCreatorId(conceptType.getCreatorId());
            tempConceptType.setTypeName(conceptType.getTypeName());
            tempConceptType.setDescription(conceptType.getDescription());
            tempConceptType.setMatches(conceptType.getMatches());
            tempConceptType.setModified(conceptType.getModified());
            tempConceptType.setSupertypeId(conceptType.getSupertypeId());
            
            mappedConceptType.add(tempConceptType);
        }
        return mappedConceptType;
    }
   
    private List<edu.asu.conceptpower.app.core.model.impl.ConceptList> conceptListMapper(List<ConceptList> conceptLists){
        
        List<edu.asu.conceptpower.app.core.model.impl.ConceptList> mappedConceptLists = new ArrayList<>();
        
        for(ConceptList conceptList : conceptLists) {
            edu.asu.conceptpower.app.core.model.impl.ConceptList tempConceptList= new edu.asu.conceptpower.app.core.model.impl.ConceptList();
            tempConceptList.setConceptListName(conceptList.getConceptListName());
            tempConceptList.setDescription(conceptList.getDescription());
            tempConceptList.setId(conceptList.getId());
            
            mappedConceptLists.add(tempConceptList);
        }
        return mappedConceptLists;
    }
    
    private List<edu.asu.conceptpower.app.core.model.impl.ReviewRequest> reviewRequestMapper(List<ReviewRequest> reviewRequests){
        List<edu.asu.conceptpower.app.core.model.impl.ReviewRequest> mappedReviewRequests = new ArrayList<>();
        
        for(ReviewRequest reviewRequest: reviewRequests) {
            edu.asu.conceptpower.app.core.model.impl.ReviewRequest tempReviewRequest = new edu.asu.conceptpower.app.core.model.impl.ReviewRequest();
            tempReviewRequest.setConceptId(reviewRequest.getConceptId());
            tempReviewRequest.setCreatedAt(reviewRequest.getCreatedAt());
            tempReviewRequest.setId(reviewRequest.getId());
            tempReviewRequest.setRequest(reviewRequest.getRequest());
            tempReviewRequest.setRequester(reviewRequest.getRequester());
            tempReviewRequest.setResolver(reviewRequest.getResolver());
            tempReviewRequest.setStatus(reviewRequest.getStatus());
            
            mappedReviewRequests.add(tempReviewRequest);
        }
        
        return mappedReviewRequests;
    }
}