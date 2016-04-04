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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
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
import edu.asu.conceptpower.servlet.exceptions.DictionaryDoesNotExistException;
import edu.asu.conceptpower.servlet.exceptions.DictionaryModifyException;
import edu.asu.conceptpower.servlet.profile.impl.ServiceRegistry;
import edu.asu.conceptpower.servlet.wrapper.IConceptWrapperCreator;
import edu.asu.conceptpower.web.backing.ConceptAddBean;

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
	public String prepareConceptAdd(ModelMap model,@ModelAttribute("conceptAddBean")ConceptAddBean conceptAddBean) {
		onLoad(conceptAddBean);
		return "/auth/conceptlist/addconcept";
	}

	private void onLoad(ConceptAddBean conceptAddBean) {
		Map<String, String> serviceNameIdMap = serviceRegistry
				.getServiceNameIdMap();
		conceptAddBean.setServiceNameIdMap(serviceNameIdMap);

		ConceptType[] allTypes = conceptTypesManager.getAllTypes();
		Map<String, String> types = new LinkedHashMap<String, String>();
		for (ConceptType conceptType : allTypes) {
			types.put(conceptType.getTypeId(), conceptType.getTypeName());
		}

		conceptAddBean.setTypes(types);
		
		List<ConceptList> allLists = conceptListManager.getAllConceptLists();
		Map<String, String> lists = new LinkedHashMap<String, String>();
		for (ConceptList conceptList : allLists) {
			lists.put(conceptList.getConceptListName(),
					conceptList.getConceptListName());
		}
		
		conceptAddBean.setLists(lists);
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
	public String addConcept(HttpServletRequest req, Principal principal, @ModelAttribute("conceptAddBean")ConceptAddBean conceptAddBean) {
		onLoad(conceptAddBean);
		try {
			boolean test = true;
			
			conceptAddBean.getSynonymId();
			conceptAddBean.getSynonymDescription();
			conceptAddBean.getTerm();
			
			conceptAddBean.getTypes();
			
			ConceptEntry conceptEntry = new ConceptEntry();

			conceptEntry.setSynonymIds(conceptAddBean.getSynonymsids());
			conceptEntry.setWord(conceptAddBean.getName());
			conceptEntry.setConceptList(conceptAddBean.getSelectedList());
			conceptEntry.setPos(conceptAddBean.getPos());
			conceptEntry.setDescription(conceptAddBean.getDescription());
			conceptEntry.setEqualTo(conceptAddBean.getEquals());
			conceptEntry.setSimilarTo(conceptAddBean.getSimilar());
			conceptEntry.setTypeId(conceptAddBean.getSelectedTypes());
			conceptEntry.setCreatorId(principal.getName());
			if(test == true)
			return "/auth/conceptlist/addconcept";
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
