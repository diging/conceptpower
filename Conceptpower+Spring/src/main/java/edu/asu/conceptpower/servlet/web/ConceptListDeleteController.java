package edu.asu.conceptpower.servlet.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptList;
import edu.asu.conceptpower.servlet.core.IConceptListManager;
import edu.asu.conceptpower.servlet.core.IConceptManager;
import edu.asu.conceptpower.servlet.exceptions.LuceneException;
import edu.asu.conceptpower.servlet.users.IUserManager;

@Controller
public class ConceptListDeleteController {

    @Autowired
    private IUserManager usersManager;

    @Autowired
    private IConceptManager conceptManager;

    @Autowired
    private IConceptListManager conceptListManager;

    /**
     * This method provides information of a concept list to be deleted
     * 
     * @param name
     *            Concept list name to be deleted
     * @param model
     *            A generic model holder for Servlet
     * @return String to redirect user to delete concept list page
     */
    @RequestMapping(value = "auth/conceptlist/deletelist/{name}", method = RequestMethod.GET)
    public String prepareDeleteConceptList(@PathVariable("name") String name, ModelMap model) throws LuceneException {
        ConceptList list = conceptListManager.getConceptList(name);
        model.addAttribute("listName", list.getConceptListName());
        model.addAttribute("description", list.getDescription());

        // condition to check enable whether to delete the conceptlist
        boolean enableDelete = true;
        List<ConceptEntry> conceptEntries = null;
        conceptEntries = conceptManager.getConceptListEntries(name);

        if (conceptEntries.size() > 0)
            enableDelete = false;

        model.addAttribute("enabledelete", enableDelete);

        return "/auth/conceptlist/deletelist";
    }

    /**
     * This method deletes a given concept list
     * 
     * @param listName
     *            Concept list name
     * @return String value to redirect user to concept list page
     */
    @RequestMapping(value = "auth/conceptlist/deleteconceptlistconfirm/{listname}", method = RequestMethod.GET)
    public String deleteConceptList(@PathVariable("listname") String listName) {

        conceptListManager.deleteConceptList(listName);

        return "redirect:/auth/conceptlist";
    }

    /**
     * This method redirects user to concept list page when user cancels concept
     * list delete operation
     * 
     * @return String value to redirect user to concept list page
     */
    @RequestMapping(value = "auth/conceptlist/canceldelete/", method = RequestMethod.GET)
    public String cancelConceptListDelete() {
        return "redirect:/auth/conceptlist";
    }

}
