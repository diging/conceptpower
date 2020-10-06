package edu.asu.conceptpower.rest.msg;

import com.fasterxml.jackson.core.JsonProcessingException;

import edu.asu.conceptpower.app.model.ConceptType;

/**
 * This interface provides method for converting the concept type to JSON or XML
 * format.
 * 
 * @author karthik
 *
 */
public interface ITypeMessage {

    /**
     * This method is used for converting the concept type and its corresponding
     * super type to JSON or XML format.
     * 
     * @param type
     * @param supertype
     * @return
     * @throws JsonProcessingException
     */
    public String getConceptTypeMessage(ConceptType type, ConceptType supertype) throws JsonProcessingException;

}
