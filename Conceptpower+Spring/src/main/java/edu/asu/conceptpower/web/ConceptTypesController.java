package edu.asu.conceptpower.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.asu.conceptpower.app.manager.IConceptDBManager;
import edu.asu.conceptpower.app.manager.IConceptTypeManager;
import edu.asu.conceptpower.app.model.ConceptType;

/**
 * This class provides methods for viewing concepts of a particular type
 * 
 * @author Chetan
 * 
 */
@Controller
public class ConceptTypesController {

    @Autowired
    private IConceptTypeManager conceptTypesManager;

    /**
     * This method provides information about all the types for concept type
     * view page
     * 
     * @param model
     *            A generic model holder for Servlet
     * @return String value to redirect user to concept type list page
     * @throws SecurityException
     * @throws NoSuchFieldException
     */
    @RequestMapping(value = "auth/concepttype")
    public String prepareShowConceptTypes(ModelMap model, @RequestParam(defaultValue = "1") String page,
            @RequestParam(defaultValue = IConceptDBManager.ASCENDING + "") String sortDir,
            @RequestParam(defaultValue = "typeName") String sortColumn) throws NoSuchFieldException, SecurityException {

        List<ConceptType> types = conceptTypesManager.getAllTypes();

        int pageInt = new Integer(page);
        int sortDirInt = new Integer(sortDir);

        List<ConceptType> conceptTypes = conceptTypesManager.getConceptTypes(pageInt, -1, sortColumn, sortDirInt);

        // to show super type name instead of type id in type list view
        for (ConceptType type : types) {
            if (type.getSupertypeId() != null && !type.getSupertypeId().equals("")) {
                ConceptType supertype = conceptTypesManager.getType(type.getSupertypeId());
                if (supertype != null)
                    type.setSupertypeId(supertype.getTypeName());
            }
        }

        if (pageInt < 1) {
            pageInt = 1;
        }

        int pageCount = conceptTypesManager.getPageCount();
        if (pageInt > pageCount) {
            pageInt = pageCount;
        }

        model.addAttribute("page", pageInt);
        model.addAttribute("result", conceptTypes);
        model.addAttribute("count", pageCount);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("sortColumn", sortColumn);
        return "layouts/concepts/concepttypes";
    }
}
