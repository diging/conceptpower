package edu.asu.conceptpower.servlet.rest;

import java.io.IOException;
import java.io.StringReader;
import java.security.Principal;
import java.util.ListIterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptType;
import edu.asu.conceptpower.servlet.core.IConceptManager;
import edu.asu.conceptpower.servlet.core.IConceptTypeManger;
import edu.asu.conceptpower.servlet.core.POS;
import edu.asu.conceptpower.servlet.exceptions.DictionaryDoesNotExistException;
import edu.asu.conceptpower.servlet.exceptions.DictionaryModifyException;
import edu.asu.conceptpower.servlet.exceptions.IndexerRunningException;
import edu.asu.conceptpower.servlet.exceptions.LuceneException;

@Controller
public class Concepts {

    @Autowired
    private IConceptManager conceptManager;

    @Autowired
    private IConceptTypeManger typeManager;

    private static final Logger logger = LoggerFactory.getLogger(Concepts.class);

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "rest/concept/add", method = RequestMethod.POST)
    public ResponseEntity<String> addConcept(@RequestBody String body, Principal principal) {

        StringReader reader = new StringReader(body);
        JSONParser jsonParser = new JSONParser();

        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject) jsonParser.parse(reader);
        } catch (IOException | ParseException e1) {
            logger.error("Error parsing request.", e1);
            return new ResponseEntity<String>("Error parsing request: " + e1,
                    HttpStatus.BAD_REQUEST);
        } catch (ClassCastException ex) {
            logger.error("Couldn't cast object.", ex);
            return new ResponseEntity<String>(
                    "It looks like you are not submitting a JSON Object.",
                    HttpStatus.BAD_REQUEST);
        }

        JsonValidationResult result = checkJsonObject(jsonObject);
        if (!result.isValid())
            return new ResponseEntity<String>(result.getMessage(), HttpStatus.BAD_REQUEST);

        ConceptEntry conceptEntry = createEntry(jsonObject, principal.getName());

        // check type
        String typeId = conceptEntry.getTypeId();
        ConceptType type = typeManager.getType(typeId);
        if (type == null) {
            return new ResponseEntity<String>("The type id you are submitting doesn't " + "match any existing type.",
                    HttpStatus.BAD_REQUEST);
        }

        String id = null;
        try {
            id = conceptManager.addConceptListEntry(conceptEntry);
        } catch (DictionaryDoesNotExistException e) {
            logger.error("Error creating concept from REST call.", e);
            return new ResponseEntity<String>("Specified concept list does not exist in Conceptpower.",
                    HttpStatus.BAD_REQUEST);
        } catch (DictionaryModifyException e) {
            logger.error("Error creating concept from REST call.", e);
            return new ResponseEntity<String>("Specified concept list can't be modified.", HttpStatus.BAD_REQUEST);
        } catch (LuceneException le) {
            logger.error("Error creating concept from REST call.", le);
            return new ResponseEntity<String>("Concept Cannot be added", HttpStatus.BAD_REQUEST);
        } catch (IllegalAccessException e) {
            logger.error("Error creating concept from REST call.", e);
            return new ResponseEntity<String>("Illegal Access", HttpStatus.BAD_REQUEST);
        } catch (IndexerRunningException ir) {
            return new ResponseEntity<String>(jsonObject.toJSONString(), HttpStatus.CONFLICT);
        }

        jsonObject.put("id", id);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", MediaType.APPLICATION_JSON_VALUE + "; charset=utf-8");
        
        return new ResponseEntity<String>(jsonObject.toJSONString(), responseHeaders,
                HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "rest/concepts/add", method = RequestMethod.POST)
    public ResponseEntity<String> addConcepts(@RequestBody String body, Principal principal) {
        StringReader reader = new StringReader(body);
        JSONParser jsonParser = new JSONParser();

        JSONArray jsonArray = null;
        try {
            jsonArray = (JSONArray) jsonParser.parse(reader);
        } catch (IOException | ParseException | ClassCastException e1) {
            logger.error("Error parsing request.", e1);
            return new ResponseEntity<String>("Error parsing request: " + e1, HttpStatus.BAD_REQUEST);
        }

        ListIterator<JSONObject> listIt = jsonArray.listIterator();
        JSONArray responseArray = new JSONArray();
        while (listIt.hasNext()) {
            JSONObject jsonObject = listIt.next();

            JsonValidationResult result = checkJsonObject(jsonObject);

            JSONObject responseObj = new JSONObject();
            responseObj.put(JsonFields.WORD, jsonObject.get(JsonFields.WORD));
            responseObj.put("validation", result.getMessage() != null ? result.getMessage() : "OK");

            responseArray.add(responseObj);

            if (!result.isValid()) {
                responseObj.put("success", false);
                continue;
            }

            ConceptEntry conceptEntry = createEntry(jsonObject, principal.getName());

            String id = null;
            try {
                id = conceptManager.addConceptListEntry(conceptEntry);
                responseObj.put(JsonFields.ID, id);
                responseObj.put("success", true);
            } catch (DictionaryDoesNotExistException e) {
                logger.error("Error creating concept from REST call.", e);
                responseObj.put("success", false);
                responseObj.put("error_message", "Specified dictionary does not exist in Conceptpower.");
            } catch (DictionaryModifyException e) {
                logger.error("Error creating concept from REST call.", e);
                responseObj.put("success", false);
                responseObj.put("error_message", "Specified dictionary can't be modified.");
            } catch (LuceneException le) {
                logger.error("Error creating concept from REST call.", le);
                return new ResponseEntity<String>("Concept Cannot be added", HttpStatus.BAD_REQUEST);
            } catch (IllegalAccessException e) {
                logger.error("Error creating concept from REST call.", e);
                return new ResponseEntity<String>("Illegal Access", HttpStatus.BAD_REQUEST);
            } catch (IndexerRunningException ir) {
                return new ResponseEntity<String>(jsonObject.toJSONString(), HttpStatus.CONFLICT);
            }

        }

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", MediaType.APPLICATION_JSON_VALUE + "; charset=utf-8");
        
        return new ResponseEntity<String>(responseArray.toJSONString(), responseHeaders,
                HttpStatus.OK);
    }

    private JsonValidationResult checkJsonObject(JSONObject jsonObject) {
        if (jsonObject.get(JsonFields.POS) == null) {
            return new JsonValidationResult("Error parsing request: please provide a POS ('pos' attribute).",
                    jsonObject, false);
        }

        if (jsonObject.get(JsonFields.WORD) == null) {
            return new JsonValidationResult(
                    "Error parsing request: please provide a word for the concept ('word' attribute).", jsonObject,
                    false);
        }

        if (jsonObject.get(JsonFields.DESCRIPTION) == null) {
            return new JsonValidationResult(
                    "Error parsing request: please provide a description for the concept ('description' attribute).",
                    jsonObject, false);
        }

        if (jsonObject.get(JsonFields.TYPE) == null) {
            return new JsonValidationResult(
                    "Error parsing request: please provide a type for the concept ('type' attribute).", jsonObject,
                    false);
        }

        if (jsonObject.get(JsonFields.CONCEPT_LIST) == null) {
            return new JsonValidationResult(
                    "Error parsing request: please provide a concept list for the concept ('conceptlist' attribute).",
                    jsonObject, false);
        }

        String pos = jsonObject.get(JsonFields.POS).toString();
        if (!POS.posValues.contains(pos)) {
            logger.error("Error creating concept from REST call. " + pos + " does not exist.");
            return new JsonValidationResult("POS '" + pos + "' does not exist.", jsonObject, false);
        }

        return new JsonValidationResult(null, jsonObject, true);
    }

    private ConceptEntry createEntry(JSONObject jsonObject, String username) {
        ConceptEntry conceptEntry = new ConceptEntry();
        conceptEntry.setCreatorId(username);
        conceptEntry.setSynonymIds(jsonObject.get(JsonFields.SYNONYM_IDS) != null ? jsonObject.get(
                JsonFields.SYNONYM_IDS).toString() : "");
        conceptEntry.setWord(jsonObject.get(JsonFields.WORD).toString());
        conceptEntry.setConceptList(jsonObject.get(JsonFields.CONCEPT_LIST).toString());
        conceptEntry.setPos(jsonObject.get(JsonFields.POS).toString());
        conceptEntry.setDescription(jsonObject.get(JsonFields.DESCRIPTION).toString());
        conceptEntry.setEqualTo(jsonObject.get(JsonFields.EQUALS) != null ? jsonObject.get(JsonFields.EQUALS)
                .toString() : "");
        conceptEntry.setSimilarTo(jsonObject.get(JsonFields.SIMILAR) != null ? jsonObject.get(JsonFields.SIMILAR)
                .toString() : "");
        conceptEntry.setTypeId(jsonObject.get(JsonFields.TYPE).toString());
        return conceptEntry;
    }

    class JsonValidationResult {
        private String message;
        private JSONObject jsonObject;
        private boolean valid;

        public JsonValidationResult(String message, JSONObject object, boolean valid) {
            this.message = message;
            this.valid = valid;
            this.jsonObject = object;
        }

        public JSONObject getJsonObject() {
            return jsonObject;
        }

        public void setJsonObject(JSONObject jsonObject) {
            this.jsonObject = jsonObject;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public boolean isValid() {
            return valid;
        }

        public void setValid(boolean valid) {
            this.valid = valid;
        }

    }
}
