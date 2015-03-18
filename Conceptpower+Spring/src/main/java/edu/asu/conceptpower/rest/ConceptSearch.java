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
 * This class provides a method to search concepts.
 * It answers requests to:
 * "http://[server.url]/conceptpower/rest/ConceptSearch?{list of fieldname=value}{operator=and/or}"
 * 
 * @author Chetan, Julia Damerow
 * 
 */
@Controller
public class ConceptSearch {

	@Autowired
	private ConceptManager dictManager;

	@Autowired
	private TypeDatabaseClient typeManager;

	@Autowired
	private XMLMessageFactory messageFactory;

	/**
	 * This method provides information of a concept for a rest interface of the
	 * form
	 * "http://[server.url]/conceptpower/rest/Type?id={URI or ID of concept}"
	 * 
	 * @param req
	 *            Holds HTTP request information
	 * @return
	 */
	@RequestMapping(value = "rest/ConceptSearch", method = RequestMethod.GET, produces = "application/xml")
	public @ResponseBody ResponseEntity<String> searchConcept(HttpServletRequest req) {
		Map<String, String[]> queryParams = req.getParameterMap();
		Map<String, String> searchFields = new HashMap<String, String>();

		String operator = SearchParamters.OP_OR;
		for (String key : queryParams.keySet()) {
			if (key.trim().equals(SearchParamters.OPERATOR)
					&& !queryParams.get(key)[0].trim().isEmpty())
				operator = queryParams.get(key)[0].trim();
			else {
				searchFields.put(key.trim(), queryParams.get(key)[0]);
			}
		}
		ConceptEntry[] searchResults = null;

		if (operator.equals(SearchParamters.OP_AND))
			searchResults = dictManager
					.searchForConceptsConnectedByAnd(searchFields);
		else {
			searchResults = dictManager
					.searchForConceptsConnectedByOr(searchFields);
		}

		List<String> xmlEntries = new ArrayList<String>();
		Map<ConceptEntry, ConceptType> entryMap = new HashMap<ConceptEntry, ConceptType>();

		XMLConceptMessage msg = messageFactory.createXMLConceptMessage();
		for (ConceptEntry entry : searchResults) {
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
