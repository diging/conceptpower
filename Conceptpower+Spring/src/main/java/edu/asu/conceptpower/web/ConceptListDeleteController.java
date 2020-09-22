package edu.asu.conceptpower.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import edu.asu.conceptpower.app.core.IConceptListService;
import edu.asu.conceptpower.app.core.IConceptManager;
import edu.asu.conceptpower.app.db4o.IConceptDBManager;
import edu.asu.conceptpower.app.exceptions.LuceneException;
import edu.asu.conceptpower.app.model.ConceptList;
import edu.asu.conceptpower.app.users.IUserManager;
import edu.asu.conceptpower.core.ConceptEntry;

@Controller
public class ConceptListDeleteController {

    @Autowired
    private IUserManager usersManager;

    @Autowired
    private IConceptManager conceptManager;

    @Autowired
    private IConceptListService conceptListService;

    /**
     * This method provides information of a concept list to be deleted
     * 
     * @param name
     *            Concept list name to be deleted
     * @param model
     *            A generic model holder for Servlet
     * @return String to redirect user to delete concept list page
     */
    @GetMapping(value = "auth/conceptlist/deletelist/{name}")
    public String prepareDeleteConceptList(@PathVariable("name") String name, ModelMap model) throws LuceneException {
        ConceptList list = conceptListService.getConceptList(name);
        model.addAttribute("listName", list.getConceptListName());
        model.addAttribute("description", list.getDescription());

        // condition to check enable whether to delete the conceptlist
        boolean enableDelete = true;
        List<ConceptEntry> conceptEntries = null;
        conceptEntries = conceptManager.getConceptListEntries(name, 1, -1, "id", IConceptDBManager.DESCENDING);

        if (conceptEntries.size() > 0)
            enableDelete = false;

        model.addAttribute("enabledelete", enableDelete);

        return "/layouts/modals/deletelist";
    }

    /**
     * This method deletes a given concept list
     * 
     * @param listName
     *            Concept list name
     * @return String value to redirect user to concept list page
     */
    @GetMapping(value = "auth/conceptlist/deleteconceptlistconfirm/{listname}")
    public String deleteConceptList(@PathVariable("listname") String listName) {

        conceptListService.deleteConceptList(listName);

        return "redirect:/auth/conceptlist";
    }

    /**
     * This method redirects user to concept list page when user cancels concept
     * list delete operation
     * 
     * @return String value to redirect user to concept list page
     */
    @GetMapping(value = "auth/conceptlist/canceldelete/")
    public String cancelConceptListDelete() {
        return "redirect:/auth/conceptlist";
    }

}
