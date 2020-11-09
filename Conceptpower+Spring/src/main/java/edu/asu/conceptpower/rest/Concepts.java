package edu.asu.conceptpower.rest;

import java.io.IOException;
import java.io.StringReader;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.text.StringEscapeUtils;
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
import org.springframework.web.bind.annotation.ResponseBody;

import edu.asu.conceptpower.app.core.Constants;
import edu.asu.conceptpower.app.core.POS;
import edu.asu.conceptpower.app.exceptions.DictionaryDoesNotExistException;
import edu.asu.conceptpower.app.exceptions.DictionaryModifyException;
import edu.asu.conceptpower.app.exceptions.IndexerRunningException;
import edu.asu.conceptpower.app.exceptions.LuceneException;
import edu.asu.conceptpower.app.manager.IConceptManager;
import edu.asu.conceptpower.app.manager.IConceptTypeManager;
import edu.asu.conceptpower.app.model.ConceptEntry;
import edu.asu.conceptpower.app.model.ConceptType;
import edu.asu.conceptpower.app.util.URIHelper;
import edu.mit.jwi.item.WordID;

@Controller
public class Concepts {

    @Autowired
    private IConceptManager conceptManager;

    @Autowired
    private IConceptTypeManager typeManager;

    @Autowired
    private URIHelper uriHelper;

    private static final Logger logger = LoggerFactory.getLogger(Concepts.class);

