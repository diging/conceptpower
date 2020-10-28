package edu.asu.conceptpower.app.validation;

import org.springframework.stereotype.Component;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import edu.asu.conceptpower.app.model.ConceptEntry;
import edu.asu.conceptpower.app.profile.impl.ServiceBackBean;
import edu.asu.conceptpower.web.backing.ConceptAddBean;

@Component
public class ConceptAddValidator implements Validator {

    @Override
    public boolean supports(Class<?> arg0) {
        return arg0.isAssignableFrom(ConceptAddBean.class) || arg0.isAssignableFrom(ServiceBackBean.class)
                || arg0.isAssignableFrom(ConceptEntry.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "concept_name.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "pos", "pos.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "selectedList", "list.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "selectedTypes", "types.required");
    }

}
