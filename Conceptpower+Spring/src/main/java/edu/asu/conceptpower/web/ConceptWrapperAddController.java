package edu.asu.conceptpower.web;

import java.security.Principal;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.asu.conceptpower.app.core.IConceptListManager;
import edu.asu.conceptpower.app.core.IConceptManager;
import edu.asu.conceptpower.app.core.IConceptTypeManger;
import edu.asu.conceptpower.app.core.IIndexService;
import edu.asu.conceptpower.app.exceptions.DictionaryDoesNotExistException;
import edu.asu.conceptpower.app.exceptions.DictionaryModifyException;
import edu.asu.conceptpower.app.exceptions.IndexerRunningException;
import edu.asu.conceptpower.app.exceptions.LuceneException;
import edu.asu.conceptpower.app.wordnet.Constants;
import edu.asu.conceptpower.app.wrapper.ConceptEntryWrapper;
import edu.asu.conceptpower.app.wrapper.IConceptWrapperCreator;
import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptList;
import edu.asu.conceptpower.core.ConceptType;

/**
 * This class provides methods required for creating concept wrappers
 * 
 * @author Chetan Ambi
 * 
 */
@Controller
public class ConceptWrapperAddController {

    @Autowired
    private IConceptWrapperCreator wrapperCreator;

    @Autowired
    private IConceptManager conceptManager;

    @Autowired
    private IConceptListManager conceptListManager;

    @Autowired
    private IConceptTypeManger conceptTypesManager;
    
    @Autowired
    private IIndexService indexService;
    
    @Value("#{messages['INDEXER_RUNNING']}")
    private String indexerRunning;

    private static final Logger logger = LoggerFactory.getLogger(ConceptWrapperAddController.class);

    /**
     * This method provides required information for concept wrapper creation
     * 
     * @param model
     *            A generic model holder for Servlet
     * @return String value which redirects user to creating concept wrappers
     *         page
     */
    @RequestMapping(value = "auth/conceptlist/addconceptwrapper")
    public String prepareConceptWrapperAdd(HttpServletRequest req, ModelMap model) {

        if (req.getAttribute("errormsg") != null)
            model.addAttribute("errormsg", req.getAttribute("errormsg"));

        ConceptType[] allTypes = conceptTypesManager.getAllTypes();
        Map<String, String> types = new LinkedHashMap<String, String>();
        for (ConceptType conceptType : allTypes) {
            types.put(conceptType.getTypeId(), conceptType.getTypeName());
        }

        model.addAttribute("types", types);

        List<ConceptList> allLists = conceptListManager.getAllConceptLists();
        Map<String, String> lists = new LinkedHashMap<String, String>();
        for (ConceptList conceptList : allLists) {
            lists.put(conceptList.getConceptListName(), conceptList.getConceptListName());
        }
        model.addAttribute("lists", lists);
        return "/auth/conceptlist/addconceptwrapper";
    }

    /**
     * This method creates a concept wrapper for the selected concept entries
     * 
     * @param req
     *            Holds the HTTP request information
     * @param principal
     *            Holds logged in user information
     * @return String value which redirects user to a particular concept list
     *         page
     * @throws DictionaryModifyException
     * @throws DictionaryDoesNotExistException
     * @throws IllegalAccessException 
     * @throws IndexerRunningException 
     */
    @RequestMapping(value = "auth/conceptlist/addconceptwrapper/add", method = RequestMethod.POST)
    public String addConcept(HttpServletRequest req, Principal principal, Model model)
            throws DictionaryDoesNotExistException, DictionaryModifyException, LuceneException, IllegalAccessException, IndexerRunningException {

        if (req.getParameter("lists") == null || req.getParameter("lists").trim().isEmpty()) {
            req.setAttribute("errormsg", "You have to select a concept list.");
            return "forward:/auth/conceptlist/addconceptwrapper";
        }
        String[] wrappers = req.getParameter("wrapperids").split(Constants.CONCEPT_SEPARATOR);
		if (wrappers.length > 0) {
			ConceptEntry conceptEntry = new ConceptEntry();
			conceptEntry.setWord(conceptManager.getConceptEntry(wrappers[0]).getWord().replace("_", " "));
			conceptEntry.setPos(conceptManager.getConceptEntry(wrappers[0]).getPos());
			conceptEntry.setSynonymIds(req.getParameter("synonymsids"));
			conceptEntry.setWordnetId(req.getParameter("wrapperids"));
			conceptEntry.setConceptList(req.getParameter("lists"));
			conceptEntry.setDescription(req.getParameter("description"));
			conceptEntry.setEqualTo(req.getParameter("equals"));
			conceptEntry.setSimilarTo(req.getParameter("similar"));
			conceptEntry.setTypeId(req.getParameter("types"));
			conceptEntry.setCreatorId(principal.getName());
			
            if (indexService.isIndexerRunning()) {
                model.addAttribute("show_error_alert", true);
                model.addAttribute("error_alert_msg", indexerRunning);
                // Need to include command Object
                return "forward:/auth/conceptlist/addconceptwrapper";
            }
            conceptManager.addConceptListEntry(conceptEntry, principal.getName());
		}

        return "redirect:/auth/" + req.getParameter("lists") + "/concepts";
    }

