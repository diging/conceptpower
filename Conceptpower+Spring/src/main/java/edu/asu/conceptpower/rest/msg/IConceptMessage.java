package edu.asu.conceptpower.rest.msg;

import java.util.List;
import java.util.Map;

import org.springframework.validation.ObjectError;

import com.fasterxml.jackson.core.JsonProcessingException;

import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptType;

/**
 * This class acts as an interface for all concept messages. Currently concepts
 * are returned in json and xml format. So we have XmlConceptMessage and
 * JsonConceptMessage implementing this class
 * 
 * @author karthik
 *
 */
public interface IConceptMessage {

    /**
     * This method checks for the validation errors and returns the concept
     * entries in JSON or XML format.
     * 
     * @param entries
     * @return
     * @throws JsonProcessingException
     */
    public String getAllConceptEntries(Map<ConceptEntry, ConceptType> entries) throws JsonProcessingException;

    /**
     * This method fetches the concept entries with the specified pagination.
     * 
     * @param entries
     * @param pagination
     * @return
     * @throws JsonProcessingException
     */
    public String getAllConceptEntriesAndPaginationDetails(Map<ConceptEntry, ConceptType> entries,
            Pagination pagination) throws JsonProcessingException;

    /**
     * This method provides the error messages in JSON and XML format
     * respectively with the supplied error details.
     * 
     * @param errors
     * @return
     * @throws JsonProcessingException
     */
    public String getErrorMessages(List<ObjectError> errors) throws JsonProcessingException;

    /**
     * This method provides the error message in JSON and XML format.
     * 
     * @param errorMessage
     * @return
     * @throws JsonProcessingException
     */
    public String getErrorMessage(String errorMessage) throws JsonProcessingException;

}

