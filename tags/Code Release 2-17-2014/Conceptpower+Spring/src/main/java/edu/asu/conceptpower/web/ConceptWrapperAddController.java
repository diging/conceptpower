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
import edu.asu.conceptpower.wrapper.ConceptEntryWrapper;
import edu.asu.conceptpower.wrapper.ConceptEntryWrapperCreator;

@Controller
public class ConceptWrapperAddController {

	private String concept;
	private List<ConceptEntryWrapper> foundConcepts;
	private String pos;
	private ArrayList<ConceptEntry> wrappedConcepts;
	private List<ConceptList> allLists;
	private ConceptType[] allTypes;
	private List<ConceptEntry> synonyms;
	private ConceptEntry conceptEntry;
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

		allTypes = conceptTypesManager.getAllTypes();
		Map<String, String> types = new LinkedHashMap<String, String>();
		for (ConceptType conceptType : allTypes) {
			types.put(conceptType.getTypeId(), conceptType.getTypeName());
		}

		model.addAttribute("types", types);

		allLists = conceptManager.getAllConceptLists();
		Map<String, String> lists = new LinkedHashMap<String, String>();
		for (ConceptList conceptList : allLists) {
			lists.put(conceptList.getConceptListName(),
					conceptList.getConceptListName());
		}
		model.addAttribute("lists", lists);

		if (synonyms != null) {
			synonyms.clear();
		}

		return "/auth/conceptlist/addconceptwrapper";
	}

	@RequestMapping(value = "auth/conceptlist/addconceptwrapper/add", method = RequestMethod.POST)
	public String addConcept(HttpServletRequest req, ModelMap model,
			Principal principal) {

		try {
			conceptEntry = new ConceptEntry();

			StringBuffer sb = new StringBuffer();

			if (synonyms != null)
				for (ConceptEntry synonym : synonyms)
					sb.append(synonym.getId() + Constants.SYNONYM_SEPARATOR);

			conceptEntry.setSynonymIds(sb.toString());

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

		concept = req.getParameter("name");
		pos = req.getParameter("pos");
		if (!concept.trim().isEmpty()) {

			ConceptEntry[] found = conceptManager.getConceptListEntriesForWord(
					concept, pos);
			foundConcepts = wrapperCreator.createWrappers(found);
			model.addAttribute("result", foundConcepts);
		}

		allTypes = conceptTypesManager.getAllTypes();
		Map<String, String> types = new LinkedHashMap<String, String>();
		for (ConceptType conceptType : allTypes) {
			types.put(conceptType.getTypeId(), conceptType.getTypeName());
		}

		model.addAttribute("types", types);

		allLists = conceptManager.getAllConceptLists();
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

	@RequestMapping(method = RequestMethod.GET, value = "conceptWrapperAddSynonymView")
	public @ResponseBody
	ConceptEntry[] searchConcept(ModelMap model,
			@RequestParam("synonymname") String synonymname) {
		ConceptEntry[] entries = conceptManager
				.getConceptListEntriesForWord(synonymname.trim());
		return entries;
	}

	@RequestMapping(method = RequestMethod.GET, value = "conceptWrapperAddAddSynonym")
	public @ResponseBody
	ConceptEntry[] addSynonym(ModelMap model,
			@RequestParam("synonymid") String synonymid) {
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

	@RequestMapping(method = RequestMethod.GET, value = "conceptWrapperAddRemoveSynonym")
	public @ResponseBody
	ConceptEntry[] removeSynonym(ModelMap model,
			@RequestParam("synonymid") String synonymid) {
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
