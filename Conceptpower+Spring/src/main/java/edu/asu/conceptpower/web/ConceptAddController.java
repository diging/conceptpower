package edu.asu.conceptpower.web;

import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ArrayUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.asu.conceptpower.app.core.IConceptListManager;
import edu.asu.conceptpower.app.core.IConceptManager;
import edu.asu.conceptpower.app.core.IConceptTypeManger;
import edu.asu.conceptpower.app.core.IIndexService;
import edu.asu.conceptpower.app.exceptions.DictionaryDoesNotExistException;
import edu.asu.conceptpower.app.exceptions.DictionaryModifyException;
import edu.asu.conceptpower.app.exceptions.IndexerRunningException;
import edu.asu.conceptpower.app.exceptions.LuceneException;
import edu.asu.conceptpower.app.model.ConceptEntry;
import edu.asu.conceptpower.app.model.ConceptList;
import edu.asu.conceptpower.app.model.ConceptType;
import edu.asu.conceptpower.app.profile.impl.ServiceRegistry;
import edu.asu.conceptpower.app.util.URIHelper;
import edu.asu.conceptpower.app.validation.ConceptAddValidator;
import edu.asu.conceptpower.web.backing.ConceptAddBean;

/**
 * This class provides all the methods required for new concept creation
 * 
 * @author Chetan
 * 
 */
@Controller
public class ConceptAddController {

    private static final Logger logger = LoggerFactory.getLogger(ConceptAddController.class);

    @Autowired
    private IConceptManager conceptManager;

    @Autowired
    private IConceptListManager conceptListService;

    @Autowired
    private IConceptTypeManger conceptTypesManager;


    @Autowired
    private ServiceRegistry serviceRegistry;

    @Autowired
    private IIndexService indexService;

    @Autowired
    private URIHelper uriHelper;

    @Value("#{messages['INDEXER_RUNNING']}")
    private String indexerRunning;

    @Autowired
    private ConceptAddValidator validator;

    @InitBinder
    private void initBinder(WebDataBinder binder) {
        binder.setValidator(validator);
    }

    /**
     * This method provides initial types and list model elements
     * 
     * @param model
     *            A generic model holder for Servlet
     * @return returns string which redirects to concept creation page
     */
    @RequestMapping(value = "auth/conceptlist/addconcept")
    public String prepareConceptAdd(ModelMap model, @ModelAttribute("conceptAddBean") ConceptAddBean conceptAddBean) {

        Map<String, String> serviceNameIdMap = serviceRegistry.getServiceNameIdMap();
        conceptAddBean.setServiceNameIdMap(serviceNameIdMap);

        ConceptType[] allTypes = conceptTypesManager.getAllTypes();
        Map<String, String> types = new LinkedHashMap<>();
        for (ConceptType conceptType : allTypes) {
            types.put(conceptType.getTypeId(), conceptType.getTypeName());
        }

        conceptAddBean.setTypes(types);

        List<ConceptList> allLists = conceptListService.getAllConceptLists();
        Map<String, String> lists = new LinkedHashMap<>();
        for (ConceptList conceptList : allLists) {
            lists.put(conceptList.getConceptListName(), conceptList.getConceptListName());
        }
        conceptAddBean.setLists(lists);

        return "/layouts/concepts/addconcept";
    }

