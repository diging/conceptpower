package edu.asu.conceptpower.app.migration.impl;

import java.util.Set;

import org.springframework.stereotype.Component;

import edu.asu.conceptpower.app.model.ChangeEvent.ChangeEventTypes;
import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptList;
import edu.asu.conceptpower.core.ConceptType;
import edu.asu.conceptpower.core.ReviewRequest;
import edu.asu.conceptpower.servlet.core.ChangeEvent;

/**
 * Util Component to map the models from db4o version to SQL version
 * 
 * @author Keerthivasan Krishnamurthy
 * 
 */

@Component
public class ModelMapperUtil {
    
    public edu.asu.conceptpower.app.model.ConceptType mapConceptType(ConceptType conceptType){  
        edu.asu.conceptpower.app.model.ConceptType mappedConceptType = new edu.asu.conceptpower.app.model.ConceptType();
        mappedConceptType.setTypeId(conceptType.getTypeId());
        mappedConceptType.setCreatorId(conceptType.getCreatorId());
        mappedConceptType.setTypeName(conceptType.getTypeName());
        mappedConceptType.setDescription(conceptType.getDescription());
        mappedConceptType.setMatches(conceptType.getMatches());
        mappedConceptType.setModified(conceptType.getModified());
        mappedConceptType.setSupertypeId(conceptType.getSupertypeId());
        
        return mappedConceptType;
    }
   
    public edu.asu.conceptpower.app.model.ConceptList mapConceptList(ConceptList conceptList){
        edu.asu.conceptpower.app.model.ConceptList mappedConceptList= new edu.asu.conceptpower.app.model.ConceptList();
        mappedConceptList.setConceptListName(conceptList.getConceptListName());
        mappedConceptList.setDescription(conceptList.getDescription());
        mappedConceptList.setId(conceptList.getId());
        
        return mappedConceptList;
    }
    
    public edu.asu.conceptpower.app.model.ReviewRequest mapReviewRequest(ReviewRequest reviewRequest){
        
        edu.asu.conceptpower.app.model.ReviewRequest mappedRequest = new edu.asu.conceptpower.app.model.ReviewRequest();
        mappedRequest.setConceptId(reviewRequest.getConceptId());
        mappedRequest.setCreatedAt(reviewRequest.getCreatedAt());
        mappedRequest.setId(reviewRequest.getId());
        mappedRequest.setRequest(reviewRequest.getRequest());
        mappedRequest.setRequester(reviewRequest.getRequester());
        mappedRequest.setResolver(reviewRequest.getResolver());
        mappedRequest.setStatus(reviewRequest.getStatus());
        
        return mappedRequest;
    }
    
    public edu.asu.conceptpower.app.model.ConceptEntry mapConceptEntry(ConceptEntry conceptEntry) {
        
        edu.asu.conceptpower.app.model.ConceptEntry mappedConceptEntry = new edu.asu.conceptpower.app.model.ConceptEntry();
        mappedConceptEntry.setId(conceptEntry.getId());
        mappedConceptEntry.setWordnetId(conceptEntry.getWordnetId());
        mappedConceptEntry.setWord(conceptEntry.getWord());
        mappedConceptEntry.setDescription(conceptEntry.getDescription());
        mappedConceptEntry.setPos(conceptEntry.getPos());
        mappedConceptEntry.setConceptList(conceptEntry.getConceptList());
        mappedConceptEntry.setTypeId(conceptEntry.getTypeId());
        mappedConceptEntry.setEqualTo(conceptEntry.getEqualTo());
        mappedConceptEntry.setSimilarTo(conceptEntry.getSimilarTo());
        mappedConceptEntry.setSynonymIds(conceptEntry.getSynonymIds());
        mappedConceptEntry.setSynsetIds(conceptEntry.getSynsetIds());
        mappedConceptEntry.setNarrows(conceptEntry.getNarrows());
        mappedConceptEntry.setBroadens(conceptEntry.getBroadens());
        mappedConceptEntry.setCreatorId(conceptEntry.getCreatorId());
        mappedConceptEntry.setModified(conceptEntry.getModified());
        mappedConceptEntry.setMergedIds(conceptEntry.getMergedIds());
        mappedConceptEntry.setDeleted(conceptEntry.isDeleted());
        mappedConceptEntry.setModifiedUser(conceptEntry.getModifiedUser());
        mappedConceptEntry.setAlternativeIds(setToCommaString(conceptEntry.getAlternativeIds()));
        
        for(ChangeEvent changeEvent: conceptEntry.getChangeEvents()) {
            mappedConceptEntry.setChangeEvents(mapChangeEvent(changeEvent));
        }

        return mappedConceptEntry;
    }
    
    private String setToCommaString(Set<String> alternativeIds) {
        StringBuilder sb = new StringBuilder();
        
        for(String s: alternativeIds) {
            sb.append(s).append(",");
        }
        
        if(sb.length() > 0) {
           return sb.substring(0,sb.length()-1); 
        }
        return null;
    }
    
    private edu.asu.conceptpower.app.model.ChangeEvent mapChangeEvent(ChangeEvent changeEvent){
       
        edu.asu.conceptpower.app.model.ChangeEvent mappedChangeEvent = new edu.asu.conceptpower.app.model.ChangeEvent();
        mappedChangeEvent.setType(ChangeEventTypes.values()[changeEvent.getType().ordinal()]);
        mappedChangeEvent.setUserName(changeEvent.getUserName());
        mappedChangeEvent.setDate(changeEvent.getDate());

        return mappedChangeEvent;
    }
}