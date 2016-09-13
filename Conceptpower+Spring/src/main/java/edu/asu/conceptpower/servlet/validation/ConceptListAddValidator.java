package edu.asu.conceptpower.servlet.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import edu.asu.conceptpower.servlet.core.IConceptListManager;
import edu.asu.conceptpower.servlet.web.ConceptListAddForm;
import edu.asu.conceptpower.servlet.wordnet.Constants;

@Component
public class ConceptListAddValidator implements Validator {

    @Autowired
    private IConceptListManager conceptListManager;

    @Override
    public boolean supports(Class<?> arg0) {
        return arg0.isAssignableFrom(ConceptListAddForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "listName", "concept_name.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "concept_description.required");

        if (!errors.hasErrors()) {
            ConceptListAddForm conceptListAddForm = (ConceptListAddForm) target;
            if (conceptListAddForm.getListName().equals(Constants.WORDNET_DICTIONARY)) {
                errors.rejectValue("listName", "concept_list_name.wordnet");
            }

            if (!conceptListAddForm.getListName().equalsIgnoreCase(conceptListAddForm.getOldListName())) {

                if (conceptListManager.checkExistingConceptList(conceptListAddForm.getListName())) {
                    errors.rejectValue("listName", "concept_unique.required");
                }
            }
        }

    }

}
