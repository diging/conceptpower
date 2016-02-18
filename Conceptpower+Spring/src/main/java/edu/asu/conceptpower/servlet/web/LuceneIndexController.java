package edu.asu.conceptpower.servlet.web;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.asu.conceptpower.servlet.core.IIndexService;
import edu.asu.conceptpower.servlet.exceptions.LuceneException;

/**
 * This class provides methods for deleting and viewing lucene indexes
 * 
 * @author mkarthik90
 *
 */
@Controller
public class LuceneIndexController {

    @Autowired
    private IIndexService manager;

    @RequestMapping(value = "auth/luceneIndex", method = RequestMethod.GET)
    public String showLuceneIndex(ModelMap model) {
        return "/auth/luceneIndex";
    }

    @RequestMapping(value = "auth/indexLuceneWordNet", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<String> indexConcepts(HttpServletRequest req, Principal principal, ModelMap model) {
        try {
            manager.deleteIndexes();
            manager.indexConcepts();
        } catch (LuceneException | IllegalArgumentException | IllegalAccessException ex) {
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.OK);
        }
        return new ResponseEntity<String>("Indexed Successfully", HttpStatus.OK);
    }

    @RequestMapping(value = "auth/deleteConcepts", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<String> deleteConcepts(HttpServletRequest req, Principal principal,
            ModelMap model) {

        try {
            manager.deleteIndexes();
        } catch (LuceneException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<String>("Deleted index successfully", HttpStatus.OK);
    }
}
