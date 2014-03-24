package edu.asu.conceptpower.web;

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
import edu.asu.conceptpower.core.ConceptManager;
import edu.asu.conceptpower.profile.IServiceRegistry;
import edu.asu.conceptpower.profile.impl.ViafService;
import edu.asu.conceptpower.web.profile.impl.SearchResultBackBean;
import edu.asu.conceptpower.web.profile.impl.SearchResultBackBeanForm;
import edu.asu.conceptpower.web.profile.impl.SearchResultBackBeanFormManager;
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

	@Autowired
	private IServiceRegistry serviceRegistry;

	@Autowired
	private SearchResultBackBeanForm searchResultBackBeanForm;

	@Autowired
	private SearchResultBackBeanFormManager backBeanFormManager;

	private String serviceId;
	private String term;

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

	@RequestMapping(method = RequestMethod.GET, value = "serviceSearch")
	public @ResponseBody
	SearchResultBackBean[] serviceSearchForConcept(ModelMap model,
			@RequestParam("serviceterm") String serviceterm,
			@RequestParam("serviceid") String serviceid) {

		serviceId = serviceid;
		term = serviceterm;

		List<SearchResultBackBean> searchResultList = backBeanFormManager
				.getsearchResultBackBeanList(serviceId, term);
		SearchResultBackBean[] result = new SearchResultBackBean[searchResultList
				.size()];
		return searchResultList.toArray(result);
	}

}
