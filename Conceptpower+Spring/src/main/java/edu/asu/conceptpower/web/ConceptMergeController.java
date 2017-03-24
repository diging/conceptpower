package edu.asu.conceptpower.web;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import edu.asu.conceptpower.app.bean.ConceptsMergeBean;
import edu.asu.conceptpower.app.core.IConceptListManager;
import edu.asu.conceptpower.app.core.IConceptManager;
import edu.asu.conceptpower.app.core.IConceptTypeManger;
import edu.asu.conceptpower.app.core.IConceptTypesService;
import edu.asu.conceptpower.app.core.IConceptTypesService.IdType;
import edu.asu.conceptpower.app.core.POS;
import edu.asu.conceptpower.app.exceptions.DictionaryDoesNotExistException;
import edu.asu.conceptpower.app.exceptions.DictionaryModifyException;
import edu.asu.conceptpower.app.exceptions.IndexerRunningException;
import edu.asu.conceptpower.app.exceptions.LuceneException;
import edu.asu.conceptpower.app.service.IConceptMergeService;
import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptList;

@Controller
public class ConceptMergeController {

    @Autowired
    private IConceptManager conceptManager;

    @Autowired
    private IConceptMergeService conceptMergeService;

    @Autowired
    private IConceptTypeManger conceptTypesManager;

    @Autowired
    private IConceptListManager conceptListManager;

    @Autowired
    private ConceptsMergeBeanValidator validator;

    @Autowired
    private IConceptTypesService conceptTypesService;

    @InitBinder
    private void initBinder(WebDataBinder binder) {
        binder.setValidator(validator);
    }

    @RequestMapping(value = "auth/concepts/merge", method = RequestMethod.GET)
    public ModelAndView prepareMergeConcept(@ModelAttribute("conceptsMergeBean") ConceptsMergeBean conceptsMergeBean,
            BindingResult result) {
        List<ConceptEntry> conceptEntries = new ArrayList<>();
        for (String id : conceptsMergeBean.getMergeIds()) {
            conceptEntries.add(conceptManager.getConceptEntry(id));
        }
        conceptsMergeBean = conceptMergeService.prepareMergeConcepts(conceptEntries, conceptsMergeBean);

        Set<String> localConceptIds = conceptsMergeBean.getMergeIds().stream()
                .filter(conceptId -> IdType.LOCAL_CONCEPT_ID == conceptTypesService
                        .getConceptTypeByConceptId(conceptId))
                .collect(Collectors.toSet());

        ModelAndView mav = new ModelAndView();
        mav.addObject("localConceptIds", localConceptIds);
        mav.addObject("types", conceptTypesManager.getAllTypes());
        mav.addObject("conceptEntries", conceptEntries);
        mav.addObject("conceptListValues", conceptListManager.getAllConceptLists().stream()
                .map(ConceptList::getConceptListName).collect(Collectors.toSet()));
        mav.addObject("posValues", POS.posValues);
        mav.setViewName("/auth/conceptMerge");
        return mav;
    }

    @RequestMapping(value = "auth/concepts/merge", method = RequestMethod.POST)
    public ModelAndView mergeConcept(
            @ModelAttribute("conceptsMergeBean") @Validated ConceptsMergeBean conceptsMergeBean, BindingResult result,
            Principal principal) throws IllegalAccessException, LuceneException, IndexerRunningException,
                    DictionaryDoesNotExistException, DictionaryModifyException {
        ModelAndView mav = new ModelAndView();
        if (result.hasErrors()) {
            // Adding all the default value.
            Set<String> localConceptIds = conceptsMergeBean.getMergeIds().stream().filter(
                    conceptId -> IdType.LOCAL_CONCEPT_ID == conceptTypesService.getConceptTypeByConceptId(conceptId))
                    .collect(Collectors.toSet());
            mav.addObject("localConceptIds", localConceptIds);
            mav.addObject("types", conceptTypesManager.getAllTypes());
            mav.addObject("conceptListValues", conceptListManager.getAllConceptLists().stream()
                    .map(ConceptList::getConceptListName).collect(Collectors.toSet()));
            mav.addObject("posValues", POS.posValues);
            mav.setViewName("/auth/conceptMerge");
            return mav;
        }
        conceptMergeService.mergeConcepts(conceptsMergeBean, principal.getName());
        mav.setViewName("redirect:/");
        return mav;
    }

}
