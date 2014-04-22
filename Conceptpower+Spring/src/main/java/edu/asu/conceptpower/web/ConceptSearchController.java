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
import edu.asu.conceptpower.wrapper.IConceptWrapperCreator;
import edu.asu.conceptpower.wrapper.impl.ConceptEntryWrapperCreator;

/**
 * This class provides concept search methods
 * 
 * @author Chetan
 * 
 */
@Controller
public class ConceptSearchController {

	@Autowired
	private ConceptManager conceptManager;

	@Autowired
	private IConceptWrapperCreator wrapperCreator;

	
	/**
	 * This method is searches concepts a specific term and pos
	 * 
	 * @param req
	 *            Holds the HTTP request information
	 * @param model
	 *            Generic model holder for servlet
	 * @return Returns a string value to redirect user to concept search page
	 */
	@RequestMapping(value = "/home/conceptsearch", method = RequestMethod.GET)
	public String search(HttpServletRequest req, ModelMap model) {

		String concept = req.getParameter("name");
		String pos = req.getParameter("pos");

		if (concept != null && !concept.trim().isEmpty()) {

			ConceptEntry[] found = conceptManager.getConceptListEntriesForWord(
					concept, pos);

			List<ConceptEntryWrapper> foundConcepts = wrapperCreator
					.createWrappers(found);

			model.addAttribute("conceptsresult", foundConcepts);

		}

		return "conceptsearch";
	}
	

	
}
