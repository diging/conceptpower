package edu.asu.conceptpower.rest;

import java.security.Principal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.text.StringEscapeUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.asu.conceptpower.app.core.Constants;
import edu.asu.conceptpower.app.core.IConceptManager;
import edu.asu.conceptpower.app.core.IConceptTypeManger;
import edu.asu.conceptpower.app.core.POS;
import edu.asu.conceptpower.app.exceptions.DictionaryDoesNotExistException;
import edu.asu.conceptpower.app.exceptions.DictionaryModifyException;
import edu.asu.conceptpower.app.exceptions.IndexerRunningException;
import edu.asu.conceptpower.app.exceptions.LuceneException;
import edu.asu.conceptpower.app.util.URIHelper;
import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptType;
import edu.asu.conceptpower.rest.msg.json.ConceptEntryMessage;
import edu.asu.conceptpower.rest.msg.json.JsonConceptMessage;
import edu.mit.jwi.item.WordID;

@Controller
public class Concepts {

    @Autowired
    private IConceptManager conceptManager;

    @Autowired
    private IConceptTypeManger typeManager;

    @Autowired
    private URIHelper uriHelper;

    private JSONObject jsonObject;

    private static final Logger logger = LoggerFactory.getLogger(Concepts.class);

    /**
     * This method creates concepts as well as concept wrappers. This is decided
     * based on the request parameter. If the request parameter contains one or
     * more wordnet ids then concept wrapper is created else a new concept is
     * created.
     *
     * Sample input for creating a concept wrapper:
     *
     * {    "wordnetIds":
     *      "WID-00450866-N-01-pony-trekking,WID-03981924-N-01-pony_cart",
     *      "synonymids" : "WID-02380464-N-01-polo_pony,WID-04206225-N-03-pony",
     *      "conceptlist" : "FirstList-1", "type" :
     *      "88bea1dc-1443-4296-8315-715c71128b01", "description" : "Description",
     *      "equal_to" : "http://viaf.org/viaf/38882290", "similarTo" :
     *      "http://viaf.org/viaf/43723621" }
     *
     * Please note any word or pos passed during concept wrapper creation will
     * be ignored. Word and pos details are fetched from wordnet. In case POS is
     * entered by user, the code validates with the wordnet POS and throws an
     * error if POS is not matching with wordnet
     *
     * Sample input for creating a concept:
     *
     * { "word": "kitty", "pos": "noun", "conceptlist": "mylist", "description":
     *      "Soft kitty, sleepy kitty, little ball of fur.", "type":
     *      "3b755111-545a-4c1c-929c-a2c0d4c3913b" }
     *
     * @param conceptEntryMessage
     * @param principal
     * @return
     * @throws IndexerRunningException
     * @throws LuceneException
     * @throws IllegalAccessException
     * @throws JsonProcessingException
     * @throws POSMismatchException
     */
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/concept/add", method = RequestMethod.POST)
    public ResponseEntity<String> addConcept(@RequestBody ConceptEntryMessage conceptEntryMessage, Principal principal)
            throws IllegalAccessException, LuceneException, IndexerRunningException, JsonProcessingException {

        JsonValidationResult result = checkConceptEntryMessage(conceptEntryMessage);

        if (result.isValid() && conceptEntryMessage.getWordnetId() != null) {
                        result = checkJsonObjectForWrapper(conceptEntryMessage);
        }

        if (!result.isValid()) {
            return new ResponseEntity<String>(getObjectMapper().writeValueAsString(result.getMessage()),
                                        HttpStatus.BAD_REQUEST);
        }

        ConceptEntry conceptEntry = createEntry(conceptEntryMessage, principal.getName());

        if (conceptEntryMessage.getWordnetId() != null) {
                        result = validatePOS(conceptEntryMessage, conceptEntry);
        }

        if (!result.isValid()) {
            return new ResponseEntity<String>(getObjectMapper().writeValueAsString(result.getMessage()),
                                        HttpStatus.BAD_REQUEST);
        }

        // check type
        String typeId = conceptEntry.getTypeId();
        ConceptType type = typeManager.getType(typeId);
        if (type == null) {
            return new ResponseEntity<String>(
                                getObjectMapper()
                                                .writeValueAsString("The type id you are submitting doesn't " + "match any existing type."),
                    HttpStatus.BAD_REQUEST);
        }

        try {
            conceptEntry.setId(conceptManager.addConceptListEntry(conceptEntry, principal.getName()).getId());
        } catch (DictionaryDoesNotExistException e) {
            logger.error("Error creating concept from REST call.", e);
            return new ResponseEntity<String>(
                                        getObjectMapper().writeValueAsString("Specified concept list does not exist in Conceptpower."),
                    HttpStatus.BAD_REQUEST);
        } catch (DictionaryModifyException e) {
            logger.error("Error creating concept from REST call.", e);
            return new ResponseEntity<String>(
                                        getObjectMapper().writeValueAsString("Specified concept list can't be modified."),
                                        HttpStatus.BAD_REQUEST);
        } catch (LuceneException le) {
            logger.error("Error creating concept from REST call.", le);
            return new ResponseEntity<String>(getObjectMapper().writeValueAsString("Concept Cannot be added"),
                                        HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IllegalAccessException e) {
            logger.error("Error creating concept from REST call.", e);
            return new ResponseEntity<String>(getObjectMapper().writeValueAsString("Illegal Access"),
                                        HttpStatus.BAD_REQUEST);
        }

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", MediaType.APPLICATION_JSON_VALUE + "; charset=utf-8");
        JsonConceptMessage jsonConceptMessage = new JsonConceptMessage(uriHelper);
                Map<ConceptEntry, ConceptType> entryTypeMap = new HashMap<>();
                entryTypeMap.put(conceptEntry, type);

                return new ResponseEntity<String>(
                        jsonConceptMessage.getAllConceptEntriesAndPaginationDetails(entryTypeMap, null), responseHeaders,
                        HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/concepts/add", method = RequestMethod.POST)
    public ResponseEntity<String> addConcepts(@RequestBody List<ConceptEntryMessage> conceptEntryMessages,
                        Principal principal)
            throws IllegalAccessException, LuceneException, IndexerRunningException, JsonProcessingException {

        Map<ConceptEntry, ConceptType> entryTypeMap = new HashMap<>();
                for (ConceptEntryMessage conceptEntryMessage : conceptEntryMessages) {

                    JsonValidationResult result = checkConceptEntryMessage(conceptEntryMessage);

                    if (result.isValid() && conceptEntryMessage.getWordnetId() != null) {
                                        result = checkJsonObjectForWrapper(conceptEntryMessage);
            }

            if (!result.isValid()) {
                return new ResponseEntity<String>(getObjectMapper().writeValueAsString(result.getMessage()),
                                                HttpStatus.BAD_REQUEST);
            }

            ConceptEntry conceptEntry = createEntry(conceptEntryMessage, principal.getName());

            if (conceptEntryMessage.getWordnetId() != null) {
                                validatePOS(conceptEntryMessage, conceptEntry);
            }

            if (!result.isValid()) {
             // responseObj.put("success", false);
                continue;
            }

            try {
                ConceptEntry entry = conceptManager.addConceptListEntry(conceptEntry, principal.getName());
                                entryTypeMap.put(entry, typeManager.getType(entry.getTypeId()));
            } catch (DictionaryDoesNotExistException e) {
                logger.error("Error creating concept from REST call.", e);
             // responseObj.put("success", false);
             // responseObj.put("error_message", "Specified dictionary does
             // not exist in Conceptpower.");
            } catch (DictionaryModifyException e) {
                logger.error("Error creating concept from REST call.", e);
             // responseObj.put("success", false);
             // responseObj.put("error_message", "Specified dictionary can't
             // be modified.");
            } catch (LuceneException le) {
                logger.error("Error creating concept from REST call.", le);
                return new ResponseEntity<String>(getObjectMapper().writeValueAsString("Concept Cannot be added"),
                                                HttpStatus.INTERNAL_SERVER_ERROR);
            } catch (IllegalAccessException e) {
                logger.error("Error creating concept from REST call.", e);
                return new ResponseEntity<String>(getObjectMapper().writeValueAsString("Illegal Access"),
                                                HttpStatus.BAD_REQUEST);
            }
        }

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", MediaType.APPLICATION_JSON_VALUE + "; charset=utf-8");
        JsonConceptMessage jsonConceptMessage = new JsonConceptMessage(uriHelper);
        return new ResponseEntity<String>(
                jsonConceptMessage.getAllConceptEntriesAndPaginationDetails(entryTypeMap, null), responseHeaders,
                HttpStatus.OK);
    }

    private JsonValidationResult checkConceptEntryMessage(ConceptEntryMessage conceptEntryMessage)
            throws IllegalAccessException, LuceneException, IndexerRunningException {
        if (conceptEntryMessage.getPos() == null) {
            return new JsonValidationResult("Error parsing request: please provide a POS ('pos' attribute).",
                    jsonObject, false);
        }

        String pos = conceptEntryMessage.getPos();
        if (!POS.posValues.contains(pos)) {
            logger.debug("Error creating concept from REST call. " + pos + " does not exist.");
            return new JsonValidationResult("Error parsing request: please provide a valid POS ('pos' attribute).",
                    jsonObject, false);
        }

        if (conceptEntryMessage.getLemma() == null) {
            return new JsonValidationResult(
                    "Error parsing request: please provide a word for the concept ('word' attribute).", jsonObject,
                    false);
        }

        if (conceptEntryMessage.getConceptList() == null) {
            return new JsonValidationResult(
                    "Error parsing request: please provide a list name for the concept ('list' attribute).", jsonObject,
                    false);
        }

        if (conceptEntryMessage.getType() == null) {
            return new JsonValidationResult(
                    "Error parsing request: please provide a type for the concept ('type' attribute).", jsonObject,
                    false);
        }

        return new JsonValidationResult(null, jsonObject, true);
    }

    private JsonValidationResult checkJsonObjectForWrapper(ConceptEntryMessage conceptEntryMessage)
            throws IllegalAccessException, LuceneException, IndexerRunningException {

        // Validation to check if wordnet ids are seperated by comma
        if (conceptEntryMessage.getWordnetId() != null) {
            List<String> wordnetIdsList = Arrays
                    .asList(conceptEntryMessage.getWordnetId().split("\\s*" + Constants.CONCEPT_SEPARATOR + "\\s*"));
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

    private JsonValidationResult validatePOS(ConceptEntryMessage conceptEntryMessage, ConceptEntry entry) {
        // In case user has entered a POS value. Validate whether POS is
        // same as wordnet POS
        if (!entry.getPos().equalsIgnoreCase(conceptEntryMessage.getPos())) {
            return new JsonValidationResult(
                    "Error parsing request: please enter POS that matches with the wordnet POS " + entry.getPos(),
                    jsonObject, false);
        }
        return new JsonValidationResult(null, jsonObject, true);
    }

    private ConceptEntry createEntry(ConceptEntryMessage conceptEntryMessage, String username) {
        ConceptEntry conceptEntry = new ConceptEntry();
        if (conceptEntryMessage.getWordnetId() != null) {
           conceptEntry.setWordnetId(conceptEntryMessage.getWordnetId());
           String[] wrappers = conceptEntryMessage.getWordnetId().split(Constants.CONCEPT_SEPARATOR);
            if (wrappers.length > 0) {
                ConceptEntry existingConceptEntry = conceptManager.getConceptEntry(wrappers[0]);
                conceptEntry.setWord(existingConceptEntry.getWord().replace("_", " "));
                conceptEntry.setPos(existingConceptEntry.getPos());
            }
        } else {
            conceptEntry.setWord(conceptEntryMessage.getLemma());
            conceptEntry.setPos(conceptEntry.getPos());
        }
        conceptEntry.setConceptList(conceptEntryMessage.getConceptList());
        conceptEntry.setCreatorId(username);
        conceptEntry
                        .setSynonymIds(conceptEntryMessage.getSynonymIds() != null ? conceptEntryMessage.getSynonymIds() : "");
                conceptEntry.setDescription(conceptEntry.getDescription() != null ? conceptEntry.getDescription() : "");
                conceptEntry.setEqualTo(conceptEntryMessage.getEqualTo() != null ? conceptEntryMessage.getEqualTo() : "");
                conceptEntry.setSimilarTo(conceptEntryMessage.getSimilarTo() != null ? conceptEntryMessage.getSimilarTo() : "");
                conceptEntry.setTypeId(conceptEntryMessage.getType().getTypeId());

        return conceptEntry;
    }

    private ObjectMapper getObjectMapper() {
                return new ObjectMapper();
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
