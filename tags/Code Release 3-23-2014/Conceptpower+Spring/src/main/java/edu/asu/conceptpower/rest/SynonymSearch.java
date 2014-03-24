package edu.asu.conceptpower.rest;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptManager;
import edu.asu.conceptpower.core.ConceptType;
import edu.asu.conceptpower.db4o.TypeDatabaseClient;
import edu.asu.conceptpower.xml.XMLConceptMessage;

@Controller
public class SynonymSearch {

	@Autowired
	private ConceptManager dictManager;

	@Autowired
	private TypeDatabaseClient typeManager;

	@Autowired
	XMLConceptMessage msg;

	@RequestMapping(value = "rest/SynonymSearch", method = RequestMethod.GET, produces = "application/xml")
	public @ResponseBody
	String getSynonymsForId(HttpServletRequest req, ModelMap model) {

		// construct the URL to the Wordnet dictionary directory
		String id = req.getParameter("id");
		String[] pathParts = id.split("/");
		int lastIndex = pathParts.length - 1;
		String wordnetId = null;
		if (lastIndex > -1)
			wordnetId = pathParts[lastIndex];

		if (wordnetId == null) {
			return "no word net id";
		}

		// context.log("Finding entry for " + wordnetId);
		ConceptEntry[] synonyms = dictManager.getSynonymsForConcept(wordnetId);

		for (ConceptEntry entry : synonyms) {
			ConceptType type = null;
			if (typeManager != null && entry.getTypeId() != null
					&& !entry.getTypeId().trim().isEmpty()) {
				type = typeManager.getType(entry.getTypeId());
			}
			msg.appendEntry(entry, type);
		}

		return msg.getXML();
	}
}
