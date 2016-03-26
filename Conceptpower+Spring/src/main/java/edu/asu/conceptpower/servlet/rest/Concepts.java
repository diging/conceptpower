package edu.asu.conceptpower.servlet.rest;

import java.io.IOException;
import java.io.StringReader;
import java.security.Principal;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.conceptpower.servlet.core.ConceptEntry;
import edu.asu.conceptpower.servlet.core.IConceptManager;
import edu.asu.conceptpower.servlet.core.POS;
import edu.asu.conceptpower.servlet.exceptions.DictionaryDoesNotExistException;
import edu.asu.conceptpower.servlet.exceptions.DictionaryModifyException;
import edu.asu.conceptpower.servlet.exceptions.LuceneException;

@Controller
public class Concepts {
	
	@Autowired
	private IConceptManager conceptManager;
	
	private static final Logger logger = LoggerFactory
			.getLogger(Concepts.class);

	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "rest/concept/add", method = RequestMethod.POST)
	public ResponseEntity<String> addConcept(@RequestBody String body, Principal principal) throws IOException, ParseException {
		
		StringReader reader = new StringReader(body);
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
		
		String pos = jsonObject.get("pos").toString();
		if (!POS.posValues.contains(pos)) {
			logger.error("Error creating concept from REST call. " + pos + " does not exist.");
			return new ResponseEntity<String>("POS '" + pos + "' does not exist.", HttpStatus.BAD_REQUEST);
		}

		ConceptEntry conceptEntry = new ConceptEntry();
		conceptEntry.setCreatorId(principal.getName());
		conceptEntry.setSynonymIds(jsonObject.get("synonymids").toString());
		conceptEntry.setWord(jsonObject.get("name").toString());
		conceptEntry.setConceptList(jsonObject.get("conceptlist").toString());
		conceptEntry.setPos(pos);
		conceptEntry.setDescription(jsonObject.get("description").toString());
		conceptEntry.setEqualTo(jsonObject.get("equals").toString());
		conceptEntry.setSimilarTo(jsonObject.get("similar").toString());
		conceptEntry.setTypeId(jsonObject.get("types").toString());
		
		String id = null;
		try {
			if((id = conceptManager.addConceptListEntry(conceptEntry)) == null){
			    return new ResponseEntity<String>(jsonObject.toJSONString(), HttpStatus.CONFLICT);
			}
		} catch (DictionaryDoesNotExistException e) {
			logger.error("Error creating concept from REST call.", e);
			return new ResponseEntity<String>("Specified dictionary does not exist in Conceptpower.", HttpStatus.BAD_REQUEST);
		} catch (DictionaryModifyException e) {
			logger.error("Error creating concept from REST call.", e);
			return new ResponseEntity<String>("Specified dictionary can't be modified.", HttpStatus.BAD_REQUEST);
		} catch(LuceneException le){
		    logger.error("Error creating concept from REST call.", le);
            return new ResponseEntity<String>("Concept Cannot be added", HttpStatus.BAD_REQUEST);
		} catch (IllegalAccessException e) {
		    logger.error("Error creating concept from REST call.", e);
		    return new ResponseEntity<String>("Illegal Access", HttpStatus.BAD_REQUEST);
        }
		
		jsonObject.put("id", id);
		
		return new ResponseEntity<String>(jsonObject.toJSONString(), HttpStatus.OK);
	}
}
