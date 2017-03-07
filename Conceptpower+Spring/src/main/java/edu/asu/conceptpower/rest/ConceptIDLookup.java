package edu.asu.conceptpower.rest;

import java.util.HashMap;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;

import edu.asu.conceptpower.app.core.ConceptTypesService;
import edu.asu.conceptpower.app.core.IConceptManager;
import edu.asu.conceptpower.app.db.TypeDatabaseClient;
import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptType;
import edu.asu.conceptpower.rest.msg.IConceptMessage;
import edu.asu.conceptpower.rest.msg.IMessageRegistry;

/**
 * This class provides a method to query concepts by their id.
 * It answers requests of the form:
 * "http://[server.url]/conceptpower/rest/Concept?id={URI or ID of concept}"
 * 
 * @author Chetan, Julia Damerow
 * 
 */
@Controller
public class ConceptIDLookup {

    @Autowired
    private IConceptManager conceptManager;

    @Autowired
    private TypeDatabaseClient typeManager;

    @Autowired
    private IMessageRegistry messageFactory;

    @Autowired
    private ConceptTypesService conceptTypesService;

    private static final Logger logger = LoggerFactory.getLogger(ConceptIDLookup.class);

    /**
     * This method provides concept information for the rest interface of the
     * form
     * "http://[server.url]/conceptpower/rest/Concept?id={URI or ID of concept}"
     * 
     * @param req
     *            Holds the HTTP request information
     * @return XML containing concept information
     * @throws JsonProcessingException
     */
    @RequestMapping(value = "/Concept", method = RequestMethod.GET, produces = { MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_JSON_VALUE })
    public @ResponseBody ResponseEntity<String> getConceptById(@RequestParam String id,
            @RequestHeader(value = "Accept", defaultValue = MediaType.APPLICATION_XML_VALUE) String acceptHeader)
                    throws JsonProcessingException {

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
        ConceptEntry entry = conceptManager.getConceptEntry(wordnetId);
        Map<ConceptEntry, ConceptType> entryMap = new HashMap<ConceptEntry, ConceptType>();

        IConceptMessage msg = messageFactory.getMessageFactory(acceptHeader).createConceptMessage();

        if (entry != null) {

            ConceptType type = null;
            if (typeManager != null && entry.getTypeId() != null && !entry.getTypeId().trim().isEmpty()) {
                type = typeManager.getType(entry.getTypeId());
            }
            entryMap.put(entry, type);
        }

        return new ResponseEntity<String>(msg.getAllConceptEntriesAndPaginationDetails(entryMap, null), HttpStatus.OK);

    }

}
