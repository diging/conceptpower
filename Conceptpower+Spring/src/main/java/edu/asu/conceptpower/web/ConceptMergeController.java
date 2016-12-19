package edu.asu.conceptpower.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.asu.conceptpower.app.bean.ConceptsMergeBean;
import edu.asu.conceptpower.app.core.IConceptManager;
import edu.asu.conceptpower.app.service.IConceptMergeService;
import edu.asu.conceptpower.core.ConceptEntry;

@Controller
public class ConceptMergeController {

    @Autowired
    private IConceptManager conceptManager;

    @Autowired
    private IConceptMergeService conceptMergeService;

    @RequestMapping(value = "prepareMergeConcept")
    public String mergeConcept(ModelMap model, @ModelAttribute("mergeConceptsBean") ConceptsMergeBean mergeConceptsBean,
            BindingResult result) {
        System.out.println(mergeConceptsBean.getConceptIds());
        List<ConceptEntry> conceptEntries = new ArrayList<>();
        for (String id : mergeConceptsBean.getConceptIds()) {
            conceptEntries.add(conceptManager.getConceptEntry(id));
        }
        model.addAttribute("mergedConcepts", conceptMergeService.mergeConcepts(conceptEntries, mergeConceptsBean));

        return "/auth/conceptMerge";
    }
}
