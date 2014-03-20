package edu.asu.conceptpower.web;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptManager;
import edu.asu.conceptpower.profile.ISearchResult;
import edu.asu.conceptpower.profile.IServiceRegistry;
import edu.asu.conceptpower.profile.impl.ServiceBackBean;
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
	private String searchengine;

	@Autowired
	private IServiceRegistry serviceRegistry;

	@Autowired
	private SearchResultBackBeanForm searchResultBackBeanForm;

	@Autowired
	private SearchResultBackBeanFormManager backBeanFormManager;

	private Map<String, String> serviceNameIdMap;

	private String serviceId;
	private String term;

	@RequestMapping(value = "/home/conceptsearch", method = RequestMethod.POST)
	public String search(HttpServletRequest req, ModelMap model) {

		concept = req.getParameter("name");
		pos = req.getParameter("pos");

		model.addAttribute("ServiceBackBean", new ServiceBackBean());
		Map<String, String> serviceNameIdMap = serviceRegistry
				.getServiceNameIdMap();
		model.addAttribute("serviceNameIdMap", serviceNameIdMap);
		model.addAttribute("SearchResultBackBeanForm",
				new SearchResultBackBeanForm());

		if (!concept.trim().isEmpty()) {

			ConceptEntry[] found = conceptManager.getConceptListEntriesForWord(
					concept, pos);

			foundConcepts = wrapperCreator.createWrappers(found);

			model.addAttribute("conceptsresult", foundConcepts);

		}

		return "conceptsearch";
	}

	@RequestMapping(value = "home/service/search", method = RequestMethod.GET)
	public String search(Model model, Principal principal,
			@ModelAttribute("ServiceBackBean") ServiceBackBean serviceBackBean) {

		serviceId = serviceBackBean.getId();
		model.addAttribute("serviceid", serviceId);
		String term = serviceBackBean.getTerm();

		model.addAttribute("term", term);

		serviceNameIdMap = serviceRegistry.getServiceNameIdMap();

		model.addAttribute("serviceNameIdMap", serviceNameIdMap);

		List<SearchResultBackBean> searchResultList = backBeanFormManager
				.getsearchResultBackBeanList(serviceId, term);

		if (!searchResultList.isEmpty()) {

			searchResultBackBeanForm.setSearchResultList(searchResultList);
			model.addAttribute("SearchResultBackBeanForm",
					searchResultBackBeanForm);
			model.addAttribute("searchResultList", searchResultList);
			model.addAttribute("success", 1);
		}

		return "auth/home/profile";
	}

}
