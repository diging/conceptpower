package edu.asu.conceptpower.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptManager;
import edu.asu.conceptpower.wrapper.ConceptEntryWrapper;
import edu.asu.conceptpower.wrapper.ConceptEntryWrapperCreator;

@Controller
public class ConceptWrapperAddController {

	private String concept;
	private List<ConceptEntryWrapper> foundConcepts;
	private String pos;
	private ArrayList<ConceptEntry> wrappedConcepts;

	@Autowired
	ConceptEntryWrapperCreator wrapperCreator;

	@Autowired
	private ConceptManager conceptManager;

	@RequestMapping(value = "auth/conceptlist/addconceptwrapper")
	public String prepareConceptWrapperAdd(HttpServletRequest req,
			ModelMap model) {

		wrappedConcepts = new ArrayList<ConceptEntry>();

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

	@RequestMapping(method = RequestMethod.GET, value = "addorremoveconcepttowrapper")
	public @ResponseBody
	ConceptEntry[] addorRemoveConceptForWrapping(ModelMap model,
			@RequestParam("conceptid") String conceptid,
			@RequestParam("add") boolean add) {

		ConceptEntry[] arraywrappedConcepts;

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
}
