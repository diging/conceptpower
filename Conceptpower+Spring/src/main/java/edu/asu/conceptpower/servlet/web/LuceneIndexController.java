package edu.asu.conceptpower.servlet.web;

import java.security.Principal;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.asu.conceptpower.servlet.core.IIndexService;
import edu.asu.conceptpower.servlet.core.IndexingEvent;
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

    private AtomicBoolean indexerRunningFlag = new AtomicBoolean(false);

    /**
     * Retrives latest indexing time and number of concepts indexed so far
     * 
     * @param model
     * @return
     */
    @RequestMapping(value = "auth/luceneIndex", method = RequestMethod.GET)
    public String showLuceneIndex(ModelMap model) {
        IndexingEvent bean = dao.getTotalNumberOfWordsIndexed();
        model.addAttribute("bean", bean);
        return "/auth/luceneIndex";
    }

    /**
     * This method deletes all the index in Lucene and recreates all the index
     * from Wordnet and from CCP db40 database
     * 
     * @param req
     * @param principal
     * @param model
     * @return
     */
    @RequestMapping(value = "auth/indexLuceneWordNet", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody IndexingEvent indexConcepts(HttpServletRequest req, Principal principal, ModelMap model) {
        IndexingEvent bean = null;
        if (!indexerRunningFlag.get()) {
            indexerRunningFlag.set(true);
            try {
                manager.deleteIndexes();
                manager.indexConcepts();
                bean = dao.getTotalNumberOfWordsIndexed();
            } catch (LuceneException | IllegalArgumentException | IllegalAccessException ex) {
                bean = new IndexingEvent();
                bean.setMessage(ex.getMessage());
                return bean;
            }
            bean.setMessage("Indexed successfully");
            indexerRunningFlag.compareAndSet(true, false);
            return bean;
        }
        bean = new IndexingEvent();
        bean.setMessage("Indexer Already Running");
        return bean;
    }

    /**
     * This method deletes all the concepts in the lucene
     * 
     * @param req
     * @param principal
     * @param model
     * @return
     */
    @RequestMapping(value = "auth/deleteConcepts", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody IndexingEvent deleteConcepts(HttpServletRequest req, Principal principal, ModelMap model) {
        IndexingEvent bean = new IndexingEvent();
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
