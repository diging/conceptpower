package edu.asu.conceptpower.web;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import edu.asu.conceptpower.app.bean.ConceptsMergeBean;

@Component
public class ConceptsMergeBeanValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return ConceptsMergeBean.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "word", "word", "Word cannot be empty.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "selectedPosValue", "selectedPosValue",
                "POS cannot be empty.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "selectedListName", "selectedListName",
                "Concept List cannot be empty.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "selectedTypeId", "selectedTypeId",
                "Concept Type cannot be empty.");

        ConceptsMergeBean conceptMergeBean = (ConceptsMergeBean) target;
        if (conceptMergeBean.getMergeIds() == null || conceptMergeBean.getMergeIds().size() < 2) {
            errors.reject("mergeIds", "Merged Id should contain atleast two ids.");
        }
    }

}
