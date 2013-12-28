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
public class ConceptAddViewController {

	@Autowired
	private ConceptManager conceptManager;

	@Autowired
	private ConceptListManager conceptListManager;

	@Autowired
	ConceptEntryWrapperCreator wrapperCreator;

	@Autowired
	private ConceptTypesManager conceptTypesManager;

	private List<ConceptList> allLists;
	private ConceptType[] allTypes;
	private ConceptEntry conceptEntry;
	private List<ConceptEntry> synonyms;

	@RequestMapping(value = "auth/concepts/ConceptAddView")
	public String conceptAddView(HttpServletRequest req, ModelMap model) {

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

		return "/auth/concepts/ConceptAddView";
	}

	@RequestMapping(value = "auth/concepts/createconcept", method = RequestMethod.POST)
	public String createConcept(HttpServletRequest req, ModelMap model,
			Principal principal) {

		try {
			conceptEntry = new ConceptEntry();

			StringBuffer sb = new StringBuffer();
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

		List<ConceptEntry> founds = conceptManager.getConceptListEntries(req
				.getParameter("lists"));

		List<ConceptEntryWrapper> foundConcepts = wrapperCreator
				.createWrappers(founds != null ? founds
						.toArray(new ConceptEntry[founds.size()])
						: new ConceptEntry[0]);

		model.addAttribute("result", foundConcepts);
		return "/auth/concepts/ConceptListView";
	}

	@RequestMapping(method = RequestMethod.GET, value = "conceptAddSynonymView")
	public @ResponseBody
	ConceptEntry[] searchConcept(ModelMap model,
			@RequestParam("synonymname") String synonymname) {
		ConceptEntry[] entries = conceptManager
				.getConceptListEntriesForWord(synonymname.trim());
		return entries;
	}

	@RequestMapping(method = RequestMethod.GET, value = "conceptAddAddSynonym")
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

	@RequestMapping(method = RequestMethod.GET, value = "conceptAddRemoveSynonym")
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
