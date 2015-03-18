package edu.asu.conceptpower.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptManager;
import edu.asu.conceptpower.core.ConceptType;
import edu.asu.conceptpower.db4o.TypeDatabaseClient;
import edu.asu.conceptpower.xml.XMLConceptMessage;
import edu.asu.conceptpower.xml.XMLMessageFactory;

/**
 * This class provides a method to retrieve all synonyms for a given concept
 * identified by its id.
 * It answers requests to:
 * "http://[server.url]/conceptpower/rest/SynonymSearch?id={URI or ID of concept you want synonyms for}"
 * 
 * @author Julia Damero, Chetan
 * 
 */
@Controller
public class SynonymSearch {

	@Autowired
	private ConceptManager dictManager;

	@Autowired
	private TypeDatabaseClient typeManager;

	@Autowired
	private XMLMessageFactory messageFactory;

	/**
	 * This method provides information of synonyms of a word for a rest
	 * interface of the form
	 * "http://[server.url]/conceptpower/rest/SynonymSearch?id={URI or ID of concept you want synonyms for}"
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "rest/SynonymSearch", method = RequestMethod.GET, produces = "application/xml")
	public @ResponseBody ResponseEntity<String> getSynonymsForId(HttpServletRequest req) {

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

		
		ConceptEntry[] synonyms = dictManager.getSynonymsForConcept(wordnetId);
		List<String> xmlEntries = new ArrayList<String>();
		Map<ConceptEntry, ConceptType> entryMap = new HashMap<ConceptEntry, ConceptType>();

		XMLConceptMessage msg = messageFactory.createXMLConceptMessage();
		for (ConceptEntry entry : synonyms) {
			ConceptType type = null;
			if (typeManager != null && entry.getTypeId() != null
					&& !entry.getTypeId().trim().isEmpty()) {
				type = typeManager.getType(entry.getTypeId());
			}
			entryMap.put(entry, type);
			xmlEntries = msg.appendEntries(entryMap);
		}

		return new ResponseEntity<String>(msg.getXML(xmlEntries), HttpStatus.OK);
	}
}
