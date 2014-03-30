package edu.asu.conceptpower.web;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptList;
import edu.asu.conceptpower.core.ConceptManager;
import edu.asu.conceptpower.users.impl.UsersManager;

@Controller
public class ConceptListEditController {

	@Autowired
	ConceptManager conceptManager;

	@Autowired
	private UsersManager usersManager;

	@RequestMapping(value = "auth/conceptlist/editlist/{listname}", method = RequestMethod.GET)
	public String prepareEditList(@PathVariable("listname") String listName,
			HttpServletRequest req, ModelMap model) {

		ConceptList list = conceptManager.getConceptList(listName);
		model.addAttribute("newlistname", list.getConceptListName());
		model.addAttribute("description", list.getDescription());

		return "/auth/conceptlist/editlist";
	}

	@RequestMapping(value = "auth/conceptlist/storeeditlist/{listname}", method = RequestMethod.POST)
	public String editType(@PathVariable("listname") String listName,
			HttpServletRequest req, ModelMap model) {

		ConceptList list = conceptManager.getConceptList(listName);
		list.setConceptListName(req.getParameter("newlistname"));
		list.setDescription(req.getParameter("description"));

		conceptManager.storeModifiedConceptList(list, listName);

		// modify the name for all the existing concepts under this concept list
		List<ConceptEntry> entries = conceptManager
				.getConceptListEntries(listName);
		Iterator<ConceptEntry> entriesIterator = entries.iterator();

		while (entriesIterator.hasNext()) {
			ConceptEntry conceptEntry = (ConceptEntry) entriesIterator.next();
			conceptEntry.setConceptList(list.getConceptListName());
			conceptManager.storeModifiedConcept(conceptEntry);
		}

		return "redirect:/auth/conceptlist";
	}

	@RequestMapping(value = "auth/conceptlist/editlist/canceledit", method = RequestMethod.GET)
	public String cancelDelete(HttpServletRequest req, ModelMap model) {
		return "redirect:/auth/conceptlist";
	}

}
