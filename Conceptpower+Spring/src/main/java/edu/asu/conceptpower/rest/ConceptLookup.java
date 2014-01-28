package edu.asu.conceptpower.rest;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptManager;
import edu.asu.conceptpower.core.ConceptType;
import edu.asu.conceptpower.db4o.TypeDatabaseClient;
import edu.asu.conceptpower.xml.XMLConceptMessage;

@Controller
public class ConceptLookup {

	@Autowired
	private ConceptManager dictManager;

	@Autowired
	private TypeDatabaseClient typeManager;

	@RequestMapping(value = "rest/ConceptLookup/{word}/{pos}", method = RequestMethod.GET)
	public String getWordNetEntry(@PathVariable("word") String word,
			@PathVariable("pos") String pos, HttpServletRequest req,
			ModelMap model) {

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

		XMLConceptMessage returnMsg = new XMLConceptMessage();
		if (entries != null) {
			returnMsg.appendEntries(entryMap);
		}

		return returnMsg.getXML();
	}

}
