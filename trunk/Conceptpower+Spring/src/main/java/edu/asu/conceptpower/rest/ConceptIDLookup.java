package edu.asu.conceptpower.rest;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptManager;
import edu.asu.conceptpower.core.ConceptType;
import edu.asu.conceptpower.db4o.TypeDatabaseClient;
import edu.asu.conceptpower.xml.XMLConceptMessage;

@Controller
public class ConceptIDLookup {

	@Autowired
	private ConceptManager dictManager;

	@Autowired
	private TypeDatabaseClient typeManager;

	@Autowired
	private XMLConceptMessage msg;

	@RequestMapping(value = "rest/Concept", method = RequestMethod.GET)
	public String getConceptById(@RequestParam("id") String id,
			HttpServletRequest req, ModelMap model) {

		String[] pathParts = id.split("/");
		int lastIndex = pathParts.length - 1;
		String wordnetId = null;
		if (lastIndex > -1)
			wordnetId = pathParts[lastIndex];

		if (wordnetId == null) {
			return null;
		}

		ConceptEntry entry = dictManager.getConceptEntry(wordnetId);

		if (entry != null) {

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
