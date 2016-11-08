package edu.asu.conceptpower.servlet.rest;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptType;
import edu.asu.conceptpower.root.TypeDatabaseClient;
import edu.asu.conceptpower.servlet.core.IConceptManager;
import edu.asu.conceptpower.servlet.exceptions.IndexerRunningException;
import edu.asu.conceptpower.servlet.exceptions.LuceneException;
import edu.asu.conceptpower.servlet.rdf.RDFMessageFactory;
import edu.asu.conceptpower.servlet.xml.IConceptMessage;
import edu.asu.conceptpower.servlet.xml.MessageRegistry;

/**
 * This class provides a method to retrieve all concepts for a given word and
 * part of speech. It answers requests of the form:
 * "http://[server.url]/conceptpower/rest/ConceptLookup/{word}/{pos}"
 * 
 * @author Chetan, Julia Damerow
 * 
 */
@Controller
public class ConceptLookup {

    @Autowired
    private IConceptManager dictManager;

    @Autowired
    private TypeDatabaseClient typeManager;

    @Autowired
    private MessageRegistry messageFactory;

    @Autowired
    private RDFMessageFactory rdfFactory;

    private static final Logger logger = LoggerFactory.getLogger(ConceptLookup.class);

    /**
     * This method provides information of a concept for a rest interface of the
     * form "http://[server.url]/conceptpower/rest/ConceptLookup/{word}/{pos}"
     * 
     * @param word
     *            String value of concept to be looked
     * @param pos
     *            String value of the POS of concept to be looked
     * @return XML containing information of given concept for given POS
     */
    @RequestMapping(value = "rest/ConceptLookup/{word}/{pos}", method = RequestMethod.GET, produces = {
            MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
    public @ResponseBody ResponseEntity<String> getWordNetEntry(@PathVariable("word") String word,
            @PathVariable("pos") String pos, @RequestHeader(value = "Accept", defaultValue = MediaType.APPLICATION_XML_VALUE) String acceptHeader) {
        ConceptEntry[] entries = null;
        try {
            entries = dictManager.getConceptListEntriesForWord(word, pos, null);
        } catch (LuceneException ex) {
            logger.error("Lucene exception", ex);
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IllegalAccessException e) {
            logger.error("Illegal access exception", e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IndexerRunningException ie) {
            logger.info("Indexer running exception", ie);
            return new ResponseEntity<String>(ie.getMessage(), HttpStatus.OK);
        }

        Map<ConceptEntry, ConceptType> entryMap = generateEntryMap(entries);
        IConceptMessage conceptMessage = messageFactory.getMessageFactory(acceptHeader).createConceptMessage();

        String xmlEntries = null;
        if (entries != null) {
            xmlEntries = conceptMessage.getAllConceptMessage(entryMap);

        }

        return new ResponseEntity<String>(xmlEntries, HttpStatus.OK);
    }

    private Map<ConceptEntry, ConceptType> generateEntryMap(ConceptEntry[] entries) {
        Map<ConceptEntry, ConceptType> entryMap = new HashMap<ConceptEntry, ConceptType>();

        for (ConceptEntry entry : entries) {
            ConceptType type = null;
            if (typeManager != null && entry.getTypeId() != null && !entry.getTypeId().trim().isEmpty()) {
                type = typeManager.getType(entry.getTypeId());
            }
            entryMap.put(entry, type);
        }
        return entryMap;
    }

    @RequestMapping(value = "rest/ConceptLookup/{word}/{pos}", method = RequestMethod.GET, produces = "application/rdf+xml")
    public @ResponseBody ResponseEntity<String> getWordNetEntryInRdf(@PathVariable("word") String word,
            @PathVariable("pos") String pos) {
        ConceptEntry[] entries = null;
        try {
            entries = dictManager.getConceptListEntriesForWord(word, pos, null);
        } catch (LuceneException ex) {
            logger.error("Lucene Exception", ex);
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IllegalAccessException e) {
            logger.error("Illegal access exception", e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (IndexerRunningException ir) {
            logger.info("Indexer running exception", ir);
            return new ResponseEntity<String>(ir.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }
        return new ResponseEntity<String>(rdfFactory.generateRDF(entries), HttpStatus.OK);
    }

}
