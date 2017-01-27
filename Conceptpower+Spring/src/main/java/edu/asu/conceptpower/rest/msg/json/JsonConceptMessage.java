package edu.asu.conceptpower.rest.msg.json;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.validation.ObjectError;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.asu.conceptpower.app.util.URIHelper;
import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptType;
import edu.asu.conceptpower.rest.msg.IConceptMessage;
import edu.asu.conceptpower.rest.msg.Pagination;
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

    private ConceptEntryMessage getConceptMessage(ConceptEntry entry, ConceptType type) {

        ConceptEntryMessage json = new ConceptEntryMessage();

        json.setId(entry.getId());
        json.setConceptUri(uriCreator.getURI(entry));
        json.setLemma(entry.getWord());
        json.setPos(entry.getPos());
        json.setDescription(entry.getDescription());
        json.setConceptList(entry.getConceptList());

        if (entry.getChangeEvents() != null && !entry.getChangeEvents().isEmpty()) {
            List<ChangeEvent> changeEvents = entry.getChangeEvents();
            Collections.sort(changeEvents);
            json.setCreatorId(
                    changeEvents.get(0).getUserName() != null ? changeEvents.get(0).getUserName().trim() : "");
        } else {
            json.setCreatorId(entry.getCreatorId() != null ? entry.getCreatorId().trim() : "");
        }

        json.setEqualTo(entry.getEqualTo() != null ? entry.getEqualTo().trim() : "");
        json.setModifiedBy(entry.getModified() != null ? entry.getModified().trim() : "");
        json.setSimilarTo(entry.getSimilarTo() != null ? entry.getSimilarTo().trim() : "");
        json.setSynonymIds(entry.getSynonymIds() != null ? entry.getSynonymIds().trim() : "");

        if (type != null) {
            ConceptTypeMessage jsonType = new ConceptTypeMessage();
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
    public String getErrorMessages(List<ObjectError> errors) throws JsonProcessingException {
        List<ErrorMessage> errorMessages = new ArrayList<>();
        for (ObjectError error : errors) {
            ErrorMessage errorMessage = new ErrorMessage(error.getDefaultMessage());
            errorMessages.add(errorMessage);
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(errorMessages);
    }

    @Override
    public String getErrorMessage(String errorMessage) throws JsonProcessingException {
        ErrorMessage errorMessageObj = new ErrorMessage(errorMessage);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(errorMessageObj);
    }

    @Override
    public String getAllConceptEntriesAndPaginationDetails(Map<ConceptEntry, ConceptType> entries,
            Pagination pagination) throws JsonProcessingException {

        if (entries == null || entries.isEmpty()) {
            ErrorMessage errorMessage = new ErrorMessage("No concept entry found.");
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(errorMessage);
        }

        List<ConceptEntryMessage> conceptMessages = new ArrayList<>();
        for (ConceptEntry entry : entries.keySet()) {
            conceptMessages.add(getConceptMessage(entry, entries.get(entry)));
        }

        ConceptResult conceptResult = new ConceptResult();
        conceptResult.setConceptEntries(conceptMessages);
        if (pagination != null) {
            conceptResult.setPagination(pagination);
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(conceptResult);
    }

}
