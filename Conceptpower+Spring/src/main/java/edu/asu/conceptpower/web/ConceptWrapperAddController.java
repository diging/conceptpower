package edu.asu.conceptpower.web;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.asu.conceptpower.app.exceptions.DictionaryDoesNotExistException;
import edu.asu.conceptpower.app.exceptions.DictionaryModifyException;
import edu.asu.conceptpower.app.exceptions.IndexerRunningException;
import edu.asu.conceptpower.app.exceptions.LuceneException;
import edu.asu.conceptpower.app.manager.IConceptManager;
import edu.asu.conceptpower.app.manager.IIndexService;
import edu.asu.conceptpower.app.service.IConceptWrapperService;
import edu.asu.conceptpower.app.validation.ConceptWrapperAddBeanValidator;
import edu.asu.conceptpower.app.wordnet.Constants;
import edu.asu.conceptpower.app.wrapper.ConceptEntryWrapper;
import edu.asu.conceptpower.app.wrapper.IConceptWrapperCreator;
import edu.asu.conceptpower.app.model.ConceptEntry;
import edu.asu.conceptpower.web.backing.ConceptWrapperAddBean;
import jakarta.servlet.http.HttpServletRequest;

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
    private IIndexService indexService;

    @Autowired
    private IConceptWrapperService conceptWrapperService;

    @Value("${messages['INDEXER_RUNNING']}")
    private String indexerRunning;

    @Autowired
    private ConceptWrapperAddBeanValidator conceptWrapperAddBeanValidator;

    private static final Logger logger = LoggerFactory.getLogger(ConceptWrapperAddController.class);

    @InitBinder
    private void initBinder(WebDataBinder binder) {
        binder.setValidator(conceptWrapperAddBeanValidator);
    }

    /**
     * This method provides required information for concept wrapper creation
     * 
     * @param model
     *                  A generic model holder for Servlet
     * @return String value which redirects user to creating concept wrappers page
     */
    @RequestMapping(value = "auth/conceptlist/addconceptwrapper")
    public String prepareConceptWrapperAdd(@RequestParam(value = "wrapperId", required = false) String wrapperId,
            HttpServletRequest req, ModelMap model) {
        model.addAttribute("types", conceptWrapperService.fetchAllConceptTypes());
        model.addAttribute("lists", conceptWrapperService.fetchAllConceptLists());
        ConceptEntry entry = null;
        ConceptWrapperAddBean conceptWrapperAddBean = new ConceptWrapperAddBean();

        if (wrapperId != null && !wrapperId.trim().isEmpty()) {
            try {
                entry = conceptManager.getWordnetConceptEntry(wrapperId);
            } catch (LuceneException ex) {
                logger.error("Error while fetching concepts based on concept id", ex);
                return "/layouts/concepts/addconceptwrapper";
            }

            conceptWrapperAddBean.setDescription(entry.getDescription());
            conceptWrapperAddBean.setWord(entry.getWord());
            conceptWrapperAddBean.setSelectedConceptList(entry.getConceptList());
            conceptWrapperAddBean.setPos(entry.getPos());
            conceptWrapperAddBean.setWrapperids(wrapperId);
        }
        model.addAttribute("conceptWrapperAddBean", conceptWrapperAddBean);
        return "/layouts/concepts/addconceptwrapper";
    }

    /**
     * This method creates a concept wrapper for the selected concept entries
     * 
     * @param req
     *                      Holds the HTTP request information
     * @param principal
     *                      Holds logged in user information
     * @return String value which redirects user to a particular concept list page
     * @throws DictionaryModifyException
     * @throws DictionaryDoesNotExistException
     * @throws IllegalAccessException
     * @throws IndexerRunningException
     */
    @RequestMapping(value = "auth/conceptlist/addconceptwrapper/add", method = RequestMethod.POST)
    public String addConcept(@Validated @ModelAttribute ConceptWrapperAddBean conceptWrapperAddBean,
            BindingResult result, Principal principal, Model model) throws DictionaryDoesNotExistException,
            DictionaryModifyException, LuceneException, IllegalAccessException, IndexerRunningException {

        if (result.hasErrors()) {
            model.addAttribute("types", conceptWrapperService.fetchAllConceptTypes());
            model.addAttribute("lists", conceptWrapperService.fetchAllConceptLists());
            return "/layouts/concepts/addconceptwrapper";
        }

        String[] wrappers = conceptWrapperAddBean.getWrapperids().split(Constants.CONCEPT_SEPARATOR);
        if (wrappers.length > 0) {
            ConceptEntry conceptEntry = new ConceptEntry();
            conceptEntry.setWord(conceptWrapperAddBean.getWord());
            conceptEntry.setPos(conceptManager.getConceptEntry(wrappers[0]).getPos());
            conceptEntry.setSynonymIds(conceptWrapperAddBean.getSynonymids());
            conceptEntry.setWordnetId(conceptWrapperAddBean.getWrapperids());
            conceptEntry.setConceptList(conceptWrapperAddBean.getSelectedConceptList());
            conceptEntry.setDescription(conceptWrapperAddBean.getDescription());
            conceptEntry.setEqualTo(conceptWrapperAddBean.getEquals());
            conceptEntry.setSimilarTo(conceptWrapperAddBean.getSimilar());
            conceptEntry.setTypeId(conceptWrapperAddBean.getSelectedType());
            conceptEntry.setCreatorId(principal.getName());

            if (indexService.isIndexerRunning()) {
                model.addAttribute("show_error_alert", true);
                model.addAttribute("error_alert_msg", indexerRunning);
                return "forward:/layouts/concepts/addconceptwrapper";
            }
            conceptManager.addConceptListEntry(conceptEntry, principal.getName());
        }

        return "redirect:/auth/" + conceptWrapperAddBean.getSelectedConceptList() + "/concepts";
    }

    /**
     * This method provides search result for a particular concept search query
     * 
     * @param req
     *                  Holds HTTP request information
     * @param model
     *                  A generic model holder for Servlet
     * @return String value which redirects user to concept wrapper creation page
     * @throws IllegalAccessException
     */
    @RequestMapping(value = "/auth/conceptlist/addconceptwrapper/conceptsearch", method = RequestMethod.POST)
    public String search(@ModelAttribute ConceptWrapperAddBean conceptWrapperAddBean, HttpServletRequest req,
            ModelMap model) throws LuceneException, IllegalAccessException {

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
                found = conceptManager.getConceptListEntriesForWordPOS(concept, pos, Constants.WORDNET_DICTIONARY);
            } catch (IndexerRunningException ie) {
                model.addAttribute(indexerRunning, ie.getMessage());
                return "/login";
            }

            List<ConceptEntryWrapper> foundConcepts = wrapperCreator.createWrappers(found);
            model.addAttribute("result", foundConcepts);
        }
        model.addAttribute("types", conceptWrapperService.fetchAllConceptTypes());
        model.addAttribute("lists", conceptWrapperService.fetchAllConceptLists());
        model.addAttribute("conceptWrapperAddBean", conceptWrapperAddBean);

        return "layouts/concepts/addconceptwrapper";
    }

    /**
     * This method provides array of concepts for a given string
     * 
     * @param synonymname
     *                        A synonym string for which we need to find existing
     *                        concepts
     * @return Returns array of concepts found for synonym name
     * @throws IllegalAccessException
     */
    @RequestMapping(method = RequestMethod.GET, value = "conceptWrapperAddSynonymView")
    public ResponseEntity<String> getSynonyms(@RequestParam("synonymname") String synonymname)
            throws IllegalAccessException {
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
            jsonStringBuilder.append("\"description\":\"" + StringEscapeUtils.escapeHtml4(syn.getDescription()) + "\"");
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
