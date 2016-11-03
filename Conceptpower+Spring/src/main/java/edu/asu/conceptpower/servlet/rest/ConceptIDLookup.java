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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptType;
import edu.asu.conceptpower.root.TypeDatabaseClient;
import edu.asu.conceptpower.servlet.core.IConceptManager;
import edu.asu.conceptpower.servlet.json.JsonConceptMessage;
import edu.asu.conceptpower.servlet.xml.MessageFactory;
import edu.asu.conceptpower.servlet.xml.XMLConceptMessage;

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
    private MessageFactory messageFactory;

	/**
	 * This method provides concept information for the rest interface of the
	 * form
	 * "http://[server.url]/conceptpower/rest/Concept?id={URI or ID of concept}"
	 * 
	 * @param req
	 *            Holds the HTTP request information
	 * @return XML containing concept information
	 */
    @RequestMapping(value = "rest/Concept", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE + ","
            + MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<String> getConceptById(HttpServletRequest req, @RequestHeader(value="Accept", defaultValue=MediaType.APPLICATION_XML_VALUE) String acceptHeader) {

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
        ConceptEntry entry = dictManager.getConceptEntry(wordnetId);
        Map<ConceptEntry, ConceptType> entryMap = new HashMap<ConceptEntry, ConceptType>();
        List<String> xmlEntries = new ArrayList<String>();

        if (acceptHeader.equalsIgnoreCase(MediaType.APPLICATION_XML_VALUE)) {
            // XML
            XMLConceptMessage msg = messageFactory.getXMLMessageFactory().createXMLConceptMessage();
            if (entry != null) {

                ConceptType type = null;
                if (typeManager != null && entry.getTypeId() != null && !entry.getTypeId().trim().isEmpty()) {
                    type = typeManager.getType(entry.getTypeId());
                }
                entryMap.put(entry, type);
                xmlEntries = msg.appendEntries(entryMap);
            }

            return new ResponseEntity<String>(msg.getXML(xmlEntries), HttpStatus.OK);
        } else {
            // JSON
            JsonConceptMessage msg = messageFactory.getJsonMessageFactory().createJsonConceptMessage();

            if (entry != null) {

                ConceptType type = null;
                if (typeManager != null && entry.getTypeId() != null && !entry.getTypeId().trim().isEmpty()) {
                    type = typeManager.getType(entry.getTypeId());
                }
                entryMap.put(entry, type);
                return new ResponseEntity<String>(msg.getJsonArray(entryMap), HttpStatus.OK);
            }

            return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
        }

    }
}
