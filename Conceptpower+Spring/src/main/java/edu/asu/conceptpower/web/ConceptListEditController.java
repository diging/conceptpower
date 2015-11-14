package edu.asu.conceptpower.web;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptList;
import edu.asu.conceptpower.core.IConceptListManager;
import edu.asu.conceptpower.core.IConceptManager;
import edu.asu.conceptpower.users.IUserManager;

/**
 * This class provides all the methods required for editing a concept list
 * 
 * @author Chetan
 * 
 */
@Controller
public class ConceptListEditController {

	@Autowired
	private IConceptManager conceptManager;

	@Autowired
	private IUserManager usersManager;
	
	@Autowired
	private IConceptListManager conceptListManager;

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
			ModelMap model,@ModelAttribute("conceptListAddForm") ConceptListAddForm conceptListAddForm) {

		ConceptList list = conceptListManager.getConceptList(listName);
		conceptListAddForm.setListName(list.getConceptListName());
		conceptListAddForm.setDescription(list.getDescription());
		conceptListAddForm.setOldListName(list.getConceptListName());

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
			HttpServletRequest req,@ModelAttribute("conceptListAddForm") ConceptListAddForm conceptListAddForm) {

		ConceptList list = conceptListManager.getConceptList(listName);
		list.setConceptListName(req.getParameter("newlistname"));
		list.setDescription(req.getParameter("description"));

		conceptListManager.storeModifiedConceptList(list, listName);

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
