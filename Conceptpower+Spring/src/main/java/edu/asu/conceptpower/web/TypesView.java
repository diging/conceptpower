package edu.asu.conceptpower.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.asu.conceptpower.core.ConceptType;
import edu.asu.conceptpower.core.ConceptTypesManager;

@Controller
public class TypesView {

	@Autowired
	private ConceptTypesManager conceptTypesManager;
	private ConceptType[] types;

	@RequestMapping(value = "auth/concepts/ConceptTypes")
	public String showConceptTypes(HttpServletRequest req, ModelMap model) {

		types = conceptTypesManager.getAllTypes();

		// to show super type name instead of type id in type list view
		for (ConceptType type : types) {
			if (type.getSupertypeId() != null) {
				ConceptType supertype = conceptTypesManager.getType(type
						.getSupertypeId());
				type.setSupertypeId(supertype.getTypeName());
			}
		}
		model.addAttribute("result", types);

		return "/auth/concepts/ConceptTypes";
	}
}
