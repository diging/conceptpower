package edu.asu.conceptpower.web;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.asu.conceptpower.core.ChangeEvent;
import edu.asu.conceptpower.core.ChangeEventConstants;
import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptList;
import edu.asu.conceptpower.core.ConceptType;
import edu.asu.conceptpower.core.IConceptListManager;
import edu.asu.conceptpower.core.IConceptManager;
import edu.asu.conceptpower.core.IConceptTypeManger;
import edu.asu.conceptpower.exceptions.DictionaryDoesNotExistException;
import edu.asu.conceptpower.exceptions.DictionaryModifyException;
import edu.asu.conceptpower.profile.impl.ServiceBackBean;
import edu.asu.conceptpower.profile.impl.ServiceRegistry;
import edu.asu.conceptpower.web.backing.SearchResultBackBeanForm;
import edu.asu.conceptpower.wrapper.IConceptWrapperCreator;

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
	private ConceptListController conceptListController;

	@Autowired
	private IConceptWrapperCreator wrapperCreator;

	@Autowired
	private IConceptTypeManger conceptTypesManager;

	@Autowired
	private ServiceRegistry serviceRegistry;

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
	 */
	@RequestMapping(value = "auth/conceptlist/addconcept/add", method = RequestMethod.POST)
	public String addConcept(HttpServletRequest req, Principal principal) {

		try {
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

			ChangeEvent changeEvent = new ChangeEvent(conceptEntry.getCreatorId(), new Date().toString(),
					ChangeEventConstants.CREATION);
			List<ChangeEvent> changeEventList = new ArrayList<ChangeEvent>();
			changeEventList.add(changeEvent);
			conceptEntry.setChangeEvent(changeEventList);
			conceptManager.addConceptListEntry(conceptEntry);

		} catch (DictionaryDoesNotExistException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "failed";
		} catch (DictionaryModifyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "failed";
		}

		return "redirect:/auth/" + req.getParameter("lists") + "/concepts";
	}

	/**
	 * This method provides array of concepts for a given string
	 * 
	 * @param synonymname
	 *            A synonym string for which we need to find existing concepts
	 * @return Returns array of concepts found for synonym name
	 */
	@RequestMapping(method = RequestMethod.GET, value = "conceptAddSynonymView")
    public @ResponseBody ResponseEntity<String> getSynonyms(@RequestParam("synonymname") String synonymname,
            @RequestParam("addedsynonym") String addedSynonnym) {
        ConceptEntry[] entries = conceptManager.getConceptListEntriesForWord(synonymname.trim());
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

        // return entries;
    }

	/**
	 * This method provides array of existing concepts for a given string
	 * 
	 * @param conceptname
	 *            A string value for which we need to find existing concepts
	 * @return Returns existing concepts which contain conceptname
	 */
	@RequestMapping(method = RequestMethod.GET, value = "getExistingConcepts")
	public @ResponseBody ResponseEntity<String> getExistingConcepts(
			@RequestParam("conceptname") String conceptname) {
		if (conceptname.isEmpty())
			return null;
		ConceptEntry[] entries = conceptManager
				.getConceptListEntriesForWord(conceptname.trim());
		
		ObjectMapper mapper=new ObjectMapper();
		ObjectWriter writer=mapper.writer();
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
