package edu.asu.conceptpower.web;

import java.security.Principal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.conceptpower.app.users.IUserManager;
import edu.asu.conceptpower.app.validation.ConceptTypeAddValidator;
import edu.asu.conceptpower.app.manager.IConceptTypeManager;
import edu.asu.conceptpower.app.model.ConceptType;

/**
 * This class provides all the methods for editing a type
 * 
 * @author Chetan
 * 
 */
@Controller
public class ConceptTypeEditController {

    @Autowired
    private IConceptTypeManager typeManager;

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
        
        if (type.getSupertypeId()==null) {
            conceptTypeAddForm.setSuperType("none");
        }
        conceptTypeAddForm.setTypeName(type.getTypeName());
        conceptTypeAddForm.setOldTypeName(type.getTypeName());
        conceptTypeAddForm.setTypeDescription(type.getDescription());
        conceptTypeAddForm.setMatches(type.getMatches());
        conceptTypeAddForm.setSuperType(type.getSupertypeId());
        conceptTypeAddForm.setTypeid(type.getTypeId());

        List<ConceptType> allTypes = typeManager.getAllTypes();

        Map<String, String> types = new LinkedHashMap<String, String>();
        for (ConceptType conceptType : allTypes) {
            types.put(conceptType.getTypeId(), conceptType.getTypeName());
        }

        types.remove(typeid);
        conceptTypeAddForm.setTypes(types);
        conceptTypeAddForm.setTypeid(typeid);
        return "/layouts/concepts/editconcepttype";
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
            @Validated @ModelAttribute("conceptTypeAddForm") ConceptTypeAddForm conceptTypeAddForm,
            BindingResult results) {

        if (results.hasErrors()) {
            return "/layouts/concepts/editconcepttype";
        }
        ConceptType type = typeManager.getType(conceptTypeAddForm.getTypeid());
        type.setTypeName(conceptTypeAddForm.getTypeName());
        type.setDescription(conceptTypeAddForm.getTypeDescription());
        type.setMatches(conceptTypeAddForm.getMatches());
        type.setSupertypeId(conceptTypeAddForm.getSuperType());

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