    /**
     * This method creates concepts as well as concept wrappers. This is decided
     * based on the request parameter. If the request parameter contains one or
     * more wordnet ids then concept wrapper is created else a new concept is
     * created.
     * 
     * Sample input for creating a concept wrapper:
     * 
     * { 
     *      "wordnetIds":
     *      "WID-00450866-N-01-pony-trekking,WID-03981924-N-01-pony_cart",
     *      "synonymids" : "WID-02380464-N-01-polo_pony,WID-04206225-N-03-pony",
     *      "conceptlist" : "FirstList-1", 
     *      "type" : "88bea1dc-1443-4296-8315-715c71128b01", 
     *      "description" : "Description",
     *      "equal_to" : "http://viaf.org/viaf/38882290", 
     *      "similarTo" : "http://viaf.org/viaf/43723621" 
     * }
     * 
     * Please note any word or pos passed during concept wrapper creation will
     * be ignored. Word and pos details are fetched from wordnet. In case POS is
     * entered by user, the code validates with the wordnet POS and throws an
     * error if POS is not matching with wordnet
     * 
     * Sample input for creating a concept:
     * 
     * { 
     *      "word": "kitty", 
     *      "pos": "noun", 
     *      "conceptlist": "mylist", 
     *      "description": "Soft kitty, sleepy kitty, little ball of fur.", 
     *      "type": "3b755111-545a-4c1c-929c-a2c0d4c3913b" 
     * }
     * 
     * @param body
     * @param principal
     * @return
     * @throws IndexerRunningException
     * @throws LuceneException
     * @throws IllegalAccessException
     * @throws POSMismatchException
     */
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/concept/add", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<String> addConcept(@RequestBody String body, Principal principal)
            throws IllegalAccessException, LuceneException, IndexerRunningException {

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

        if (result.isValid() && jsonObject.get(JsonFields.WORDNET_ID) != null) {
            result = checkJsonObjectForWrapper(jsonObject);
        }

        if (!result.isValid()) {
            return new ResponseEntity<String>(result.getMessage(), HttpStatus.BAD_REQUEST);
        }

        ConceptEntry conceptEntry = createEntry(jsonObject, principal.getName());

        if (jsonObject.get(JsonFields.WORDNET_ID) != null) {
            result = validatePOS(jsonObject, conceptEntry);
        }

        if (!result.isValid()) {
            return new ResponseEntity<String>(result.getMessage(), HttpStatus.BAD_REQUEST);
        }

        // check type
        String typeId = conceptEntry.getTypeId();
        ConceptType type = typeManager.getType(typeId);
        if (type == null) {
            return new ResponseEntity<String>("The type id you are submitting doesn't " + "match any existing type.",
                    HttpStatus.BAD_REQUEST);
        }

        try {
            conceptEntry.setId(conceptManager.addConceptListEntry(conceptEntry, principal.getName()).getId());
        } catch (DictionaryDoesNotExistException e) {
            logger.error("Error creating concept from REST call.", e);
            return new ResponseEntity<String>("Specified concept list does not exist in Conceptpower.",
                    HttpStatus.BAD_REQUEST);
        } catch (DictionaryModifyException e) {
            logger.error("Error creating concept from REST call.", e);
            return new ResponseEntity<String>("Specified concept list can't be modified.", HttpStatus.BAD_REQUEST);
        } catch (LuceneException le) {
            logger.error("Error creating concept from REST call.", le);
            return new ResponseEntity<String>("Concept Cannot be added", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IllegalAccessException e) {
            logger.error("Error creating concept from REST call.", e);
            return new ResponseEntity<String>("Illegal Access", HttpStatus.BAD_REQUEST);
        }

        jsonObject.put(JsonFields.ID, conceptEntry.getId());
        jsonObject.put(JsonFields.URI, uriHelper.getURI(conceptEntry));

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", MediaType.APPLICATION_JSON_VALUE + "; charset=utf-8");
        
        return new ResponseEntity<String>(StringEscapeUtils.unescapeJson(jsonObject.toJSONString()), responseHeaders,
                HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/concepts/add", method = RequestMethod.POST)
    public ResponseEntity<String> addConcepts(@RequestBody String body, Principal principal)
            throws IllegalAccessException, LuceneException, IndexerRunningException {
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
            
            if (result.isValid() && jsonObject.get(JsonFields.WORDNET_ID) != null) {
                result = checkJsonObjectForWrapper(jsonObject);
            }

            if (!result.isValid()) {
                return new ResponseEntity<String>(result.getMessage(), HttpStatus.BAD_REQUEST);
            }

            ConceptEntry conceptEntry = createEntry(jsonObject, principal.getName());

            if (jsonObject.get(JsonFields.WORDNET_ID) != null) {
                validatePOS(jsonObject, conceptEntry);
            }
            JSONObject responseObj = new JSONObject();
            responseObj.put(JsonFields.WORD, jsonObject.get(JsonFields.WORD));
            responseObj.put("validation", result.getMessage() != null ? result.getMessage() : "OK");

            responseArray.add(responseObj);

            if (!result.isValid()) {
                responseObj.put("success", false);
                continue;
            }

            try {
                conceptEntry.setId(conceptManager.addConceptListEntry(conceptEntry, principal.getName()).getId());
                responseObj.put(JsonFields.ID, conceptEntry.getId());
                responseObj.put(JsonFields.URI, uriHelper.getURI(conceptEntry));
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
                return new ResponseEntity<String>("Concept Cannot be added", HttpStatus.INTERNAL_SERVER_ERROR);
            } catch (IllegalAccessException e) {
                logger.error("Error creating concept from REST call.", e);
                return new ResponseEntity<String>("Illegal Access", HttpStatus.BAD_REQUEST);
            }
        }

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", MediaType.APPLICATION_JSON_VALUE + "; charset=utf-8");
        
        return new ResponseEntity<String>(StringEscapeUtils.unescapeJson(responseArray.toJSONString()), responseHeaders,
                HttpStatus.OK);
    }

    private JsonValidationResult checkJsonObject(JSONObject jsonObject)
            throws IllegalAccessException, LuceneException, IndexerRunningException {
        if (jsonObject.get(JsonFields.POS) == null) {
            return new JsonValidationResult("Error parsing request: please provide a POS ('pos' attribute).",
                    jsonObject, false);
        }

        String pos = jsonObject.get(JsonFields.POS).toString();
        if (!POS.posValues.contains(pos)) {
            logger.debug("Error creating concept from REST call. " + pos + " does not exist.");
            return new JsonValidationResult("Error parsing request: please provide a valid POS ('pos' attribute).",
                    jsonObject, false);
        }

        if (jsonObject.get(JsonFields.WORD) == null) {
            return new JsonValidationResult(
                    "Error parsing request: please provide a word for the concept ('word' attribute).", jsonObject,
                    false);
        }

        if (jsonObject.get(JsonFields.CONCEPT_LIST) == null) {
            return new JsonValidationResult(
                    "Error parsing request: please provide a list name for the concept ('list' attribute).", jsonObject,
                    false);
        }

        if (jsonObject.get(JsonFields.TYPE) == null) {
            return new JsonValidationResult(
                    "Error parsing request: please provide a type for the concept ('type' attribute).", jsonObject,
                    false);
        }

        return new JsonValidationResult(null, jsonObject, true);
    }

    private JsonValidationResult checkJsonObjectForWrapper(JSONObject jsonObject)
            throws IllegalAccessException, LuceneException, IndexerRunningException {

        // Validation to check if wordnet ids are seperated by comma
        if (jsonObject.get(JsonFields.WORDNET_ID) != null) {
            String wordnetIds = jsonObject.get(JsonFields.WORDNET_ID).toString();
            List<String> wordnetIdsList = Arrays
                    .asList(wordnetIds.split("\\s*" + Constants.CONCEPT_SEPARATOR + "\\s*"));
            for (String wordNetId : wordnetIdsList) {
                try{ 
                    WordID.parseWordID(wordNetId);
                } catch (IllegalArgumentException ex) {
                    return new JsonValidationResult(
                            "Error parsing request: please provide a valid list of wordnet ids seperated by commas. Wordnet id "
                                    + wordNetId + " doesn't exist.",
                            jsonObject, false);
                }
            }

            // Check if there is an existing wrapper concept if so throw error
            for (String wordNetId : wordnetIdsList) {
                if (conceptManager.getConceptWrappedEntryByWordNetId(wordNetId) != null) {
                    // This wordnet id is already wrapped. Throw error
                    return new JsonValidationResult(
                            "Error parsing request: the WordNet concept you are trying to wrap is already wrapped.",
                            jsonObject, false);
                }
            }
        }

        return new JsonValidationResult(null, jsonObject, true);
    }

    private JsonValidationResult validatePOS(JSONObject jsonObject, ConceptEntry entry) {
        // In case user has entered a POS value. Validate whether POS is
        // same as wordnet POS
        if (!entry.getPos().equalsIgnoreCase(jsonObject.get(JsonFields.POS).toString())) {
            return new JsonValidationResult(
                    "Error parsing request: please enter POS that matches with the wordnet POS " + entry.getPos(),
                    jsonObject, false);
        }
        return new JsonValidationResult(null, jsonObject, true);
    }

    private ConceptEntry createEntry(JSONObject jsonObject, String username) {
        ConceptEntry conceptEntry = new ConceptEntry();
        if (jsonObject.get(JsonFields.WORDNET_ID) != null) {
            conceptEntry.setWordnetId(jsonObject.get(JsonFields.WORDNET_ID).toString());
            String[] wrappers = jsonObject.get(JsonFields.WORDNET_ID).toString().split(Constants.CONCEPT_SEPARATOR);
            if (wrappers.length > 0) {
                ConceptEntry existingConceptEntry = conceptManager.getConceptEntry(wrappers[0]);
                conceptEntry.setWord(existingConceptEntry.getWord().replace("_", " "));
                conceptEntry.setPos(existingConceptEntry.getPos());
            }
        } else {
            conceptEntry.setWord(jsonObject.get(JsonFields.WORD).toString());
            conceptEntry.setPos(jsonObject.get(JsonFields.POS).toString());
        }
        conceptEntry.setConceptList(jsonObject.get(JsonFields.CONCEPT_LIST).toString());
        conceptEntry.setCreatorId(username);
        conceptEntry.setSynonymIds(jsonObject.get(JsonFields.SYNONYM_IDS) != null
                ? jsonObject.get(JsonFields.SYNONYM_IDS).toString() : "");
        conceptEntry.setDescription(jsonObject.get(JsonFields.DESCRIPTION) != null
                ? jsonObject.get(JsonFields.DESCRIPTION).toString() : "");
        conceptEntry.setEqualTo(
                jsonObject.get(JsonFields.EQUALS) != null ? jsonObject.get(JsonFields.EQUALS).toString() : "");
        conceptEntry.setSimilarTo(
                jsonObject.get(JsonFields.SIMILAR) != null ? jsonObject.get(JsonFields.SIMILAR).toString() : "");
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
