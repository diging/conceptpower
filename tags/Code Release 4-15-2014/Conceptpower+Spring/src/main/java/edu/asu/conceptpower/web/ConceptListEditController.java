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

/**
 * This class provides all the methods required for editing a concept list
 * 
 * @author Chetan
 * 
 */
@Controller
public class ConceptListEditController {

	@Autowired
	ConceptManager conceptManager;

	@Autowired
	private UsersManager usersManager;

	/**
	 * This method provides list name and description of a list to be edited
	 * 
	 * @param listName
	 *            Represents list which has to be edited
	 * @param model
	 *            Generic model holder for servlet
	 * @return Returns a string value to redirect user to edit list page
	 */
	@RequestMapping(value = "auth/conceptlist/editlist/{listname}", method = RequestMethod.GET)
	public String prepareEditList(@PathVariable("listname") String listName,
			ModelMap model) {

		ConceptList list = conceptManager.getConceptList(listName);
		model.addAttribute("newlistname", list.getConceptListName());
		model.addAttribute("description", list.getDescription());

		return "/auth/conceptlist/editlist";
	}

	/**
	 * This method updates edited list information
	 * 
	 * @param listName
	 *            Represents list whose data has to be updated
	 * @param req
	 *            holds HTTP request information
	 * @return Returns a string value to redirect user to concept list page
	 */
	@RequestMapping(value = "auth/conceptlist/storeeditlist/{listname}", method = RequestMethod.POST)
	public String editList(@PathVariable("listname") String listName,
			HttpServletRequest req) {

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

	/**
	 * Returns a string value to redirect user to concept list page when the
	 * user cancels list edit operation
	 * 
	 * @return Returns a string value to redirect user to concept list page
	 */
	@RequestMapping(value = "auth/conceptlist/editlist/canceledit", method = RequestMethod.GET)
	public String cancelEdit() {
		return "redirect:/auth/conceptlist";
	}

}
