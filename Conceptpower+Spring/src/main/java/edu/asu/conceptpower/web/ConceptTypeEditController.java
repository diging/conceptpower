package edu.asu.conceptpower.web;

import java.security.Principal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.conceptpower.core.ConceptType;
import edu.asu.conceptpower.core.ConceptTypesManager;
import edu.asu.conceptpower.users.impl.UsersManager;

@Controller
public class ConceptTypeEditController {

	@Autowired
	ConceptTypesManager typeManager;

	@Autowired
	private UsersManager usersManager;

	@RequestMapping(value = "auth/concepttype/edittype/{typeid}", method = RequestMethod.GET)
	public String prepateEditType(@PathVariable("typeid") String typeid,
			HttpServletRequest req, ModelMap model) {

		ConceptType type = typeManager.getType(typeid);
		model.addAttribute("typeid", type.getTypeId());
		model.addAttribute("typeName", type.getTypeName());
		model.addAttribute("description", type.getDescription());
		model.addAttribute("matches", type.getMatches());

		ConceptType[] allTypes = typeManager.getAllTypes();

		Map<String, String> types = new LinkedHashMap<String, String>();
		for (ConceptType conceptType : allTypes) {
			types.put(conceptType.getTypeId(), conceptType.getTypeName());
		}

		model.addAttribute("selectedtypeid", typeid);
		model.addAttribute("selectedtypename", types.get(typeid));
		types.remove(typeid);
		model.addAttribute("supertypes", types);

		return "/auth/concepttype/edittype";
	}

	@RequestMapping(value = "auth/concepttype/storeedittype/{typeid}", method = RequestMethod.POST)
	public String editType(@PathVariable("typeid") String typeid,
			HttpServletRequest req, ModelMap model, Principal principal) {

		ConceptType type = typeManager.getType(typeid);
		type.setTypeName(req.getParameter("name"));
		type.setDescription(req.getParameter("description"));
		type.setMatches(req.getParameter("match"));
		type.setSupertypeId(req.getParameter("supertypes"));

		String userId = usersManager.findUser(principal.getName()).getUser();
		String modified = type.getModified() != null ? type.getModified() : "";
		if (!modified.trim().isEmpty())
			modified += ", ";
		type.setModified(modified + userId + "@" + (new Date()).toString());

		typeManager.storeModifiedConceptType(type);
		return "redirect:/auth/concepttype";
	}

	@RequestMapping(value = "auth/concepttype/edittype/canceledit", method = RequestMethod.GET)
	public String cancelDelete(HttpServletRequest req, ModelMap model) {
		return "redirect:/auth/concepttype";
	}
}
