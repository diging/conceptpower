package edu.asu.conceptpower.web;

import java.security.Principal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.conceptpower.core.ConceptType;
import edu.asu.conceptpower.core.IConceptTypeManger;
import edu.asu.conceptpower.users.IUserManager;
import edu.asu.conceptpower.validation.ConceptTypeAddValidator;

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

	@Autowired
	private ConceptTypeAddValidator validator;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.setValidator(validator);
	}

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
	public String prepareEditType(@PathVariable("typeid") String typeid, ModelMap model,
			@ModelAttribute("conceptTypeAddForm") ConceptTypeAddForm conceptTypeAddForm) {
		
			ConceptType type = typeManager.getType(typeid);
			
			
			conceptTypeAddForm.setTypeName(type.getTypeName());
			conceptTypeAddForm.setTypeDescription(type.getDescription());
			conceptTypeAddForm.setMatches(type.getMatches());
			conceptTypeAddForm.setSelectedType(type.getSupertypeId().trim());
			model.addAttribute("typeid", type.getTypeId());

			ConceptType[] allTypes = typeManager.getAllTypes();

			Map<String, String> types = new LinkedHashMap<String, String>();
			for (ConceptType conceptType : allTypes) {
				types.put(conceptType.getTypeId(), conceptType.getTypeName());
			}

			types.remove(typeid);
			//model.addAttribute("supertypes", types);
			conceptTypeAddForm.setTypes(types);
			conceptTypeAddForm.setTypeid(typeid);
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
	@RequestMapping(value = "auth/concepttype/storeedittype", method = RequestMethod.POST)
	public String editType(HttpServletRequest req, Principal principal,
			@ModelAttribute("conceptTypeAddForm") ConceptTypeAddForm conceptTypeAddForm,BindingResult results) {
		
		if(results.hasErrors()){
			return "/auth/concepttype/edittype";
		}
		String typeid = conceptTypeAddForm.getTypeid();
		ConceptType type = typeManager.getType(typeid);
		type.setTypeName(conceptTypeAddForm.getTypeName());
		type.setDescription(conceptTypeAddForm.getTypeDescription());
		type.setMatches(conceptTypeAddForm.getMatches());
		type.setSupertypeId(conceptTypeAddForm.getSelectedType());

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
