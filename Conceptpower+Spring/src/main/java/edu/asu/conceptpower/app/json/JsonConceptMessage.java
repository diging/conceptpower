package edu.asu.conceptpower.app.json;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.validation.ObjectError;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.asu.conceptpower.app.xml.IConceptMessage;
import edu.asu.conceptpower.app.xml.Pagination;
import edu.asu.conceptpower.app.xml.URIHelper;
import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptType;
import edu.asu.conceptpower.servlet.core.ChangeEvent;

/**
 * This class helps to convert the given object to json. This class loads the
 * data from concept entry to concept message. If pagination details are passed,
 * then pagination details and concept message details are added to
 * ConceptMessagePagination class and final json is created.
 * 
 * @author karthikeyanmohan
 *
 */
public class JsonConceptMessage implements IConceptMessage {

    private URIHelper uriCreator;

    public JsonConceptMessage(URIHelper uriCreator) {
        this.uriCreator = uriCreator;
    }

    public String getAllConceptEntries(Map<ConceptEntry, ConceptType> entries) throws JsonProcessingException {
        return getAllConceptEntriesAndPaginationDetails(entries, null);
    }

    private ConceptMessage getConceptMessage(ConceptEntry entry, ConceptType type) {

        ConceptMessage json = new ConceptMessage();

        json.setId(entry.getId());
        json.setConceptUri(uriCreator.getURI(entry));
        json.setLemma(entry.getWord());
        json.setPos(entry.getPos());
        json.setDescription(entry.getDescription());
        json.setConceptList(entry.getConceptList());

        if (entry.getChangeEvents() != null && !entry.getChangeEvents().isEmpty()) {
            List<ChangeEvent> changeEvents = entry.getChangeEvents();
            Collections.sort(changeEvents);
            json.setCreatorId(changeEvents.get(0).getUserName() != null ? changeEvents.get(0).getUserName().trim() : "");
        } else {
            json.setCreatorId(entry.getCreatorId() != null ? entry.getCreatorId().trim() : "");
        }

        json.setEqualTo(entry.getEqualTo() != null ? entry.getEqualTo().trim() : "");
        json.setModifiedBy(entry.getModified() != null ? entry.getModified().trim() : "");
        json.setSimilarTo(entry.getSimilarTo() != null ? entry.getSimilarTo().trim() : "");
        json.setSynonymIds(entry.getSynonymIds() != null ? entry.getSynonymIds().trim() : "");

        if (type != null) {
            ConceptTypeJson jsonType = new ConceptTypeJson();
            jsonType.setTypeId(type.getTypeId());
            jsonType.setTypeUri(uriCreator.getTypeURI(type));
            jsonType.setTypeName(type.getTypeName());

            json.setType(jsonType);
        }

        json.setDeleted(entry.isDeleted());
        json.setWordnetId(entry.getWordnetId() != null ? entry.getWordnetId().trim() : "");
        
        if (entry.getAlternativeIds() != null && !entry.getAlternativeIds().isEmpty()) {
            Map<String, String> uriMap = uriCreator.getUrisBasedOnIds(entry.getAlternativeIds());
            if (uriMap != null && !uriMap.isEmpty()) {
                List<AlternativeId> alternativeIds = new ArrayList<>();
                for (Map.Entry<String, String> uri : uriMap.entrySet()) {
                    AlternativeId alternativeId = new AlternativeId();
                    alternativeId.setConceptId(uri.getKey());
                    alternativeId.setConceptUri(uri.getValue());
                    alternativeIds.add(alternativeId);
                }
                json.setAlternativeIds(alternativeIds);
            }
        }
        return json;
    }

    @Override
    public String appendErrorMessages(List<ObjectError> errors) throws JsonProcessingException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String appendErrorMessage(String errorMessage) throws JsonProcessingException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getAllConceptEntriesAndPaginationDetails(Map<ConceptEntry, ConceptType> entries,
            Pagination pagination) throws JsonProcessingException {
        List<ConceptMessage> conceptMessages = new ArrayList<>();
        for (ConceptEntry entry : entries.keySet()) {
            conceptMessages.add(getConceptMessage(entry, entries.get(entry)));
        }
        ObjectMapper mapper = new ObjectMapper();
        if (pagination != null) {
            ConceptMessagePagination conceptMessagePagination = new ConceptMessagePagination();
            conceptMessagePagination.setConceptEntries(conceptMessages);
            conceptMessagePagination.setPagination(pagination);
            return mapper.writeValueAsString(conceptMessagePagination);
        }
        return mapper.writeValueAsString(conceptMessages);
    }

}
