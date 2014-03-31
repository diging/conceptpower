package edu.asu.conceptpower.web;

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
import edu.asu.conceptpower.profile.IServiceRegistry;
import edu.asu.conceptpower.profile.impl.ViafService;
import edu.asu.conceptpower.web.profile.impl.SearchResultBackBean;
import edu.asu.conceptpower.web.profile.impl.SearchResultBackBeanForm;
import edu.asu.conceptpower.web.profile.impl.SearchResultBackBeanFormManager;
import edu.asu.conceptpower.wrapper.ConceptEntryWrapper;
import edu.asu.conceptpower.wrapper.ConceptEntryWrapperCreator;

/**
 * This class provides all the methods for concept search functionality
 * 
 * @author Chetan
 * 
 */
@Controller
public class ConceptSearchController {

	@Autowired
	private ConceptManager conceptManager;

	@Autowired
	ConceptEntryWrapperCreator wrapperCreator;

	@Autowired
	ViafService viafService;

	private String concept;

	// Holds the concept search result
	private List<ConceptEntryWrapper> foundConcepts;

	// Holds the pos selected
	private String pos;

	@Autowired
	private IServiceRegistry serviceRegistry;

	@Autowired
	private SearchResultBackBeanForm searchResultBackBeanForm;

	@Autowired
	private SearchResultBackBeanFormManager backBeanFormManager;

	/**
	 * This method is used to search for a concept for a specific term and pos
	 * 
	 * @param req
	 *            Holds the HTTP request information
	 * @param model
	 *            Generic model holder for servlet
	 * @return Returns a string value to redirect user to concept search page
	 */
	@RequestMapping(value = "/home/conceptsearch", method = RequestMethod.POST)
	public String search(HttpServletRequest req, ModelMap model) {

		concept = req.getParameter("name");
		pos = req.getParameter("pos");

		if (!concept.trim().isEmpty()) {

			ConceptEntry[] found = conceptManager.getConceptListEntriesForWord(
					concept, pos);

			foundConcepts = wrapperCreator.createWrappers(found);

			model.addAttribute("conceptsresult", foundConcepts);

		}

		return "conceptsearch";
	}

	/**
	 * This method searches a specific service for term
	 * 
	 * @param serviceterm
	 *            holds a string value for which we need to service search
	 * @param serviceid
	 *            holds service ID string which represents a specific service
	 *            selected by user
	 * @return Returns array of SearchResultBackBean objects which represent
	 *         service search results for specific servicterm and serviceid
	 */
	@RequestMapping(method = RequestMethod.GET, value = "serviceSearch")
	public @ResponseBody
	SearchResultBackBean[] serviceSearchForConcept(
			@RequestParam("serviceterm") String serviceterm,
			@RequestParam("serviceid") String serviceid) {

		List<SearchResultBackBean> searchResultList = backBeanFormManager
				.getsearchResultBackBeanList(serviceid, serviceterm);
		SearchResultBackBean[] result = new SearchResultBackBean[searchResultList
				.size()];
		return searchResultList.toArray(result);
	}

}
