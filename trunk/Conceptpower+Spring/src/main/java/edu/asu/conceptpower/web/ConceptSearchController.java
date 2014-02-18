package edu.asu.conceptpower.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptManager;
import edu.asu.conceptpower.profile.ISearchResult;
import edu.asu.conceptpower.profile.impl.ViafService;
import edu.asu.conceptpower.wrapper.ConceptEntryWrapper;
import edu.asu.conceptpower.wrapper.ConceptEntryWrapperCreator;

@Controller
public class ConceptSearchController {

	@Autowired
	private ConceptManager conceptManager;

	@Autowired
	ConceptEntryWrapperCreator wrapperCreator;

	@Autowired
	ViafService viafService;

	private String concept;
	private List<ConceptEntryWrapper> foundConcepts;
	private String pos;
	private String searchengine;

	@RequestMapping(value = "/home/conceptsearch", method = RequestMethod.POST)
	public String search(HttpServletRequest req, ModelMap model) {

		concept = req.getParameter("name");
		pos = req.getParameter("pos");
		searchengine = req.getParameter("searchengine");

		List<ISearchResult> viafResult = viafService.search(concept);

		if (!concept.trim().isEmpty()) {

			ConceptEntry[] found = conceptManager.getConceptListEntriesForWord(
					concept, pos);

			foundConcepts = wrapperCreator.createWrappers(found);

			model.addAttribute("result", foundConcepts);

		}

		return "conceptsearch";
	}
}
