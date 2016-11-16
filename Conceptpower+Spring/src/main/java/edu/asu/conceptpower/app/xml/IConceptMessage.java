package edu.asu.conceptpower.app.xml;

import java.util.List;
import java.util.Map;

import org.springframework.validation.ObjectError;

import com.fasterxml.jackson.core.JsonProcessingException;

import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptType;

public interface IConceptMessage {

    public String getAllConceptEntries(Map<ConceptEntry, ConceptType> entries) throws JsonProcessingException;

    public String getAllConceptEntriesAndPaginationDetails(Map<ConceptEntry, ConceptType> entries,
            Pagination pagination) throws JsonProcessingException;

    public String appendErrorMessages(List<ObjectError> errors) throws JsonProcessingException;

    public String appendErrorMessage(String errorMessage) throws JsonProcessingException;

}

