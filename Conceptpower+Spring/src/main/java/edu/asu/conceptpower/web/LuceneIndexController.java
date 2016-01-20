package edu.asu.conceptpower.web;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.conceptpower.exceptions.LuceneException;
import edu.asu.conceptpower.lucene.ILuceneIndexManger;

/**
 * This class provides methods for deleting and viewing lucene indexes
 * @author mkarthik90
 *
 */
@Controller
public class LuceneIndexController {

    @Autowired
    private ILuceneIndexManger manager;

    
    @RequestMapping(value = "auth/luceneIndex", method = RequestMethod.GET)
    public String showLuceneIndex(ModelMap model) {
        return "/auth/luceneIndex";
    }

    @RequestMapping(value = "auth/indexLuceneWordNet", method = RequestMethod.POST)
    public String indexConcepts(HttpServletRequest req, Principal principal, ModelMap model) {

        try {
            manager.deleteIndexes();
            manager.indexConcepts();
        } catch (LuceneException ex) {
            model.addAttribute("message", ex.getMessage());
        }
        return "/auth/luceneIndex";
    }
    
    @RequestMapping(value = "auth/deleteConcepts", method = RequestMethod.POST)
    public String deleteConcepts(HttpServletRequest req, Principal principal, ModelMap model) {
        
        try{
            manager.deleteIndexes();
        }
        catch(LuceneException ex){
            model.addAttribute("message",ex.getMessage());
        }
        return "/auth/luceneIndex";
    }
}
