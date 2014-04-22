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

/**
 * This class provides required methods for concept type creation
 * 
 * @author Chetan
 * 
 */
@Controller
public class ConceptTypeAddController {

	@Autowired
	private ConceptTypesManager conceptTypesManager;


	/**
	 * This method provides types information required for concept type creation
	 * 
	 * @param model
	 *            A generic model holder for Servlet
	 * @return String value to redirect use to concept type add page
	 */
	@RequestMapping(value = "auth/concepttype/addtype")
	public String prepareTypeAddView(ModelMap model) {
		ConceptType[] allTypes = conceptTypesManager.getAllTypes();

		Map<String, String> types = new LinkedHashMap<String, String>();
		for (ConceptType conceptType : allTypes) {
			types.put(conceptType.getTypeId(), conceptType.getTypeName());
		}

		model.addAttribute("types", types);
		return "/auth/concepttype/addtype";
	}

	/**
	 * This method stores the newly created concept type
	 * 
	 * @param req
	 *            Holds HTTP request information
	 * @param principal
	 *            Holds information of a logged in user
	 * @return String value to redirect user to concept type list page
	 */
	@RequestMapping(value = "auth/concepts/createconcepttype", method = RequestMethod.POST)
	public String createConceptType(HttpServletRequest req, Principal principal) {

		ConceptType type = new ConceptType();
		type.setTypeName(req.getParameter("name"));
		type.setDescription(req.getParameter("description"));
		type.setMatches(req.getParameter("match"));
		type.setSupertypeId(req.getParameter("types"));
		type.setCreatorId(principal.getName());

		conceptTypesManager.addConceptType(type);
		return "redirect:/auth/concepttype";
	}
}
