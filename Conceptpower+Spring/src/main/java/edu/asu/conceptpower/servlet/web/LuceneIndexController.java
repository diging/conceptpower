package edu.asu.conceptpower.servlet.web;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.asu.conceptpower.servlet.core.IIndexService;
import edu.asu.conceptpower.servlet.core.LuceneBean;
import edu.asu.conceptpower.servlet.exceptions.LuceneException;
import edu.asu.conceptpower.servlet.lucene.ILuceneDAO;

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

    @Autowired
    @Qualifier("luceneDAO")
    private ILuceneDAO dao;

    @RequestMapping(value = "auth/luceneIndex", method = RequestMethod.GET)
    public String showLuceneIndex(ModelMap model) {
        LuceneBean bean = dao.getTotalNumberOfWordsIndexed();
        model.addAttribute("bean", bean);
        return "/auth/luceneIndex";
    }

    @RequestMapping(value = "auth/indexLuceneWordNet", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody LuceneBean indexConcepts(HttpServletRequest req, Principal principal, ModelMap model) {
        LuceneBean bean = new LuceneBean();
        try {
            manager.deleteIndexes();
            manager.indexConcepts();
            bean = dao.getTotalNumberOfWordsIndexed();
        } catch (LuceneException | IllegalArgumentException | IllegalAccessException ex) {
            bean.setMessage(ex.getMessage());
            return bean;
        }
        bean.setMessage("Indexed successfully");
        return bean;
    }

    @RequestMapping(value = "auth/deleteConcepts", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody LuceneBean deleteConcepts(HttpServletRequest req, Principal principal, ModelMap model) {
        LuceneBean bean = new LuceneBean();
        try {
            manager.deleteIndexes();
            bean = dao.getTotalNumberOfWordsIndexed();
        } catch (LuceneException e) {
            bean.setMessage(e.getMessage());
            return bean;
        }
        bean.setMessage("Concept deleted successfully");
        return bean;
    }
}
