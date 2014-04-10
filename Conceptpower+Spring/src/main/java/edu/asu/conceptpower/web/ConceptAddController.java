package edu.asu.conceptpower.web;

import java.security.Principal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptList;
import edu.asu.conceptpower.core.ConceptManager;
import edu.asu.conceptpower.core.ConceptType;
import edu.asu.conceptpower.core.ConceptTypesManager;
import edu.asu.conceptpower.core.Constants;
import edu.asu.conceptpower.exceptions.DictionaryDoesNotExistException;
import edu.asu.conceptpower.exceptions.DictionaryModifyException;
import edu.asu.conceptpower.profile.impl.ServiceBackBean;
import edu.asu.conceptpower.profile.impl.ServiceRegistry;
import edu.asu.conceptpower.web.back.SearchResultBackBeanForm;
import edu.asu.conceptpower.wrapper.ConceptEntryWrapperCreator;

/**
 * This class provides all the methods required for new concept creation
 * 
 * @author Chetan
 * 
 */
@Controller
public class ConceptAddController {

	@Autowired
	private ConceptManager conceptManager;

	@Autowired
	private ConceptListController conceptListManager;

	@Autowired
	ConceptEntryWrapperCreator wrapperCreator;

	@Autowired
	private ConceptTypesManager conceptTypesManager;

	@Autowired
	private ServiceRegistry serviceRegistry;

	private List<ConceptEntry> synonyms;

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

		List<ConceptList> allLists = conceptManager.getAllConceptLists();
		Map<String, String> lists = new LinkedHashMap<String, String>();
		for (ConceptList conceptList : allLists) {
			lists.put(conceptList.getConceptListName(),
					conceptList.getConceptListName());
		}
		model.addAttribute("lists", lists);

		if (synonyms != null) {
			synonyms.clear();
		}

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

			StringBuffer sb = new StringBuffer();

			if (synonyms != null)
				for (ConceptEntry synonym : synonyms)
					sb.append(synonym.getId() + Constants.SYNONYM_SEPARATOR);

			conceptEntry.setSynonymIds(sb.toString());

			conceptEntry.setWord(req.getParameter("name"));
			conceptEntry.setConceptList(req.getParameter("lists"));
			conceptEntry.setPos(req.getParameter("pos"));
			conceptEntry.setDescription(req.getParameter("description"));
			conceptEntry.setEqualTo(req.getParameter("equals"));
			conceptEntry.setSimilarTo(req.getParameter("similar"));
			conceptEntry.setTypeId(req.getParameter("types"));
			conceptEntry.setCreatorId(principal.getName());
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
	public @ResponseBody
	ConceptEntry[] getSynonyms(@RequestParam("synonymname") String synonymname) {
		ConceptEntry[] entries = conceptManager
				.getConceptListEntriesForWord(synonymname.trim());
		return entries;
	}

	/**
	 * This method provides array of existing concepts for a given string
	 * 
	 * @param conceptname
	 *            A string value for which we need to find existing concepts
	 * @return Returns existing concepts which contain conceptname
	 */
	@RequestMapping(method = RequestMethod.GET, value = "getExistingConcepts")
	public @ResponseBody
	ConceptEntry[] getExistingConcepts(
			@RequestParam("conceptname") String conceptname) {
		if (conceptname.isEmpty())
			return null;
		ConceptEntry[] entries = conceptManager
				.getConceptListEntriesForWord(conceptname.trim());
		return entries;
	}

	/**
	 * This method adds a synonym for a concept
	 * 
	 * @param synonymid
	 *            Synonym ID which should be added for a concept
	 * @return Returns array of synonyms added
	 */
	@RequestMapping(method = RequestMethod.GET, value = "conceptAddAddSynonym")
	public @ResponseBody
	ConceptEntry[] addSynonym(@RequestParam("synonymid") String synonymid) {
		ConceptEntry synonym = conceptManager.getConceptEntry(synonymid.trim());
		if (synonyms == null) {
			synonyms = new ArrayList<ConceptEntry>();
		}
		synonyms.add(synonym);
		ConceptEntry[] arraySynonyms = new ConceptEntry[synonyms.size()];
		int i = 0;
		for (ConceptEntry syn : synonyms) {
			arraySynonyms[i++] = syn;
		}
		return arraySynonyms;
	}	

	/**
	 * This method removes the synonym from synonyms list for id synonymid
	 * 
	 * @param synonymid
	 *            A synonym which has to removed for a concept
	 * @return Returns updated array of synonyms
	 */
	@RequestMapping(method = RequestMethod.GET, value = "conceptAddRemoveSynonym")
	public @ResponseBody
	ConceptEntry[] removeSynonym(@RequestParam("synonymid") String synonymid) {
		if (synonyms != null) {
			for (int i = 0; i < synonyms.size(); i++) {
				if (synonyms.get(i).getId().equals(synonymid)) {
					synonyms.remove(i);
				}
			}
		}
		ConceptEntry[] arraySynonyms = new ConceptEntry[synonyms.size()];
		int i = 0;
		for (ConceptEntry syn : synonyms) {
			arraySynonyms[i++] = syn;
		}
		return arraySynonyms;
	}
}
