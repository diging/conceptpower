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
import edu.asu.conceptpower.exceptions.DictionaryDoesNotExistException;
import edu.asu.conceptpower.exceptions.DictionaryModifyException;
import edu.asu.conceptpower.wrapper.ConceptEntryWrapper;
import edu.asu.conceptpower.wrapper.ConceptEntryWrapperCreator;

@Controller
public class ConceptWrapperAddController {

	private ArrayList<ConceptEntry> wrappedConcepts;
	private ConceptEntry[] arraywrappedConcepts;

	@Autowired
	ConceptEntryWrapperCreator wrapperCreator;

	@Autowired
	private ConceptManager conceptManager;

	@Autowired
	private ConceptTypesManager conceptTypesManager;

	@RequestMapping(value = "auth/conceptlist/addconceptwrapper")
	public String prepareConceptWrapperAdd(HttpServletRequest req,
			ModelMap model) {

		wrappedConcepts = new ArrayList<ConceptEntry>();
		arraywrappedConcepts = null;

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

		return "/auth/conceptlist/addconceptwrapper";
	}

	@RequestMapping(value = "auth/conceptlist/addconceptwrapper/add", method = RequestMethod.POST)
	public String addConcept(HttpServletRequest req, ModelMap model,
			Principal principal) {

		try {
			ConceptEntry conceptEntry = new ConceptEntry();

			conceptEntry.setSynonymIds(req.getParameter("synonymsids"));

			if (arraywrappedConcepts != null && arraywrappedConcepts[0] != null) {
				conceptEntry.setWord(arraywrappedConcepts[0].getWord());
				conceptEntry.setPos(arraywrappedConcepts[0].getPos());

				String wordnetID = "";

				for (int i = 0; i < arraywrappedConcepts.length; i++) {
					wordnetID += (arraywrappedConcepts[i].getWordnetId() + ",");
				}
				conceptEntry.setWordnetId(wordnetID);
			} else {
				return "failed";
			}

			conceptEntry.setConceptList(req.getParameter("lists"));
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

		List<ConceptList> allLists = conceptManager.getAllConceptLists();
		Map<String, String> lists = new LinkedHashMap<String, String>();
		for (ConceptList conceptList : allLists) {
			lists.put(conceptList.getConceptListName(),
					conceptList.getConceptListName());
		}
		model.addAttribute("lists", lists);

		return "/auth/conceptlist/addconceptwrapper";
	}

	@RequestMapping(method = RequestMethod.GET, value = "addorremoveconcepttowrapper")
	public @ResponseBody
	ConceptEntry[] addorRemoveConceptForWrapping(ModelMap model,
			@RequestParam("conceptid") String conceptid,
			@RequestParam("add") boolean add) {

		if (add) {
			ConceptEntry concept = conceptManager.getConceptEntry(conceptid
					.trim());

			wrappedConcepts.add(concept);
			arraywrappedConcepts = new ConceptEntry[wrappedConcepts.size()];
			int i = 0;
			for (ConceptEntry syn : wrappedConcepts) {
				arraywrappedConcepts[i++] = syn;
			}

		} else {

			ConceptEntry entry = null;
			for (ConceptEntry e : wrappedConcepts) {
				if (e.getId().endsWith(conceptid.trim()))
					entry = e;
			}
			wrappedConcepts.remove(entry);

			arraywrappedConcepts = new ConceptEntry[wrappedConcepts.size()];
			int i = 0;
			for (ConceptEntry syn : wrappedConcepts) {
				arraywrappedConcepts[i++] = syn;
			}

		}

		return arraywrappedConcepts;
	}

	@RequestMapping(method = RequestMethod.GET, value = "getSelectedConceptsFroWrapping")
	public @ResponseBody
	ConceptEntry[] getSelectedConceptsForWrappers(ModelMap model) {
		return arraywrappedConcepts;
	}

	@RequestMapping(method = RequestMethod.GET, value = "conceptWrapperAddSynonymView")
	public @ResponseBody
	ConceptEntry[] searchConcept(ModelMap model,
			@RequestParam("synonymname") String synonymname) {
		ConceptEntry[] entries = conceptManager
				.getConceptListEntriesForWord(synonymname.trim());
		return entries;
	}

}