package edu.asu.conceptpower.web;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.asu.conceptpower.app.bean.ConceptEditBean;
import edu.asu.conceptpower.app.core.Constants;
import edu.asu.conceptpower.app.exceptions.IndexerRunningException;
import edu.asu.conceptpower.app.exceptions.LuceneException;
import edu.asu.conceptpower.app.manager.IConceptListManager;
import edu.asu.conceptpower.app.manager.IConceptManager;
import edu.asu.conceptpower.app.manager.IConceptTypeManager;
import edu.asu.conceptpower.app.manager.IIndexService;
import edu.asu.conceptpower.app.model.ConceptEntry;
import edu.asu.conceptpower.app.model.ConceptList;
import edu.asu.conceptpower.app.model.ConceptType;
import edu.asu.conceptpower.app.service.IConceptEditService;
import edu.asu.conceptpower.app.users.IUserManager;
import edu.asu.conceptpower.app.wrapper.ConceptEntryWrapper;
import edu.asu.conceptpower.app.wrapper.IConceptWrapperCreator;

/**
 * This method provides all the required methods for editing a concept
 * 
 * @author Chetan
 * 
 */
@Controller
public class ConceptEditController {

    @Autowired
    private IConceptManager conceptManager;

    @Autowired
    private IConceptListManager conceptListService;

    @Autowired
    private IConceptWrapperCreator wrapperCreator;

    @Autowired
    private IUserManager usersManager;

    @Autowired
    private IConceptTypeManager conceptTypesManager;

    @Autowired
    private IIndexService indexService;

    @Autowired
    private IConceptEditService conceptEditService;

    @Value("#{messages['INDEXER_RUNNING']}")
    private String indexerRunning;

    @Value("#{messages['INDEXERSTATUS']}")
    private String indexerStatus;

    private static final int numberOfRecords = 20;

    /**
     * This method provides information of a concept to be edited for concept
     * edit page
     * 
     * @param conceptid
     *            ID of a concept to be edited
     * @param model
     *            A generic model holder for Servlet
     * @return String value to redirect user to concept edit page
     */
    @RequestMapping(value = "auth/conceptlist/editconcept/{conceptid}", method = RequestMethod.GET)
    public String prepareEditConcept(@PathVariable("conceptid") String conceptid,
            @RequestParam(value = "fromHomeScreen", required = false) String fromHomeScreen,
            @RequestParam(value = "searchWord", required = false) String searchWord,
            @ModelAttribute("conceptEditBean") ConceptEditBean conceptEditBean, ModelMap model, BindingResult results)
            throws LuceneException {

        if (fromHomeScreen != null) {
            conceptEditBean.setFromHomeScreen(true);
        }
        
        ConceptEntry concept = conceptManager.getConceptEntry(conceptid);
        List<ConceptType> allTypes = conceptTypesManager.getAllTypes();
        List<ConceptList> allLists = conceptListService.getAllConceptLists();
        conceptEditBean.setWord(concept.getWord());
        conceptEditBean.setSelectedPosValue(concept.getPos().toLowerCase());
        conceptEditBean.setConceptListValue(concept.getConceptList());
        conceptEditBean.setSelectedListName(concept.getConceptList());
        conceptEditBean.setConceptList(allLists);
        conceptEditBean.setDescription(concept.getDescription().trim());
        conceptEditBean.setSynonymsids(concept.getSynonymIds());
        conceptEditBean.setSelectedTypeId(concept.getTypeId());
        conceptEditBean.setTypes(allTypes);
        conceptEditBean.setSelectedTypeId(concept.getTypeId());
        conceptEditBean.setEquals(concept.getEqualTo());
        conceptEditBean.setSimilar(concept.getSimilarTo());
        conceptEditBean.setConceptId(concept.getId());
        conceptEditBean.setConceptEntryList(new ArrayList<>());
        conceptEditBean.setWordnetIds(concept.getWordnetId());
        conceptEditBean.setExistingWordnetIds(concept.getWordnetId());
        model.addAttribute("conceptId", concept.getId());
        model.addAttribute("searchWord", searchWord);
        
        return "/layouts/concepts/editconcept";
    }

