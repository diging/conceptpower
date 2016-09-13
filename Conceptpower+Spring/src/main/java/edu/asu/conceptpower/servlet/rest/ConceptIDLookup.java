package edu.asu.conceptpower.servlet.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptType;
import edu.asu.conceptpower.root.TypeDatabaseClient;
import edu.asu.conceptpower.servlet.core.IConceptManager;
import edu.asu.conceptpower.servlet.exceptions.LuceneException;
import edu.asu.conceptpower.servlet.xml.XMLConceptMessage;
import edu.asu.conceptpower.servlet.xml.XMLMessageFactory;

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
	private IConceptManager dictManager;

	@Autowired
	private TypeDatabaseClient typeManager;
	
	@Autowired
	private XMLMessageFactory messageFactory;

    @Autowired
    private IConceptManager conceptManager;
	/**
	 * This method provides concept information for the rest interface of the
	 * form
	 * "http://[server.url]/conceptpower/rest/Concept?id={URI or ID of concept}"
	 * 
	 * @param req
	 *            Holds the HTTP request information
	 * @return XML containing concept information
	 */
	@RequestMapping(value = "rest/Concept", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE + "; charset=utf-8")
    public @ResponseBody ResponseEntity<String> getConceptById(HttpServletRequest req) {

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
        ConceptEntry entry = null;
        try {
            entry = dictManager.getConceptEntry(wordnetId);
        } catch (LuceneException ex) {
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
        Map<ConceptEntry, ConceptType> entryMap = new HashMap<ConceptEntry, ConceptType>();
        List<String> xmlEntries = new ArrayList<String>();

        XMLConceptMessage msg = messageFactory.createXMLConceptMessage();

        if (entry != null) {

            try {
                addAlternativeIds(pathParts[lastIndex], entry);
            } catch (LuceneException e) {
                return new ResponseEntity<String>(msg.getXML(xmlEntries), HttpStatus.BAD_REQUEST);
            }

            ConceptType type = null;

            if (typeManager != null && entry.getTypeId() != null && !entry.getTypeId().trim().isEmpty()) {
                type = typeManager.getType(entry.getTypeId());
            }
            entryMap.put(entry, type);
            xmlEntries = msg.appendEntries(entryMap);
        }
        
        return new ResponseEntity<String>(msg.getXML(xmlEntries), HttpStatus.OK);
    }

    private void addAlternativeIds(String id, ConceptEntry entry) throws LuceneException {

        // User have queried with a general wordnet id
        if (!entry.getId().equalsIgnoreCase(id)) {
            // general wordnet id
            entry.getAlternativeIds().add(id);
            
            ConceptEntry conceptEntry = conceptManager.getConceptEntry(entry.getWordnetId());
            // CP id
            entry.getAlternativeIds().add(conceptEntry.getId());
            // specific wordnet ID
            entry.getAlternativeIds().add(conceptEntry.getWordnetId());
        } else {
            // User queried with specific wordnet id or CP id. Add it
            // directly from the entry parameter
            entry.getAlternativeIds().add(entry.getId());
            entry.getAlternativeIds().add(entry.getWordnetId());
        }
    }
}
