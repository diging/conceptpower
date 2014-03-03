package edu.asu.conceptpower.web;

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
import edu.asu.conceptpower.core.ConceptType;
import edu.asu.conceptpower.core.ConceptTypesManager;
import edu.asu.conceptpower.users.impl.UsersManager;

@Controller
public class ConcepTypeDeleteController {

	@Autowired
	ConceptTypesManager typeManager;

	@Autowired
	private UsersManager usersManager;

	@Autowired
	private ConceptManager conceptManager;

	/**
	 * 
	 * @param typeid
	 * @param req
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "auth/concepttype/deletetype/{typeid}", method = RequestMethod.GET)
	public String prepareDeleteType(@PathVariable("typeid") String typeid,
			HttpServletRequest req, ModelMap model) {

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
	 * 
	 * @param typeid
	 * @param req
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "auth/concepttype/deleteconcepttypeconfirm/{typeid}", method = RequestMethod.GET)
	public String deleteType(@PathVariable("typeid") String typeid,
			HttpServletRequest req, ModelMap model) {

		typeManager.deleteType(typeid);

		return "redirect:/auth/concepttype";
	}

	/**
	 * 
	 * @param req
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "auth/concepttype/canceldelete/", method = RequestMethod.GET)
	public String cancelDelete(HttpServletRequest req, ModelMap model) {
		return "redirect:/auth/concepttype";
	}

}
