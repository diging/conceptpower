package edu.asu.conceptpower.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptManager;
import edu.asu.conceptpower.wrapper.ConceptEntryWrapper;
import edu.asu.conceptpower.wrapper.ConceptEntryWrapperCreator;

@Controller
public class ConceptDeleteView {

	@Autowired
	ConceptManager conceptManager;

	@Autowired
	ConceptEntryWrapperCreator wrapperCreator;

	@RequestMapping(value = "auth/concepts/deleteconcept/{conceptid}", method = RequestMethod.GET)
	public String deleteConcept(@PathVariable("conceptid") String conceptid,
			HttpServletRequest req, ModelMap model) {
		ConceptEntry concept = conceptManager.getConceptEntry(conceptid);
		model.addAttribute("word", concept.getWord());
		model.addAttribute("description", concept.getDescription());
		model.addAttribute("id", concept.getId());
		model.addAttribute("wordnetId", concept.getWordnetId());
		model.addAttribute("pos", concept.getPos());
		model.addAttribute("conceptList", concept.getConceptList());
		model.addAttribute("type", concept.getTypeId());
		model.addAttribute("equal", concept.getEqualTo());
		model.addAttribute("similar", concept.getSimilarTo());
		model.addAttribute("user", concept.getModified());
		model.addAttribute("modified", concept.getModified());

		return "/auth/concepts/deleteconcept";
	}

	@RequestMapping(value = "auth/concepts/canceldelete/{conceptList}", method = RequestMethod.GET)
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

	@RequestMapping(value = "auth/concepts/deleteconceptconfirm/{id}", method = RequestMethod.GET)
	public String confirmlDelete(@PathVariable("id") String id,
			HttpServletRequest req, ModelMap model) {
		ConceptEntry concept = conceptManager.getConceptEntry(id);

		concept.setDeleted(true);
		// set modified and user details
		conceptManager.storeModifiedConcept(concept);

		List<ConceptEntry> founds = conceptManager
				.getConceptListEntries(concept.getConceptList());

		List<ConceptEntryWrapper> foundConcepts = wrapperCreator
				.createWrappers(founds != null ? founds
						.toArray(new ConceptEntry[founds.size()])
						: new ConceptEntry[0]);

		model.addAttribute("result", foundConcepts);
		return "/auth/concepts/ConceptListView";
	}

}
