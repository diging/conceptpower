package edu.asu.conceptpower.app.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import edu.asu.conceptpower.web.backing.ConceptWrapperAddBean;

@Component
public class ConceptWrapperAddBeanValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return ConceptWrapperAddBean.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "word", "name.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "selectedConceptList", "list.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "selectedType", "types.required");
    }

}