    /**
     * This method provides search result for a particular concept search query
     * 
     * @param req
     *            Holds HTTP request information
     * @param model
     *            A generic model holder for Servlet
     * @return String value which redirects user to concept wrapper creation
     *         page
     * @throws IllegalAccessException
     * @throws IndexerRunningException
     */
    @RequestMapping(value = "/auth/conceptlist/addconceptwrapper/conceptsearch", method = RequestMethod.POST)
    public String search(HttpServletRequest req, ModelMap model)
            throws LuceneException, IllegalAccessException, IndexerRunningException {

        String concept = req.getParameter("name");
        String pos = req.getParameter("pos");
        if (!concept.trim().isEmpty()) {

            ConceptEntry[] found = null;
            
            if (indexService.isIndexerRunning()) {
                model.addAttribute("show_error_alert", true);
                model.addAttribute("error_alert_msg", indexerRunning);
                return "/login";
            }

            try {
                found = conceptManager.getConceptListEntriesForWordPOS(concept, pos,
                        Constants.WORDNET_DICTIONARY);
            } catch (IndexerRunningException ie) {
                model.addAttribute(indexerRunning, ie.getMessage());
                return "/login";
            }

            List<ConceptEntryWrapper> foundConcepts = wrapperCreator.createWrappers(found);
            model.addAttribute("result", foundConcepts);
        }
        ConceptType[] allTypes = conceptTypesManager.getAllTypes();
        Map<String, String> types = new LinkedHashMap<String, String>();
        for (ConceptType conceptType : allTypes) {
            types.put(conceptType.getTypeId(), conceptType.getTypeName());
        }

        model.addAttribute("types", types);

        List<ConceptList> allLists = conceptListManager.getAllConceptLists();
        Map<String, String> lists = new LinkedHashMap<String, String>();
        for (ConceptList conceptList : allLists) {
            lists.put(conceptList.getConceptListName(), conceptList.getConceptListName());
        }
        model.addAttribute("lists", lists);

        return "/auth/conceptlist/addconceptwrapper";
    }

    /**
     * This method provides array of concepts for a given string
     * 
     * @param synonymname
     *            A synonym string for which we need to find existing concepts
     * @return Returns array of concepts found for synonym name
     * @throws IllegalAccessException 
     */
    @RequestMapping(method = RequestMethod.GET, value = "conceptWrapperAddSynonymView")
    public ResponseEntity<String> getSynonyms(@RequestParam("synonymname") String synonymname) throws IllegalAccessException {
        ConceptEntry[] entries = null;
        try {
            entries = conceptManager.getConceptListEntriesForWord(synonymname.trim());
        } catch (LuceneException ex) {
            logger.error("Lucene exception", ex);
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IndexerRunningException ex) {
            logger.info("Indexer running exception", ex);
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.OK);
        }
        return new ResponseEntity<String>(buildJSON(Arrays.asList(entries)), HttpStatus.OK);
    }

    private String buildJSON(List<ConceptEntry> synonyms) {

        int i = 0;
        StringBuffer jsonStringBuilder = new StringBuffer("{");
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
            jsonStringBuilder.append("\"id\":\"" + syn.getId() + "\"");
            jsonStringBuilder.append(",");
            jsonStringBuilder.append("\"word\":\"" + syn.getWord() + "\"");
            jsonStringBuilder.append(",");
            jsonStringBuilder.append("\"description\":\"" + StringEscapeUtils.escapeHtml(syn.getDescription()) + "\"");
            jsonStringBuilder.append(",");
            String pos = syn.getPos().replaceAll("\"", "'");
            jsonStringBuilder.append("\"pos\":\"" + pos + "\"");
            jsonStringBuilder.append("}");
            i++;
        }
        jsonStringBuilder.append("]");
        jsonStringBuilder.append("}");
        return jsonStringBuilder.toString();
    }

}
