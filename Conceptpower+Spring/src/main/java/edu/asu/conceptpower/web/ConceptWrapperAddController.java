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
import edu.asu.conceptpower.wrapper.ConceptEntryWrapper;
import edu.asu.conceptpower.wrapper.ConceptEntryWrapperCreator;

@Controller
public class ConceptWrapperAddController {

	private String concept;
	private List<ConceptEntryWrapper> foundConcepts;
	private String pos;

	@Autowired
	ConceptEntryWrapperCreator wrapperCreator;

	@Autowired
	private ConceptManager conceptManager;

	@RequestMapping(value = "auth/conceptlist/addconceptwrapper")
	public String prepareConceptWrapperAdd(HttpServletRequest req,
			ModelMap model) {

		return "/auth/conceptlist/addconceptwrapper";
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

		return "/auth/conceptlist/addconceptwrapper";
	}
}
