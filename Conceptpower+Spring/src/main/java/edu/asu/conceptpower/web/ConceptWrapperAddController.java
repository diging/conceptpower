package edu.asu.conceptpower.web;

import java.security.Principal;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptList;
import edu.asu.conceptpower.core.ConceptType;
import edu.asu.conceptpower.core.Constants;
import edu.asu.conceptpower.core.IConceptListManager;
import edu.asu.conceptpower.core.IConceptManager;
import edu.asu.conceptpower.core.IConceptTypeManger;
import edu.asu.conceptpower.exceptions.DictionaryDoesNotExistException;
import edu.asu.conceptpower.exceptions.DictionaryModifyException;
import edu.asu.conceptpower.wrapper.ConceptEntryWrapper;
import edu.asu.conceptpower.wrapper.IConceptWrapperCreator;

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

	/**
	 * This method provides required information for concept wrapper creation
	 * 
	 * @param model
	 *            A generic model holder for Servlet
	 * @return String value which redirects user to creating concept wrappers
	 *         page
	 */
	@RequestMapping(value = "auth/conceptlist/addconceptwrapper")
	public String prepareConceptWrapperAdd(ModelMap model) {

		if (model.get("errormsg") != null)
			model.addAttribute("errormsg", "You have to select a concept list.");
		
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
	 */
	@RequestMapping(value = "auth/conceptlist/addconceptwrapper/add", method = RequestMethod.POST)
	public String addConcept(HttpServletRequest req, Principal principal, Model model) throws DictionaryDoesNotExistException, DictionaryModifyException {

		if (req.getParameter("lists") == null || req.getParameter("lists").trim().isEmpty()) {
			model.addAttribute("errormsg", "You have to select a concept list.");
			return "redirect:/auth/conceptlist/addconceptwrapper";
		}
		String[] wrappers = req.getParameter("wrapperids").split(
				Constants.CONCEPT_SEPARATOR);
		
		if (wrappers.length > 0) {
			ConceptEntry conceptEntry = new ConceptEntry();
			conceptEntry.setWord(conceptManager
					.getConceptEntry(wrappers[0]).getWord());
			conceptEntry.setPos(conceptManager.getConceptEntry(wrappers[0])
					.getPos());
			conceptEntry.setSynonymIds(req.getParameter("synonymsids"));
			conceptEntry.setWordnetId(req.getParameter("wrapperids"));
			conceptEntry.setConceptList(req.getParameter("lists"));
			conceptEntry.setDescription(req.getParameter("description"));
			conceptEntry.setEqualTo(req.getParameter("equals"));
			conceptEntry.setSimilarTo(req.getParameter("similar"));
			conceptEntry.setTypeId(req.getParameter("types"));
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
	 */
	@RequestMapping(value = "/auth/conceptlist/addconceptwrapper/conceptsearch", method = RequestMethod.POST)
	public String search(HttpServletRequest req, ModelMap model) {

		String concept = req.getParameter("name");
		String pos = req.getParameter("pos");
		if (!concept.trim().isEmpty()) {

			ConceptEntry[] found = conceptManager.getConceptListEntriesForWord(
					concept, pos);
			List<ConceptEntryWrapper> foundConcepts = wrapperCreator
					.createWrappers(found);
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
			lists.put(conceptList.getConceptListName(),
					conceptList.getConceptListName());
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
	 */
    @RequestMapping(method = RequestMethod.GET, value = "conceptWrapperAddSynonymView")
    public ResponseEntity<String> getSynonyms(@RequestParam("synonymname") String synonymname) {
        ConceptEntry[] entries = conceptManager.getConceptListEntriesForWord(synonymname.trim());
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
