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
import edu.asu.conceptpower.core.IConceptTypeManger;
import edu.asu.conceptpower.users.IUserManager;

/**
 * This class provides all the methods for editing a type
 * 
 * @author Chetan
 * 
 */
@Controller
public class ConceptTypeEditController {

	@Autowired
	private IConceptTypeManger typeManager;

	@Autowired
	private IUserManager usersManager;

	/**
	 * This method provides a type information to edit type page
	 * 
	 * @param typeid
	 *            Represents a type which has to be edited
	 * @param model
	 *            Generic model holder for servlet
	 * @return Returns a string value to redirect user to type edit page
	 */
	@RequestMapping(value = "auth/concepttype/edittype/{typeid}", method = RequestMethod.GET)
	public String prepareEditType(@PathVariable("typeid") String typeid,
			ModelMap model) {

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

	/**
	 * This method stores the updated information of a type
	 * 
	 * @param typeid
	 * @param req
	 *            holds HTTP request information
	 * @param principal
	 *            holds logged in user information
	 * @return Returns a string value to redirect user to type list page
	 */
	@RequestMapping(value = "auth/concepttype/storeedittype/{typeid}", method = RequestMethod.POST)
	public String editType(@PathVariable("typeid") String typeid,
			HttpServletRequest req, Principal principal) {

		ConceptType type = typeManager.getType(typeid);
		type.setTypeName(req.getParameter("name"));
		type.setDescription(req.getParameter("description"));
		type.setMatches(req.getParameter("match"));
		type.setSupertypeId(req.getParameter("supertypes"));

		String userId = usersManager.findUser(principal.getName()).getUsername();
		String modified = type.getModified() != null ? type.getModified() : "";
		if (!modified.trim().isEmpty())
			modified += ", ";
		type.setModified(modified + userId + "@" + (new Date()).toString());

		typeManager.storeModifiedConceptType(type);
		return "redirect:/auth/concepttype";
	}

	/**
	 * This method returns the string to redirect user to type list page when
	 * user cancels type edit operation
	 * 
	 * @return Returns a string value to redirect user to concept type list page
	 */
	@RequestMapping(value = "auth/concepttype/edittype/canceledit", method = RequestMethod.GET)
	public String cancelEdit() {
		return "redirect:/auth/concepttype";
	}
}
