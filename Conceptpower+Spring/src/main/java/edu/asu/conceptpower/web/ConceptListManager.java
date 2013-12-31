package edu.asu.conceptpower.web;

import java.util.List;

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
import edu.asu.conceptpower.wrapper.ConceptEntryWrapper;
import edu.asu.conceptpower.wrapper.ConceptEntryWrapperCreator;

@Controller
public class ConceptListManager {

	@Autowired
	private ConceptManager conceptManager;
	private List<ConceptList> conceptLists;

	@Autowired
	ConceptEntryWrapperCreator wrapperCreator;

	@RequestMapping(value = "auth/conceptlist")
	public String showConceptList(HttpServletRequest req, ModelMap model) {

		conceptLists = conceptManager.getAllConceptLists();
		model.addAttribute("result", conceptLists);

		return "/auth/conceptlist";
	}

	@RequestMapping(value = "auth/{listid}/concepts", method = RequestMethod.GET)
	public String conceptsList(@PathVariable("listid") String list,
			HttpServletRequest req, ModelMap model) {

		List<ConceptEntry> founds = conceptManager.getConceptListEntries(list);

		List<ConceptEntryWrapper> foundConcepts = wrapperCreator
				.createWrappers(founds != null ? founds
						.toArray(new ConceptEntry[founds.size()])
						: new ConceptEntry[0]);

		model.addAttribute("result", foundConcepts);
		return "/auth/conceptlist/concepts";
	}

	@RequestMapping(method = RequestMethod.GET, value = "conceptDetail")
	public @ResponseBody
	ConceptEntry getConceptDetails(ModelMap model,
			@RequestParam("conceptid") String conceptid) {
		ConceptEntry conceptEntry = conceptManager.getConceptEntry(conceptid);
		return conceptEntry;
	}

}
