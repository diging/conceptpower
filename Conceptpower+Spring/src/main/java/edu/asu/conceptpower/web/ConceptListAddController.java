package edu.asu.conceptpower.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.asu.conceptpower.core.ConceptManager;
import edu.asu.conceptpower.exceptions.DictionaryExistsException;

@Controller
public class ConceptListAddController {

	@Autowired
	private ConceptManager conceptManager;

	/**
	 * This method provides a string value to redirect user to add concept list
	 * page
	 * 
	 * @return string value to redirect user to add concept list page
	 */
	@RequestMapping(value = "auth/conceptlist/addconceptlist")
	public String listAddView() {
		return "/auth/conceptlist/addconceptlist";
	}

	/**
	 * This method creates and stores a new concept list
	 * 
	 * @param req
	 *            Holds HTTP request information
	 * @param model
	 *            A generic model holder for Servlet
	 * @return String value to redirect user to a concept list page
	 */
	@RequestMapping(value = "auth/concepts/createconceptlist")
	public String createConceptList(HttpServletRequest req, ModelMap model) {
		String name = req.getParameter("name");
		String description = req.getParameter("description");

		try {
			conceptManager.addConceptList(name, description);
		} catch (DictionaryExistsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "redirect:/auth/conceptlist";
	}

}
