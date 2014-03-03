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
import edu.asu.conceptpower.users.impl.UsersManager;

@Controller
public class ConceptListDeleteController {

	@Autowired
	private UsersManager usersManager;

	@Autowired
	private ConceptManager conceptManager;

	@RequestMapping(value = "auth/conceptlist/deletelist/{name}", method = RequestMethod.GET)
	public String prepareDeletelist(@PathVariable("name") String name,
			HttpServletRequest req, ModelMap model) {
		ConceptList list = conceptManager.getConceptList(name);
		model.addAttribute("listName", list.getConceptListName());
		model.addAttribute("description", list.getDescription());

		// condition to check enable whether to delete the conceptlist
		boolean enableDelete = true;
		List<ConceptEntry> conceptEntries = conceptManager
				.getConceptListEntries(name);

		if (conceptEntries.size() > 0)
			enableDelete = false;

		model.addAttribute("enabledelete", enableDelete);

		return "/auth/conceptlist/deletelist";
	}

	@RequestMapping(value = "auth/conceptlist/deleteconceptlistconfirm/{listname}", method = RequestMethod.GET)
	public String deleteType(@PathVariable("listname") String listName,
			HttpServletRequest req, ModelMap model) {

		conceptManager.deleteConceptList(listName);

		return "redirect:/auth/conceptlist";
	}

	@RequestMapping(value = "auth/conceptlist/canceldelete/", method = RequestMethod.GET)
	public String cancelDelete(HttpServletRequest req, ModelMap model) {
		return "redirect:/auth/conceptlist";
	}

}
