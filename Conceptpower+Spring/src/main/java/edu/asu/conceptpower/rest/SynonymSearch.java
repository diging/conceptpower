package edu.asu.conceptpower.rest;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;

import edu.asu.conceptpower.app.db.TypeDatabaseClient;
import edu.asu.conceptpower.app.exceptions.LuceneException;
import edu.asu.conceptpower.app.manager.IConceptManager;
import edu.asu.conceptpower.app.model.ConceptEntry;
import edu.asu.conceptpower.app.model.ConceptType;
import edu.asu.conceptpower.rest.msg.IConceptMessage;
import edu.asu.conceptpower.rest.msg.IMessageRegistry;
import jakarta.servlet.http.HttpServletRequest;

/**
 * This class provides a method to retrieve all synonyms for a given concept
 * identified by its id. It answers requests to:
 * "http://[server.url]/conceptpower/rest/SynonymSearch?id={URI or ID of concept
 * you want synonyms for}"
 * 
 * @author Julia Damero, Chetan
 * 
 */
@Controller
public class SynonymSearch {

    @Autowired
    private IConceptManager dictManager;

    @Autowired
    private TypeDatabaseClient typeManager;

    @Autowired
    private IMessageRegistry messageFactory;

    private static final Logger logger = LoggerFactory.getLogger(SynonymSearch.class);

    /**
     * This method provides information of synonyms of a word for a rest
     * interface of the form
     * "http://[server.url]/conceptpower/rest/SynonymSearch?id={URI or ID of
     * concept you want synonyms for}"
     * 
     * @param req
     * @return
     * @throws JsonProcessingException
     */
    @RequestMapping(value = "/SynonymSearch", method = RequestMethod.GET, produces = { MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_JSON_VALUE })
    public @ResponseBody ResponseEntity<String> getSynonymsForId(HttpServletRequest req,
            @RequestHeader(value = "Accept", defaultValue = MediaType.APPLICATION_XML_VALUE) String acceptHeader)
            throws JsonProcessingException {

        // construct the URL to the Wordnet dictionary directory
        String id = req.getParameter("id");
        if (id == null || id.trim().isEmpty()) {
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }

        String[] pathParts = id.split("/");
        int lastIndex = pathParts.length - 1;
        String wordnetId = null;
        if (lastIndex > -1)
            wordnetId = pathParts[lastIndex];

        if (wordnetId == null) {
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }

        ConceptEntry[] synonyms = null;
        try {
            synonyms = dictManager.getSynonymsForConcept(wordnetId);
        } catch (LuceneException ex) {
            logger.error("Lucene exception", ex);
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        String entries = null;
        Map<ConceptEntry, ConceptType> entryMap = new HashMap<ConceptEntry, ConceptType>();

        IConceptMessage msg = messageFactory.getMessageFactory(acceptHeader).createConceptMessage();
        for (ConceptEntry entry : synonyms) {
            ConceptType type = null;
            if (typeManager != null && entry.getTypeId() != null && !entry.getTypeId().trim().isEmpty()) {
                type = typeManager.getType(entry.getTypeId());
            }
            entryMap.put(entry, type);
            entries = msg.getAllConceptEntriesAndPaginationDetails(entryMap, null);
        }

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "text/html; charset=utf-8");

        return new ResponseEntity<String>(entries, responseHeaders, HttpStatus.OK);
    }
}
