package edu.asu.conceptpower.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.asu.conceptpower.core.ConceptType;
import edu.asu.conceptpower.core.ConceptTypesManager;

/**
 * This class provides methods for viewing concepts of a particular type
 * 
 * @author Chetan
 * 
 */
@Controller
public class ConceptTypesController {

	@Autowired
	private ConceptTypesManager conceptTypesManager;

	/**
	 * This method provides information about all the types for concept type
	 * view page
	 * 
	 * @param model
	 *            A generic model holder for Servlet
	 * @return String value to redirect user to concept type list page
	 */
	@RequestMapping(value = "auth/concepttype")
	public String prepateShowConceptTypes(ModelMap model) {

		ConceptType[] types = conceptTypesManager.getAllTypes();

		// to show super type name instead of type id in type list view
		for (ConceptType type : types) {
			if (type.getSupertypeId() != null
					&& !type.getSupertypeId().equals("")) {
				ConceptType supertype = conceptTypesManager.getType(type
						.getSupertypeId());
				if (supertype != null)
					type.setSupertypeId(supertype.getTypeName());
			}
		}
		model.addAttribute("result", types);

		return "/auth/concepttype";
	}
}
