package edu.asu.conceptpower.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptList;
import edu.asu.conceptpower.core.ConceptManager;
import edu.asu.conceptpower.core.ConceptType;
import edu.asu.conceptpower.core.ConceptTypesManager;
import edu.asu.conceptpower.users.impl.UsersManager;

/**
 * This class provides methods for concept type deletion
 * 
 * @author Chetan
 * 
 */
@Controller
public class ConcepTypeDeleteController {

	@Autowired
	ConceptTypesManager typeManager;

	@Autowired
	private UsersManager usersManager;

	@Autowired
	private ConceptManager conceptManager;

	/**
	 * This method provides information of a type to be deleted to concept type
	 * deletion page
	 * 
	 * @param typeid
	 *            ID of a type to be deleted
	 * @param model
	 *            A generic model holder for Servlet
	 * @return String value to redirect user to concept type delete page
	 */
	@RequestMapping(value = "auth/concepttype/deletetype/{typeid}", method = RequestMethod.GET)
	public String prepareDeleteType(@PathVariable("typeid") String typeid,
			ModelMap model) {

		ConceptType type = typeManager.getType(typeid);
		model.addAttribute("typeid", type.getTypeId());
		model.addAttribute("typeName", type.getTypeName());
		model.addAttribute("description", type.getDescription());
		model.addAttribute("matches", type.getMatches());
		model.addAttribute("supertype", type.getSupertypeId());

		// condition to check enable whether to delete the concepttype
		boolean enableDelete = true;
		List<ConceptList> conceptLists = conceptManager.getAllConceptLists();
		for (ConceptList conceptList : conceptLists) {
			List<ConceptEntry> conceptEntries = conceptManager
					.getConceptListEntries(conceptList.getConceptListName());
			for (ConceptEntry conceptEntry : conceptEntries) {
				if ((conceptEntry.getTypeId()).equals(typeid)) {
					enableDelete = false;
				}
			}
		}

		model.addAttribute("enabledelete", enableDelete);

		return "/auth/concepttype/deletetype";
	}

	/**
	 * This method deletes a specified concept type by ID
	 * 
	 * @param typeid
	 *            ID of a type to be deleted
	 * @return String value which redirects user to cocept type list page
	 */
	@RequestMapping(value = "auth/concepttype/deleteconcepttypeconfirm/{typeid}", method = RequestMethod.GET)
	public String deleteType(@PathVariable("typeid") String typeid) {

		typeManager.deleteType(typeid);

		return "redirect:/auth/concepttype";
	}

	/**
	 * This method provides a string value to redirect user to concept type list
	 * page when the user cancels the concept type delete operation
	 * 
	 * @return string value to redirect user to concept type list
	 */
	@RequestMapping(value = "auth/concepttype/canceldelete/", method = RequestMethod.GET)
	public String cancelDelete() {
		return "redirect:/auth/concepttype";
	}

}
