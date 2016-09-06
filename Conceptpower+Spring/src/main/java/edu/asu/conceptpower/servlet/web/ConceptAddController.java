package edu.asu.conceptpower.servlet.web;

import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ArrayUtils;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptList;
import edu.asu.conceptpower.core.ConceptType;
import edu.asu.conceptpower.servlet.core.IConceptListManager;
import edu.asu.conceptpower.servlet.core.IConceptManager;
import edu.asu.conceptpower.servlet.core.IConceptTypeManger;
import edu.asu.conceptpower.servlet.core.IIndexService;
import edu.asu.conceptpower.servlet.exceptions.DictionaryDoesNotExistException;
import edu.asu.conceptpower.servlet.exceptions.DictionaryModifyException;
import edu.asu.conceptpower.servlet.exceptions.IndexerRunningException;
import edu.asu.conceptpower.servlet.exceptions.LuceneException;
import edu.asu.conceptpower.servlet.profile.impl.ServiceBackBean;
import edu.asu.conceptpower.servlet.profile.impl.ServiceRegistry;
import edu.asu.conceptpower.servlet.web.backing.SearchResultBackBeanForm;

/**
 * This class provides all the methods required for new concept creation
 * 
 * @author Chetan
 * 
 */
@Controller
public class ConceptAddController {
	
	private static final Logger logger = LoggerFactory
			.getLogger(ConceptAddController.class);

	@Autowired
	private IConceptManager conceptManager;
	
	@Autowired
	private IConceptListManager conceptListManager;

	@Autowired
	private IConceptTypeManger conceptTypesManager;

	@Autowired
	private ServiceRegistry serviceRegistry;
	
	@Autowired
	private IIndexService indexService;
	
	@Value("#{messages['INDEXER_RUNNING']}")
    private String indexerRunning;

	/**
	 * This method provides initial types and list model elements
	 * 
	 * @param model
	 *            A generic model holder for Servlet
	 * @return returns string which redirects to concept creation page
	 */
	@RequestMapping(value = "auth/conceptlist/addconcept")
	public String prepareConceptAdd(ModelMap model) {

		model.addAttribute("ServiceBackBean", new ServiceBackBean());
		Map<String, String> serviceNameIdMap = serviceRegistry
				.getServiceNameIdMap();
		model.addAttribute("serviceNameIdMap", serviceNameIdMap);
		model.addAttribute("SearchResultBackBeanForm",
				new SearchResultBackBeanForm());

		ConceptType[] allTypes = conceptTypesManager.getAllTypes();
		Map<String, String> types = new LinkedHashMap<String, String>();
		for (ConceptType conceptType : allTypes) {
			types.put(conceptType.getTypeId(), conceptType.getTypeName());
		}

		model.addAttribute("types", types);

		List<ConceptList> allLists = conceptListManager.getAllConceptLists();
		Map<String, String> lists = new LinkedHashMap<String, String>();
		for (ConceptList conceptList : allLists) {
			lists.put(conceptList.getConceptListName(),
					conceptList.getConceptListName());
		}
		model.addAttribute("lists", lists);

		return "/auth/conceptlist/addconcept";
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
    public String addConcept(HttpServletRequest req, Principal principal, ModelMap model) throws LuceneException, DictionaryDoesNotExistException, DictionaryModifyException, IllegalAccessException, IndexerRunningException {

        ConceptEntry conceptEntry = new ConceptEntry();
        conceptEntry.setSynonymIds(req.getParameter("synonymsids"));
        conceptEntry.setWord(req.getParameter("name"));
        conceptEntry.setConceptList(req.getParameter("lists"));
        conceptEntry.setPos(req.getParameter("pos"));
        conceptEntry.setDescription(req.getParameter("description"));
        conceptEntry.setEqualTo(req.getParameter("equals"));
        conceptEntry.setSimilarTo(req.getParameter("similar"));
        conceptEntry.setTypeId(req.getParameter("types"));
        conceptEntry.setCreatorId(principal.getName());
        
        //Checking if indexer is already running.
        if(indexService.isIndexerRunning()){
            model.addAttribute("show_error_alert", true);
            model.addAttribute("error_alert_msg", indexerRunning);
        	return "forward:/auth/conceptlist/addconcept";
        }
        conceptManager.addConceptListEntry(conceptEntry, principal.getName());
        return "redirect:/auth/" + req.getParameter("lists") + "/concepts";
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
            return new ResponseEntity<String>(e1.getMessage(), HttpStatus.CONFLICT);
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
            return new ResponseEntity<String>(e1.getMessage(),HttpStatus.CONFLICT);
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
