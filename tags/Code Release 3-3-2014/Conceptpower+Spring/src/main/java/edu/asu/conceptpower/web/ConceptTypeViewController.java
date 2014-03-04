package edu.asu.conceptpower.web;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.conceptpower.core.ConceptType;
import edu.asu.conceptpower.core.ConceptTypesManager;

@Controller
public class ConceptTypeViewController {

	private ConceptType type;

	@Autowired
	private ConceptTypesManager conceptTypesManager;

	@Autowired
	private LoginController loginController;

	@RequestMapping(value = "auth/concepttype/addtype")
	public String typeAddView(HttpServletRequest req, ModelMap model) {
		ConceptType[] allTypes = conceptTypesManager.getAllTypes();

		Map<String, String> types = new LinkedHashMap<String, String>();
		for (ConceptType conceptType : allTypes) {
			types.put(conceptType.getTypeId(), conceptType.getTypeName());
		}

		model.addAttribute("types", types);
		return "/auth/concepttype/addtype";
	}

	@RequestMapping(value = "auth/concepts/createconcepttype", method = RequestMethod.POST)
	public String createConceptType(HttpServletRequest req, ModelMap model,
			Principal principal) {

		type = new ConceptType();
		type.setTypeName(req.getParameter("name"));
		type.setDescription(req.getParameter("description"));
		type.setMatches(req.getParameter("match"));
		type.setSupertypeId(req.getParameter("types"));
		type.setCreatorId(principal.getName());

		conceptTypesManager.addConceptType(type);
		return "redirect:/auth/concepttype";
	}
}
