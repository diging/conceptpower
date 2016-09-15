package edu.asu.conceptpower.servlet.validation;

import org.springframework.stereotype.Component;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import edu.asu.conceptpower.servlet.profile.impl.ServiceBackBean;
import edu.asu.conceptpower.servlet.web.backing.ConceptAddBean;

@Component
public class ConceptAddValidator implements Validator {

    @Override
    public boolean supports(Class<?> arg0) {
        return arg0.isAssignableFrom(ConceptAddBean.class) || arg0.isAssignableFrom(ServiceBackBean.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "concept_name.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "pos", "pos.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "selectedList", "list.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "selectedTypes", "types.required");
    }

}
