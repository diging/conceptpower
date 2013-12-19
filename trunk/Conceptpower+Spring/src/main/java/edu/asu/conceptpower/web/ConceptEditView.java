package edu.asu.conceptpower.web;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
public class ConceptEditView {

	@Autowired
	ConceptManager conceptManager;

	@Autowired
	private ConceptTypesManager conceptTypesManager;

	@Autowired
	ConceptEntryWrapperCreator wrapperCreator;

	private List<ConceptList> allLists;
	private ConceptType[] allTypes;

	@RequestMapping(value = "auth/concepts/editconcept/{conceptid}", method = RequestMethod.GET)
	public String editConcept(@PathVariable("conceptid") String conceptid,
			HttpServletRequest req, ModelMap model) {

		ConceptEntry concept = conceptManager.getConceptEntry(conceptid);

		allTypes = conceptTypesManager.getAllTypes();
		Map<String, String> types = new LinkedHashMap<String, String>();
		for (ConceptType conceptType : allTypes) {
			types.put(conceptType.getTypeId(), conceptType.getTypeName());
		}

		allLists = conceptManager.getAllConceptLists();
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
		model.addAttribute("id", concept.getId());

		return "/auth/concepts/editconcept";
	}

	@RequestMapping(value = "auth/concepts/canceledit/{conceptList}", method = RequestMethod.GET)
	public String cancelDelete(@PathVariable("conceptList") String conceptList,
			HttpServletRequest req, ModelMap model) {
		List<ConceptEntry> founds = conceptManager
				.getConceptListEntries(conceptList);

		List<ConceptEntryWrapper> foundConcepts = wrapperCreator
				.createWrappers(founds != null ? founds
						.toArray(new ConceptEntry[founds.size()])
						: new ConceptEntry[0]);

		model.addAttribute("result", foundConcepts);
		return "/auth/concepts/ConceptListView";
	}

	@RequestMapping(value = "auth/concepts/editconceptconfirm/{id}", method = RequestMethod.POST)
	public String confirmlDelete(@PathVariable("id") String id,
			HttpServletRequest req, ModelMap model) {

		ConceptEntry conceptEntry = conceptManager.getConceptEntry(id);
		conceptEntry.setWord(req.getParameter("name"));
		conceptEntry.setConceptList(req.getParameter("lists"));
		conceptEntry.setPos(req.getParameter("poss"));
		conceptEntry.setDescription(req.getParameter("description"));
		conceptEntry.setEqualTo(req.getParameter("equal"));
		conceptEntry.setSimilarTo(req.getParameter("similar"));
		conceptEntry.setTypeId(req.getParameter("types"));

		// set modified and user details
		conceptManager.storeModifiedConcept(conceptEntry);

		List<ConceptEntry> founds = conceptManager
				.getConceptListEntries(conceptEntry.getConceptList());

		List<ConceptEntryWrapper> foundConcepts = wrapperCreator
				.createWrappers(founds != null ? founds
						.toArray(new ConceptEntry[founds.size()])
						: new ConceptEntry[0]);

		model.addAttribute("result", foundConcepts);
		return "/auth/concepts/ConceptListView";
	}

}
