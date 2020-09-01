package edu.asu.conceptpower.app.migration.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptList;
import edu.asu.conceptpower.core.ConceptType;
import edu.asu.conceptpower.core.ReviewRequest;
import edu.asu.conceptpower.servlet.core.ChangeEvent;

/**
 * 
 * @author Keerthivasan
 * 
 */

public class ModelMapperUtil {
    
    /*Avoid Instantiation of this utility class*/
    private ModelMapperUtil() {}
    
    public static List<edu.asu.conceptpower.app.core.model.impl.ConceptType> conceptTypeMapper(ConceptType[] conceptTypeList){  
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
   
    public static List<edu.asu.conceptpower.app.core.model.impl.ConceptList> conceptListMapper(List<ConceptList> conceptLists){
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
    
    public static List<edu.asu.conceptpower.app.core.model.impl.ReviewRequest> reviewRequestMapper(List<ReviewRequest> reviewRequests){
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
    
    public static List<edu.asu.conceptpower.app.core.model.impl.ConceptEntry> conceptEntryMapper(List<ConceptEntry> conceptEntries) {
        List<edu.asu.conceptpower.app.core.model.impl.ConceptEntry> mappedConceptEntries = new ArrayList<>();
        
        for(ConceptEntry conceptEntry: conceptEntries) {
            edu.asu.conceptpower.app.core.model.impl.ConceptEntry tempConceptEntry = new edu.asu.conceptpower.app.core.model.impl.ConceptEntry();
            tempConceptEntry.setId(conceptEntry.getId());
            tempConceptEntry.setWordnetId(conceptEntry.getWordnetId());
            tempConceptEntry.setWord(conceptEntry.getWord());
            tempConceptEntry.setDescription(conceptEntry.getDescription());
            tempConceptEntry.setPos(conceptEntry.getPos());
            tempConceptEntry.setConceptList(conceptEntry.getConceptList());
            tempConceptEntry.setTypeId(conceptEntry.getTypeId());
            tempConceptEntry.setEqualTo(conceptEntry.getEqualTo());
            tempConceptEntry.setSimilarTo(conceptEntry.getSimilarTo());
            tempConceptEntry.setSynonymIds(conceptEntry.getSynonymIds());
            tempConceptEntry.setSynsetIds(conceptEntry.getSynsetIds());
            tempConceptEntry.setNarrows(conceptEntry.getNarrows());
            tempConceptEntry.setBroadens(conceptEntry.getBroadens());
            tempConceptEntry.setCreatorId(conceptEntry.getCreatorId());
            tempConceptEntry.setModified(conceptEntry.getModified());
            tempConceptEntry.setMergedIds(conceptEntry.getMergedIds());
            tempConceptEntry.setDeleted(conceptEntry.isDeleted());
            tempConceptEntry.setModifiedUser(conceptEntry.getModifiedUser());
            tempConceptEntry.setAlternativeIds(setToCommaString(conceptEntry.getAlternativeIds()));
            tempConceptEntry.setChangeEvents(changeEventMapper(conceptEntry.getChangeEvents(),conceptEntry.getId()));

            mappedConceptEntries.add(tempConceptEntry);
        }
        
        return mappedConceptEntries;
    }
    
    private static String setToCommaString(Set<String> alternativeIds) {
        StringBuilder sb = new StringBuilder();
        
        for(String s: alternativeIds) {
            sb.append(s).append(",");
        }
        
        return sb.substring(0,sb.length()-1); 
    }
    
    private static List<edu.asu.conceptpower.app.core.model.impl.ChangeEvent> changeEventMapper(List<ChangeEvent> changeEvents, String conceptId){
        List<edu.asu.conceptpower.app.core.model.impl.ChangeEvent> mappedChangeEvents = new ArrayList<>();
        
        for(ChangeEvent changeEvent: changeEvents) {
            edu.asu.conceptpower.app.core.model.impl.ChangeEvent tempChangeEvent = new edu.asu.conceptpower.app.core.model.impl.ChangeEvent();
            tempChangeEvent.setType(changeEvent.getType());
            tempChangeEvent.setUserName(changeEvent.getUserName());
            tempChangeEvent.setDate(changeEvent.getDate());
            
            mappedChangeEvents.add(tempChangeEvent);
        }
        
        return mappedChangeEvents;
    }
}