    /**
     * This method redirects user to a particular concept list page when the
     * user cancels concept edit operation
     * 
     * @param conceptList
     *            Concept list where user has to be redirected
     * @return String value which redirect user to a particular concept list
     *         page
     */
    @GetMapping(value = "auth/concepts/canceledit")
    public String cancelEdit(@RequestParam(value = "fromHomeScreen", required = false) String fromHomeScreen) {
        if (fromHomeScreen.equalsIgnoreCase("true")) {
            return "redirect:/login";
        }
        return "redirect:/auth/conceptlist";
    }

    /**
     * This method stores the updated information of a concept
     * 
     * @param id
     *            ID of a concept to be edited
     * @param req
     *            Holds HTTP request information
     * @param principal
     *            Holds logged in user information
     * @return String value which redirects user to a particular concept list
     *         page
     * @throws IllegalAccessException
     * @throws IndexerRunningException
     */
    @PostMapping(value = "auth/conceptlist/editconcept/edit/{id}")
    public String confirmEdit(@PathVariable("id") String id, HttpServletRequest req, Principal principal,
            @ModelAttribute("conceptEditBean") ConceptEditBean conceptEditBean, BindingResult result, Model model)
            throws LuceneException, IllegalAccessException, IndexerRunningException {
        ConceptEntry conceptEntry = conceptManager.getConceptEntry(id);
        conceptEntry.setWord(conceptEditBean.getWord());
        conceptEntry.setWordnetId(conceptEditBean.getWordnetIds());
        conceptEntry.setConceptList(conceptEditBean.getConceptListValue());
        conceptEntry.setPos(conceptEditBean.getSelectedPosValue());
        conceptEntry.setDescription(conceptEditBean.getDescription());
        conceptEntry.setEqualTo(conceptEditBean.getEquals());
        conceptEntry.setSimilarTo(conceptEditBean.getSimilar());
        conceptEntry.setTypeId(conceptEditBean.getSelectedTypeId());
        conceptEntry.setSynonymIds(conceptEditBean.getSynonymsids());
        conceptEntry.setModifiedUser(principal.getName());

        String userId = usersManager.findUser(principal.getName()).getUsername();
        conceptEntry.setModified(userId);
        
        if (indexService.isIndexerRunning()) {
            List<ConceptList> allLists = conceptListService.getAllConceptLists();
            conceptEditBean.setConceptList(allLists);
            List<ConceptType> allTypes = conceptTypesManager.getAllTypes();
            conceptEditBean.setTypes(allTypes);
            conceptEditBean.setConceptId(conceptEntry.getId());
            model.addAttribute("conceptId", conceptEntry.getId());
            model.addAttribute("show_error_alert", true);
            model.addAttribute("error_alert_msg", indexerRunning);
            model.addAttribute(indexerStatus, indexerRunning);
            return "/layouts/concepts/editconcept";
        }

        conceptEditService.editConcepts(conceptEntry, conceptEditBean.getExistingWordnetIds(),
                conceptEditBean.getWordnetIds(), principal.getName());

        if (conceptEditBean.isFromHomeScreen()) {
            return "redirect:/home/conceptsearch?word=" + conceptEditBean.getWord() + "&pos="
                    + conceptEditBean.getSelectedPosValue();
        }
        return "redirect:/auth/" + conceptEditBean.getConceptListValue() + "/concepts";
    }

    /**
     * This method provides the existing concept words for a given synonym
     * 
     * @param synonymname
     *            Synonym concept name for which the existing concepts has to be
     *            fetched
     * @return The list of existing concepts
     * @throws IllegalAccessException
     * @throws IndexerRunningException
     */
    @GetMapping(value = "conceptEditSynonymView")
    public ResponseEntity<String> searchConcept(@RequestParam("synonymname") String synonymname)
            throws LuceneException, IllegalAccessException, IndexerRunningException {
        ConceptEntry[] entries = conceptManager.getConceptListEntriesForWord(synonymname.trim());
        List<ConceptEntry> synonyms = Arrays.asList(entries);
        return new ResponseEntity<>(buildJSON(synonyms, true, false), HttpStatus.OK);
    }

