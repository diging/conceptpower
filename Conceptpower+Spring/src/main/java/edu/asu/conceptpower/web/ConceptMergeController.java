package edu.asu.conceptpower.web;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.conceptpower.app.bean.ConceptsMergeBean;
import edu.asu.conceptpower.app.core.IConceptManager;
import edu.asu.conceptpower.app.exceptions.DictionaryDoesNotExistException;
import edu.asu.conceptpower.app.exceptions.DictionaryModifyException;
import edu.asu.conceptpower.app.exceptions.IndexerRunningException;
import edu.asu.conceptpower.app.exceptions.LuceneException;
import edu.asu.conceptpower.app.service.IConceptMergeService;
import edu.asu.conceptpower.core.ConceptEntry;

@Controller
public class ConceptMergeController {

    @Autowired
    private IConceptManager conceptManager;

    @Autowired
    private IConceptMergeService conceptMergeService;

    @RequestMapping(value = "auth/concepts/merge/prepare")
    public String prepareMergeConcept(ModelMap model,
            @ModelAttribute("conceptsMergeBean") ConceptsMergeBean conceptsMergeBean, BindingResult result) {
        List<ConceptEntry> conceptEntries = new ArrayList<>();
        for (String id : conceptsMergeBean.getConceptIds()) {
            conceptEntries.add(conceptManager.getConceptEntry(id));
        }
        conceptsMergeBean = conceptMergeService.prepareMergeConcepts(conceptEntries, conceptsMergeBean);

        return "/auth/conceptMerge";
    }

    @RequestMapping(value = "auth/concepts/merge", method = RequestMethod.POST)
    public String mergeConcept(ModelMap model, @ModelAttribute("conceptsMergeBean") ConceptsMergeBean conceptsMergeBean,
            BindingResult result, Principal principal) throws IllegalAccessException, LuceneException,
            IndexerRunningException, DictionaryDoesNotExistException, DictionaryModifyException {
        conceptMergeService.mergeConcepts(conceptsMergeBean, principal.getName());
        return "redirect:/login";
    }

}
