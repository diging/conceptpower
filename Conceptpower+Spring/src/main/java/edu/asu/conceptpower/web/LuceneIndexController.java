package edu.asu.conceptpower.web;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.conceptpower.lucene.ILuceneIndexManger;

@Controller
public class LuceneIndexController {

    @Autowired
    private ILuceneIndexManger manager;

    @RequestMapping(value = "auth/luceneIndex", method = RequestMethod.GET)
    public String showLuceneIndex(ModelMap model) {
        return "/auth/luceneIndex";
    }

    @RequestMapping(value = "auth/indexLuceneWordNet", method = RequestMethod.POST)
    public String addConcept(HttpServletRequest req, Principal principal, ModelMap model) {

        manager.deleteWordNetLuceneDocuments();

        if (manager.indexLuceneDocuments()) {
            model.addAttribute("message", "Indexing Completed Sucessfully");
        } else {
            model.addAttribute("message", "Issues in Indexing. Please try again later");
        }

        return "/auth/luceneIndex";
    }
    
    @RequestMapping(value = "auth/indexLuceneWordNet", method = RequestMethod.POST)
    public String deleteUserDefinedConcepts(HttpServletRequest req, Principal principal, ModelMap model) {
        
        manager.deleteUserDefinedConcepts();
        
        return "/auth/luceneIndex";
    }
}