    /**
     * This method provides the list of existing synonyms for a concept
     * 
     * @param conceptid
     *            Holds the ID of a concept
     * @param model
     *            A generic model holder for Servlet
     * @return List of existing synonyms
     * @throws JSONException
     */
    @GetMapping(value = "getConceptEditSynonyms")
    public ResponseEntity<String> getSynonyms(@RequestParam("conceptid") String conceptid, ModelMap model)
            throws LuceneException {
        List<ConceptEntry> synonyms = new ArrayList<>();
        ConceptEntry concept = conceptManager.getConceptEntry(conceptid);
        String synonymIds = concept.getSynonymIds();

        // Inside getConceptEntry . In fillConceptEntry the below logic is
        // performed
        if (synonymIds != null) {
            String[] ids = synonymIds.trim().split(Constants.SYNONYM_SEPARATOR);
            if (ids != null) {
                for (String id : ids) {
                    if (id == null || id.isEmpty())
                        continue;
                    ConceptEntry synonym = conceptManager.getConceptEntry(id);
                    if (synonym != null)
                        synonyms.add(synonym);
                }
            }
        }
        return new ResponseEntity<>(buildJSON(synonyms, false, true), HttpStatus.OK);
    }

    /**
     * This method fetches the synonym details based on the synonym id selected
     * in the Add synonym table.
     * 
     * @param synonymid
     *            Holds the ID of a synonym
     * @param model
     *            A generic model holder for Servlet
     * @return synonym details
     */
    @GetMapping(value = "getConceptAddSynonyms")
    public ResponseEntity<String> getSynonymRows(@RequestParam("synonymid") String synonymid, ModelMap model)
            throws LuceneException {
        ConceptEntry synonym = null;
        synonym = conceptManager.getConceptEntry(synonymid);

        List<ConceptEntry> synonymList = new ArrayList<>();
        synonymList.add(synonym);
        return new ResponseEntity<>(buildJSON(synonymList, false, true), HttpStatus.OK);
    }

    private String buildJSON(List<ConceptEntry> synonyms, boolean posRequired, boolean synonymRequired) {

        int i = 0;
        StringBuilder jsonStringBuilder = new StringBuilder("{");
        jsonStringBuilder.append("\"Total\"");
        jsonStringBuilder.append(":");
        jsonStringBuilder.append(synonyms.size());
        jsonStringBuilder.append(",");
        jsonStringBuilder.append("\"synonyms\":");
        jsonStringBuilder.append("[");
        for (ConceptEntry syn : synonyms) {
            // Appending for next element in JSON
            if (i != 0) {
                jsonStringBuilder.append(",");
            }

            jsonStringBuilder.append("{");
            jsonStringBuilder.append("\"Id\":\"" + syn.getId() + "\"");
            jsonStringBuilder.append(",");
            jsonStringBuilder.append("\"Word\":\"" + syn.getWord() + "\"");
            jsonStringBuilder.append(",");
            jsonStringBuilder.append("\"Description\":\"" + syn.getDescription().replaceAll("\"", "'") + "\"");
            jsonStringBuilder.append(",");
            String pos = syn.getPos().replaceAll("\"", "'");
            jsonStringBuilder.append("\"Pos\":\"" + pos + "\"");
            jsonStringBuilder.append(",");
            jsonStringBuilder.append("\"SynonymObject\":\"" + syn + "\"");
            jsonStringBuilder.append("}");
            i++;
        }
        jsonStringBuilder.append("]");
        jsonStringBuilder.append("}");
        return jsonStringBuilder.toString();
    }

    @GetMapping(value = "conceptEdit/search")
    public @ResponseBody ResponseEntity<Object> searchConcept(@RequestParam("concept") String concept,
            @RequestParam("pos") String pos) throws IllegalAccessException, LuceneException {

        List<ConceptEntryWrapper> foundConcepts = null;

        if (!concept.trim().isEmpty()) {

            ConceptEntry[] found = null;

            if (indexService.isIndexerRunning()) {
                return new ResponseEntity<>("Indexer is running. Please try again later.",
                        HttpStatus.SERVICE_UNAVAILABLE);
            }

            try {
                found = conceptManager.getConceptListEntriesForWordPOS(concept, pos, Constants.WORDNET_DICTIONARY, -1,
                        numberOfRecords, null, 0);
            } catch (IndexerRunningException ie) {
                return new ResponseEntity<>("Indexer is running. Please try again later.",
                        HttpStatus.SERVICE_UNAVAILABLE);
            }

            foundConcepts = new CopyOnWriteArrayList<>(wrapperCreator.createWrappers(found));
        }

        return new ResponseEntity<>(foundConcepts, HttpStatus.OK);
    }

}