    /**
     * This method prepares a new concept and stores it using concept manager
     * 
     * @param req
     *            Holds http request object information
     * @param principal
     *            holds log in information
     * @return returns string which redirects to concept list page
     * @throws LuceneException
     * @throws DictionaryModifyException
     * @throws DictionaryDoesNotExistException
     * @throws IllegalAccessException
     * @throws IndexerRunningException
     */
    @RequestMapping(value = "auth/conceptlist/addconcept/add", method = RequestMethod.POST)
    public String addConcept(HttpServletRequest req, Principal principal,
            @Validated @ModelAttribute("conceptAddBean") ConceptAddBean conceptAddBean, ModelMap model,
            BindingResult result) throws LuceneException, DictionaryDoesNotExistException, DictionaryModifyException,
                    IllegalAccessException, IndexerRunningException {
        if (result.hasErrors()) {
            return "/auth/conceptlist/addconcept";
        }
        ConceptEntry conceptEntry = new ConceptEntry();
        try {
            conceptEntry.setSynonymIds(conceptAddBean.getSynonymsids());
            conceptEntry.setWord(conceptAddBean.getName());
            conceptEntry.setConceptList(conceptAddBean.getSelectedList());
            conceptEntry.setPos(conceptAddBean.getPos());
            conceptEntry.setDescription(conceptAddBean.getDescription());
            conceptEntry.setEqualTo(conceptAddBean.getEquals());
            conceptEntry.setSimilarTo(conceptAddBean.getSimilar());
            conceptEntry.setTypeId(conceptAddBean.getSelectedTypes());
            conceptEntry.setCreatorId(principal.getName());

            // Checking if indexer is already running.
            if ((indexService.isIndexerRunning())) {
                model.addAttribute("show_error_alert", true);
                model.addAttribute("error_alert_msg", indexerRunning);
                return "forward:/auth/conceptlist/addconcept";
            }
            conceptEntry = conceptManager.addConceptListEntry(conceptEntry, principal.getName());

        } catch (DictionaryDoesNotExistException e) {
            logger.warn("Dictionary does not exists", e);
            model.addAttribute("show_error_alert", true);
            model.addAttribute("error_alert_msg", "Concept couldn't be added. Please try again.");
            return "forward:/auth/conceptlist/addconcept";
        } catch (DictionaryModifyException dme) {
            logger.warn("Dictionary modify exception", dme);
            model.addAttribute("show_error_alert", true);
            model.addAttribute("error_alert_msg", "Concept couldn't be added. Please try again.");
            return "forward:/auth/conceptlist/addconcept";
        }
        model.addAttribute("entry", conceptEntry);
        model.addAttribute("uri", uriHelper.getURI(conceptEntry));
        return "/layouts/concepts/conceptdetails";
    }

    /**
     * This method provides array of concepts for a given string
     * 
     * @param synonymname
     *            A synonym string for which we need to find existing concepts
     * @return Returns array of concepts found for synonym name
     * @throws IllegalAccessException
     */
    @RequestMapping(method = RequestMethod.GET, value = "conceptAddSynonymView")
    public @ResponseBody ResponseEntity<String> getSynonyms(@RequestParam("synonymname") String synonymname,
            @RequestParam("addedsynonym") String addedSynonnym) throws LuceneException, IllegalAccessException {
        ConceptEntry[] entries = null;
        try {
            entries = conceptManager.getConceptListEntriesForWord(synonymname.trim());
        } catch (IndexerRunningException e1) {
            return new ResponseEntity<String>(e1.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }
        List<String> addedSynonymList = Arrays.asList(addedSynonnym.replaceAll("\\s", "").split(","));
        // Removing existing synonym from the entries.
        int i = 0;
        for (ConceptEntry concept : entries) {
            if (addedSynonymList.contains(concept.getWordnetId())) {
                entries = (ConceptEntry[]) ArrayUtils.remove(entries, i);
                i--;
            }
            i++;
        }
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer();
        try {
            return new ResponseEntity<String>(writer.writeValueAsString(entries), HttpStatus.OK);
        } catch (JsonGenerationException e) {
            logger.error("Couldn't parse results.", e);
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (JsonMappingException e) {
            logger.error("Couldn't parse results.", e);
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            logger.error("Couldn't parse results.", e);
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * This method provides array of existing concepts for a given string
     * 
     * @param conceptname
     *            A string value for which we need to find existing concepts
     * @return Returns existing concepts which contain conceptname
     * @throws IllegalAccessException
     */
    @RequestMapping(method = RequestMethod.GET, value = "getExistingConcepts")
    public @ResponseBody ResponseEntity<String> getExistingConcepts(@RequestParam("conceptname") String conceptname)
            throws LuceneException, IllegalAccessException {
        if (conceptname.isEmpty())
            return null;
        ConceptEntry[] entries = null;
        try {
            entries = conceptManager.getConceptListEntriesForWord(conceptname.trim());
        } catch (IndexerRunningException e1) {
            return new ResponseEntity<String>(e1.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer();
        try {
            return new ResponseEntity<String>(writer.writeValueAsString(entries), HttpStatus.OK);
        } catch (JsonGenerationException e) {
            logger.error("Couldn't parse results.", e);
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (JsonMappingException e) {
            logger.error("Couldn't parse results.", e);
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            logger.error("Couldn't parse results.", e);
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
