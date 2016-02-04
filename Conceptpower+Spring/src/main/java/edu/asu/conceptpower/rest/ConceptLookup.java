package edu.asu.conceptpower.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptType;
import edu.asu.conceptpower.core.IConceptManager;
import edu.asu.conceptpower.db4o.TypeDatabaseClient;
import edu.asu.conceptpower.xml.XMLConceptMessage;
import edu.asu.conceptpower.xml.XMLMessageFactory;

/**
 * This class provides a method to retrieve all concepts
 * for a given word and part of speech. It answers requests of the form:
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
	private XMLMessageFactory messageFactory;

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
	@RequestMapping(value = "rest/ConceptLookup/{word}/{pos}", method = RequestMethod.GET, produces = "application/xml")
	public @ResponseBody ResponseEntity<String> getWordNetEntry(@PathVariable("word") String word,
			@PathVariable("pos") String pos) {

		ConceptEntry[] entries = dictManager.getConceptListEntriesForWord(word,
				pos);
		Map<ConceptEntry, ConceptType> entryMap = new HashMap<ConceptEntry, ConceptType>();

		for (ConceptEntry entry : entries) {
			ConceptType type = null;
			if (typeManager != null && entry.getTypeId() != null
					&& !entry.getTypeId().trim().isEmpty()) {
				type = typeManager.getType(entry.getTypeId());
			}
			entryMap.put(entry, type);
		}

		XMLConceptMessage returnMsg = messageFactory.createXMLConceptMessage();
		List<String> xmlEntries = new ArrayList<String>();
		if (entries != null) {
			xmlEntries = returnMsg.appendEntries(entryMap);
		}

		return new ResponseEntity<String>(returnMsg.getXML(xmlEntries), HttpStatus.OK);
	}
}
