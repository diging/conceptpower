package edu.asu.conceptpower.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.conceptpower.app.core.IConceptManager;
import edu.asu.conceptpower.app.core.IConceptTypeManager;
import edu.asu.conceptpower.app.exceptions.LuceneException;
import edu.asu.conceptpower.app.model.ConceptEntry;
import edu.asu.conceptpower.app.model.ConceptType;

/**
 * This class provides methods for concept type deletion
 * 
 * @author Chetan
 * 
 */
@Controller
public class ConcepTypeDeleteController {

    @Autowired
    private IConceptTypeManager typeManager;

    @Autowired
    private IConceptManager conceptManager;

    /**
     * This method provides information of a type to be deleted to concept type
     * deletion page
     * 
     * @param typeid
     *            ID of a type to be deleted
     * @param model
     *            A generic model holder for Servlet
     * @return String value to redirect user to concept type delete page
     */
    @RequestMapping(value = "auth/concepttype/deletetype/{typeid}", method = RequestMethod.GET)
    public String prepareDeleteType(@PathVariable("typeid") String typeid, ModelMap model)
            throws LuceneException {

        ConceptType type = typeManager.getType(typeid);
        model.addAttribute("typeid", type.getTypeId());
        model.addAttribute("typeName", type.getTypeName());
        model.addAttribute("description", type.getDescription());
        model.addAttribute("matches", type.getMatches());
        model.addAttribute("supertype", type.getSupertypeId());
        // condition to check enable whether to delete the concepttype
        boolean enableDelete = true;
        List<ConceptEntry> conceptEntries = conceptManager.getConceptEntryByTypeId(typeid);
        if (conceptEntries.size() > 0) {
            enableDelete = false;
        }
        model.addAttribute("enabledelete", enableDelete);

        return "/layouts/modals/deletetype";
    }

    /**
     * This method deletes a specified concept type by ID
     * 
     * @param typeid
     *            ID of a type to be deleted
     * @return String value which redirects user to cocept type list page
     */
    @RequestMapping(value = "auth/concepttype/deleteconcepttypeconfirm/{typeid}", method = RequestMethod.GET)
    public String deleteType(@PathVariable("typeid") String typeid) {

        typeManager.deleteType(typeid);

        return "redirect:/auth/concepttype";
    }

    /**
     * This method provides a string value to redirect user to concept type list
     * page when the user cancels the concept type delete operation
     * 
     * @return string value to redirect user to concept type list
     */
    @RequestMapping(value = "auth/concepttype/canceldelete/", method = RequestMethod.GET)
    public String cancelDelete() {
        return "redirect:/auth/concepttype";
    }

}
