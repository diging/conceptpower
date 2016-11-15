package edu.asu.conceptpower.rest;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;

import edu.asu.conceptpower.app.constants.SearchFieldNames;
import edu.asu.conceptpower.app.core.IIndexService;
import edu.asu.conceptpower.app.db.TypeDatabaseClient;
import edu.asu.conceptpower.app.exceptions.IndexerRunningException;
import edu.asu.conceptpower.app.exceptions.LuceneException;
import edu.asu.conceptpower.app.xml.IConceptMessage;
import edu.asu.conceptpower.app.xml.MessageRegistry;
import edu.asu.conceptpower.app.xml.URIHelper;
import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptType;

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
    private MessageRegistry messageFactory;

    @Autowired
    private URIHelper uriHelper;

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
    @RequestMapping(value = "rest/ConceptSearch", method = RequestMethod.GET, produces = {
            MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public @ResponseBody ResponseEntity<String> searchConcept(HttpServletRequest req,
            @RequestHeader(value = "Accept", defaultValue = MediaType.APPLICATION_XML_VALUE) String acceptHeader)
                    throws JsonProcessingException {
        Map<String, String[]> queryParams = req.getParameterMap();
        Map<String, String> searchFields = new HashMap<String, String>();
        String operator = SearchParamters.OP_OR;
        for (String key : queryParams.keySet()) {
            if (key.trim().equals(SearchParamters.OPERATOR) && !queryParams.get(key)[0].trim().isEmpty()) {
                operator = queryParams.get(key)[0].trim();
            } else if (key.trim().equalsIgnoreCase(SearchFieldNames.TYPE_URI)) {
                searchFields.put("type_id", uriHelper.getTypeId(queryParams.get(key)[0]));
            } else {
                searchFields.put(key.trim(), queryParams.get(key)[0]);
            }
        }
        ConceptEntry[] searchResults = null;

        try {
            searchResults = manager.searchForConcepts(searchFields, operator);
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

        Map<ConceptEntry, ConceptType> entryMap = new HashMap<ConceptEntry, ConceptType>();

        IConceptMessage msg = messageFactory.getMessageFactory(acceptHeader).createConceptMessage();
        createEntryMap(searchResults, entryMap);

        return new ResponseEntity<String>(msg.getAllConceptMessage(entryMap), HttpStatus.OK);

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
