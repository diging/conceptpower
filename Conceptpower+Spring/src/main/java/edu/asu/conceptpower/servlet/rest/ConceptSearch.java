package edu.asu.conceptpower.servlet.rest;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptType;
import edu.asu.conceptpower.root.TypeDatabaseClient;
import edu.asu.conceptpower.root.URIHelper;
import edu.asu.conceptpower.servlet.core.IIndexService;
import edu.asu.conceptpower.servlet.exceptions.IndexerRunningException;
import edu.asu.conceptpower.servlet.exceptions.LuceneException;
import edu.asu.conceptpower.servlet.xml.XMLConceptMessage;
import edu.asu.conceptpower.servlet.xml.XMLMessageFactory;

/**
 * This class provides a method to search concepts. It answers requests to:
 * "http://[server.url]/conceptpower/rest/ConceptSearch?{list of fieldname=value}{operator=and/or}"
 * 
 * @author Chetan, Julia Damerow
 * 
 */
@Controller
public class ConceptSearch {

    @Autowired
    private IIndexService manager;

    @Autowired
    private TypeDatabaseClient typeManager;

    @Autowired
    private XMLMessageFactory messageFactory;

    @Autowired
    private URIHelper uriHelper;

    @Value("${default_page_size}")
    private int numberOfRecordsPerPage;

    @Autowired
    private ConceptSearchParameterValidator validator;

    @InitBinder
    private void initBinder(WebDataBinder binder) {
        binder.setValidator(validator);
    }

    private static final Logger logger = LoggerFactory.getLogger(ConceptSearch.class);

    /**
     * This method provides information of a concept for a rest interface of the
     * form
     * "http://[server.url]/conceptpower/rest/Type?id={URI or ID of concept}"
     * 
     * @param req
     *            Holds HTTP request information
     * @return
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    @RequestMapping(value = "rest/ConceptSearch", method = RequestMethod.GET, produces = "application/xml; charset=utf-8")
    public @ResponseBody ResponseEntity<String> searchConcept(
            @Validated ConceptSearchParameters conceptSearchParameters,
            BindingResult result)
            throws IllegalArgumentException, IllegalAccessException {

        List<String> xmlEntries = new ArrayList<String>();

        if (result.hasErrors()) {
            XMLConceptMessage msg = messageFactory.createXMLConceptMessage();
            for (ObjectError error : result.getAllErrors()) {
                xmlEntries.add(msg.appendErrorMessage(error.getDefaultMessage()));
            }
            return new ResponseEntity<String>(msg.getXML(xmlEntries), HttpStatus.BAD_REQUEST);
        }

        Map<String, String> searchFields = new HashMap<String, String>();
        String operator = SearchParamters.OP_OR;
        int page = 1;

        for (Field field : conceptSearchParameters.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (field.getName().equalsIgnoreCase("type_uri")) {
                searchFields.put("type_id",
                        uriHelper.getTypeId(String.valueOf(field.get(conceptSearchParameters))));
            } else if (SearchParamters.OPERATOR.equalsIgnoreCase(field.getName())) {
                // If the value is null, then operator will be OR by default
                if (field.get(conceptSearchParameters) != null) {
                    operator = String.valueOf(field.get(conceptSearchParameters));
                }
            } else if (SearchParamters.PAGE.equalsIgnoreCase(field.getName())) {
                page = field.get(conceptSearchParameters) != null
                        ? (Integer) field.get(conceptSearchParameters) : page;
            } else if (SearchParamters.NUMBER_OF_RECORDS_PER_PAGE
                    .equalsIgnoreCase(field.getName())) {
                numberOfRecordsPerPage = field.get(conceptSearchParameters) != null
                        ? (Integer) field.get(conceptSearchParameters) : numberOfRecordsPerPage;
            } else if (field.get(conceptSearchParameters) != null) {
                searchFields.put(field.getName().trim(), String.valueOf(field.get(conceptSearchParameters)));
            }
        }

        ConceptEntry[] searchResults = null;

        int totalNumberOfRecords = 0;
        try {
            totalNumberOfRecords = manager.getTotalNumberOfRecordsForSearch(searchFields, operator);
            searchResults = manager.searchForConceptByPageNumberAndFieldMap(searchFields, operator,
                    page, numberOfRecordsPerPage);
        } catch (LuceneException ex) {
            logger.error("Lucene Exception", ex);
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IllegalAccessException iae) {
            logger.error("Illegal access exception", iae);
            return new ResponseEntity<String>(iae.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (IndexerRunningException ire) {
            logger.info("Indexer running exception", ire);
            return new ResponseEntity<String>(ire.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }

        if (searchResults.length == 0) {
            // Data is not found but still returning OK as per review comments
            XMLConceptMessage msg = messageFactory.createXMLConceptMessage();
            xmlEntries.add(msg.appendErrorMessage("No records found for the search condition."));
            return new ResponseEntity<String>(msg.getXML(xmlEntries), HttpStatus.OK);
        }
        
        Map<ConceptEntry, ConceptType> entryMap = new HashMap<ConceptEntry, ConceptType>();

        XMLConceptMessage msg = messageFactory.createXMLConceptMessage();
        for (ConceptEntry entry : searchResults) {
            ConceptType type = null;
            if (typeManager != null && entry.getTypeId() != null && !entry.getTypeId().trim().isEmpty()) {
                type = typeManager.getType(entry.getTypeId());
            }
            entryMap.put(entry, type);
            xmlEntries = msg.appendEntries(entryMap);
        }

        // Append the number of records when user fetches the first page.
        // Total number of records is returned only for first page.
        xmlEntries.add(msg.appendNumberOfRecords(totalNumberOfRecords));

        return new ResponseEntity<String>(msg.getXML(xmlEntries), HttpStatus.OK);
    }

}
