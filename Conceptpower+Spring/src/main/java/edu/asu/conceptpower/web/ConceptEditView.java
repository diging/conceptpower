package edu.asu.conceptpower.web;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptList;
import edu.asu.conceptpower.core.ConceptManager;
import edu.asu.conceptpower.core.ConceptType;
import edu.asu.conceptpower.core.ConceptTypesManager;
import edu.asu.conceptpower.core.Constants;
import edu.asu.conceptpower.users.impl.UsersManager;
import edu.asu.conceptpower.wrapper.ConceptEntryWrapper;
import edu.asu.conceptpower.wrapper.ConceptEntryWrapperCreator;

@Controller
public class ConceptEditView {

	@Autowired
	ConceptManager conceptManager;

	@Autowired
	private UsersManager usersManager;

	@Autowired
	private ConceptTypesManager conceptTypesManager;

	@Autowired
	ConceptEntryWrapperCreator wrapperCreator;

	private List<ConceptList> allLists;
	private ConceptType[] allTypes;
	private List<ConceptEntry> synonyms;

	@RequestMapping(value = "auth/conceptlist/editconcept/{conceptid}", method = RequestMethod.GET)
	public String prepateEditConcept(
			@PathVariable("conceptid") String conceptid,
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

		synonyms = new ArrayList<ConceptEntry>();
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

		return "/auth/conceptlist/editconcept";
	}

	@RequestMapping(value = "auth/concepts/canceledit/{conceptList}", method = RequestMethod.GET)
	public String cancelEdit(@PathVariable("conceptList") String conceptList,
			HttpServletRequest req, ModelMap model) {
		return "redirect:/auth/" + conceptList + "/concepts";
	}

	@RequestMapping(value = "auth/conceptlist/editconcept/edit/{id}", method = RequestMethod.POST)
	public String confirmlEdit(@PathVariable("id") String id,
			HttpServletRequest req, Principal principal, ModelMap model) {

		ConceptEntry conceptEntry = conceptManager.getConceptEntry(id);
		conceptEntry.setWord(req.getParameter("name"));
		conceptEntry.setConceptList(req.getParameter("lists"));
		conceptEntry.setPos(req.getParameter("poss"));
		conceptEntry.setDescription(req.getParameter("description"));
		conceptEntry.setEqualTo(req.getParameter("equal"));
		conceptEntry.setSimilarTo(req.getParameter("similar"));
		conceptEntry.setTypeId(req.getParameter("types"));

		StringBuffer sb = new StringBuffer();
		if (synonyms != null)
			for (ConceptEntry synonym : synonyms)
				sb.append(synonym.getId() + Constants.SYNONYM_SEPARATOR);

		conceptEntry.setSynonymIds(sb.toString());

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

	@RequestMapping(method = RequestMethod.GET, value = "conceptEditSynonymView")
	public @ResponseBody
	ConceptEntry[] searchConcept(ModelMap model,
			@RequestParam("synonymname") String synonymname) {
		ConceptEntry[] entries = conceptManager
				.getConceptListEntriesForWord(synonymname.trim());
		return entries;
	}

	@RequestMapping(method = RequestMethod.GET, value = "conceptEditAddSynonym")
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

	@RequestMapping(method = RequestMethod.GET, value = "conceptEditRemoveSynonym")
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

	@RequestMapping(method = RequestMethod.GET, value = "getConceptEditSynonyms")
	public @ResponseBody
	ConceptEntry[] getSynonyms(ModelMap model) {

		ConceptEntry[] arraySynonyms = new ConceptEntry[synonyms.size()];
		int i = 0;
		for (ConceptEntry syn : synonyms) {
			arraySynonyms[i++] = syn;
		}
		return arraySynonyms;
	}

}
