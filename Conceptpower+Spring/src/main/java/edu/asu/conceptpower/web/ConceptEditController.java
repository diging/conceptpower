package edu.asu.conceptpower.web;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jettison.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptList;
import edu.asu.conceptpower.core.ConceptType;
import edu.asu.conceptpower.core.Constants;
import edu.asu.conceptpower.core.IConceptListManager;
import edu.asu.conceptpower.core.IConceptManager;
import edu.asu.conceptpower.core.IConceptTypeManger;
import edu.asu.conceptpower.users.IUserManager;
import edu.asu.conceptpower.wrapper.IConceptWrapperCreator;

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
	private IConceptListManager conceptListManager;

	@Autowired
	private IUserManager usersManager;

	@Autowired
	private IConceptTypeManger conceptTypesManager;

	@Autowired
	IConceptWrapperCreator wrapperCreator;

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
	public String prepareEditConcept(
			@PathVariable("conceptid") String conceptid, ModelMap model) {

		ConceptEntry concept = conceptManager.getConceptEntry(conceptid);

		ConceptType[] allTypes = conceptTypesManager.getAllTypes();
		Map<String, String> types = new LinkedHashMap<String, String>();
		for (ConceptType conceptType : allTypes) {
			types.put(conceptType.getTypeId(), conceptType.getTypeName());
		}

		List<ConceptList> allLists = conceptListManager.getAllConceptLists();
		Map<String, String> lists = new LinkedHashMap<String, String>();
		for (ConceptList conceptList : allLists) {
			lists.put(conceptList.getConceptListName(),
					conceptList.getConceptListName());
		}

		// set poss values and seleted pos
		Map<String, String> poss = new LinkedHashMap<String, String>();
		poss.put("noun", "Nouns");
		poss.put("verb", "Verbs");
		poss.put("adverb", "Adverb");
		poss.put("adjective", "Adjective");
		poss.put("other", "Other");

		model.addAttribute("word", concept.getWord());

		model.addAttribute("selectedposvalue", poss.get(concept.getPos()));
		model.addAttribute("selectedposname", concept.getPos());
		poss.remove(concept.getPos());
		model.addAttribute("poss", poss);

		model.addAttribute("selectedlistname", concept.getConceptList());
		lists.remove(concept.getConceptList());
		model.addAttribute("lists", lists);

		model.addAttribute("description", concept.getDescription());

		model.addAttribute("synonyms", concept.getSynonymIds());

		model.addAttribute("selectedtypeid", concept.getTypeId());
		model.addAttribute("selectedtypename", types.get(concept.getTypeId()));
		types.remove(concept.getTypeId());
		model.addAttribute("types", types);

		model.addAttribute("equal", concept.getEqualTo());
		model.addAttribute("similar", concept.getSimilarTo());

		model.addAttribute("conceptList", concept.getConceptList());
		model.addAttribute("conceptId", concept.getId());

		return "/auth/conceptlist/editconcept";
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
	@RequestMapping(value = "auth/concepts/canceledit/{conceptList}", method = RequestMethod.GET)
	public String cancelEdit(@PathVariable("conceptList") String conceptList) {
		return "redirect:/auth/" + conceptList + "/concepts";
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
	 */
	@RequestMapping(value = "auth/conceptlist/editconcept/edit/{id}", method = RequestMethod.POST)
	public String confirmlEdit(@PathVariable("id") String id,
			HttpServletRequest req, Principal principal) {

		ConceptEntry conceptEntry = conceptManager.getConceptEntry(id);
		conceptEntry.setWord(req.getParameter("name"));
		conceptEntry.setConceptList(req.getParameter("lists"));
		conceptEntry.setPos(req.getParameter("poss"));
		conceptEntry.setDescription(req.getParameter("description"));
		conceptEntry.setEqualTo(req.getParameter("equal"));
		conceptEntry.setSimilarTo(req.getParameter("similar"));
		conceptEntry.setTypeId(req.getParameter("types"));
		conceptEntry.setSynonymIds(req.getParameter("synonymsids"));

		String userId = usersManager.findUser(principal.getName()).getUser();
		String modified = conceptEntry.getModified() != null ? conceptEntry
				.getModified() : "";
		if (!modified.trim().isEmpty())
			modified += ", ";
		conceptEntry.setModified(modified + userId + "@"
				+ (new Date()).toString());

		conceptManager.storeModifiedConcept(conceptEntry);

		return "redirect:/auth/" + req.getParameter("lists") + "/concepts";
	}

	/**
	 * This method provides the existing concept words for a given synonym
	 * 
	 * @param synonymname
	 *            Synonym concept name for which the existing concepts has to be
	 *            fetched
	 * @return The list of existing concepts
	 */
	@RequestMapping(method = RequestMethod.GET, value = "conceptEditSynonymView")
	public @ResponseBody
	ConceptEntry[] searchConcept(@RequestParam("synonymname") String synonymname) {
		ConceptEntry[] entries = conceptManager
				.getConceptListEntriesForWord(synonymname.trim());
		return entries;
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
	@RequestMapping(method = RequestMethod.GET, value = "getConceptEditSynonyms")
	public @ResponseBody ResponseEntity<String> getSynonyms(@RequestParam("conceptid") String conceptid, ModelMap model)
			throws JSONException {

		ConceptEntry concept = conceptManager.getConceptEntry(conceptid);
		List<ConceptEntry> synonyms = new ArrayList<ConceptEntry>();
		String synonymIds = concept.getSynonymIds();
		
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
		ConceptEntry[] arraySynonyms = new ConceptEntry[synonyms.size()];
		int i = 0;
		StringBuffer jsonStringBuilder = new StringBuffer("{");
		jsonStringBuilder.append("\"Total\"");
		jsonStringBuilder.append(":");
		jsonStringBuilder.append(synonyms.size());
		jsonStringBuilder.append(",");
		jsonStringBuilder.append("\"synonyms\":");
		jsonStringBuilder.append("[");
		for (ConceptEntry syn : synonyms) {
			arraySynonyms[i++] = syn;
			// Appending for next element in JSON
			if (i != 1) {
				jsonStringBuilder.append(",");
			}
			jsonStringBuilder.append("{");
			jsonStringBuilder.append("\"Id\":\"" + syn.getId() + "\"");
			jsonStringBuilder.append(",");
			jsonStringBuilder.append("\"Word\":\"" + syn.getWord() + "\"");
			jsonStringBuilder.append(",");
			String description = syn.getDescription().replaceAll("\"", "'");
			jsonStringBuilder.append("\"Description\":\"" + description + "\"");
			jsonStringBuilder.append("}");
		}
		jsonStringBuilder.append("]");
		jsonStringBuilder.append("}");
		System.out.println(jsonStringBuilder.toString());
		return new ResponseEntity<String>(jsonStringBuilder.toString(), HttpStatus.OK);
	}

}
