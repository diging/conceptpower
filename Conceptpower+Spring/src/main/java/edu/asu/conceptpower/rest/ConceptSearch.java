package edu.asu.conceptpower.rest;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;

import edu.asu.conceptpower.app.core.IIndexService;
import edu.asu.conceptpower.app.db.TypeDatabaseClient;
import edu.asu.conceptpower.app.exceptions.IndexerRunningException;
import edu.asu.conceptpower.app.exceptions.LuceneException;
import edu.asu.conceptpower.app.util.URIHelper;
import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptType;
import edu.asu.conceptpower.rest.msg.IConceptMessage;
import edu.asu.conceptpower.rest.msg.MessageRegistry;
import edu.asu.conceptpower.rest.msg.Pagination;

/**
 * This class provides a method to search concepts. It answers requests to:
 * "http://[server.url]/conceptpower/rest/ConceptSearch?{list of fieldname=value}{operator=and/or}"
 * 
 * @author Chetan, Julia Damerow
 * 
 */
@Controller
@PropertySource("classpath:config.properties")
public class ConceptSearch {

    @Autowired
    private IIndexService manager;

    @Autowired
    private TypeDatabaseClient typeManager;

    @Autowired
    private MessageRegistry messageFactory;

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
     * @throws JsonProcessingException
     */
    @RequestMapping(value = "/ConceptSearch", method = RequestMethod.GET, produces = {
            MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public @ResponseBody ResponseEntity<String> searchConcept(HttpServletRequest req,
            @Validated ConceptSearchParameters conceptSearchParameters, BindingResult result,
            @RequestHeader(value = "Accept", defaultValue = MediaType.APPLICATION_XML_VALUE) String acceptHeader)
                    throws JsonProcessingException, IllegalArgumentException, IllegalAccessException {

        if (result.hasErrors()) {
            IConceptMessage msg = messageFactory.getMessageFactory(acceptHeader).createConceptMessage();
            String errorMessage = msg.getErrorMessages(result.getAllErrors());
            return new ResponseEntity<String>(errorMessage, HttpStatus.BAD_REQUEST);
        }
        Map<String, String> searchFields = new HashMap<String, String>();
        String operator = SearchParamters.OP_OR;

        int page = 1;

        for (Field field : conceptSearchParameters.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (field.getName().equalsIgnoreCase("type_uri")) {
                if (field.get(conceptSearchParameters) != null) {
                    searchFields.put("type_id",
                            uriHelper.getTypeId(String.valueOf(field.get(conceptSearchParameters))));
                }
            } else if (SearchParamters.OPERATOR.equalsIgnoreCase(field.getName())) {
                // If the value is null, then operator will be OR by default
                if (field.get(conceptSearchParameters) != null) {
                    operator = String.valueOf(field.get(conceptSearchParameters)).toUpperCase();
                }
            } else if (SearchParamters.PAGE.equalsIgnoreCase(field.getName())) {
                page = field.get(conceptSearchParameters) != null ? (Integer) field.get(conceptSearchParameters) : page;
            } else if (SearchParamters.NUMBER_OF_RECORDS_PER_PAGE.equalsIgnoreCase(field.getName())) {
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
            searchResults = manager.searchForConceptByPageNumberAndFieldMap(searchFields, operator, page,
                    numberOfRecordsPerPage);
        } catch (LuceneException ex) {
            logger.error("Lucene Exception", ex);
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IndexerRunningException ire) {
            logger.info("Indexer running exception", ire);
            return new ResponseEntity<String>(ire.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }

        if (searchResults.length == 0) {
            // Data is not found but still returning OK as per review comments
            IConceptMessage msg = messageFactory.getMessageFactory(acceptHeader).createConceptMessage();
            return new ResponseEntity<String>(msg.getErrorMessage("No records found for the search condition."),
                    HttpStatus.OK);
        }
        Map<ConceptEntry, ConceptType> entryMap = new LinkedHashMap<ConceptEntry, ConceptType>();
        IConceptMessage msg = messageFactory.getMessageFactory(acceptHeader).createConceptMessage();
        createEntryMap(searchResults, entryMap);
        Pagination pagination = new Pagination(page, totalNumberOfRecords);
        return new ResponseEntity<String>(msg.getAllConceptEntriesAndPaginationDetails(entryMap, pagination),
                HttpStatus.OK);
    }

    private void createEntryMap(ConceptEntry[] searchResults, Map<ConceptEntry, ConceptType> entryMap) {
        for (ConceptEntry entry : searchResults) {
            ConceptType type = null;
            if (typeManager != null && entry.getTypeId() != null && !entry.getTypeId().trim().isEmpty()) {
                type = typeManager.getType(entry.getTypeId());
            }
            entryMap.put(entry, type);
        }
    }
}
