package edu.asu.conceptpower.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import edu.asu.conceptpower.web.ConceptSearchBean;

@Component
public class ConceptSearchValidator implements Validator {

    @Override
    public boolean supports(Class<?> arg0) {
        return arg0.isAssignableFrom(ConceptSearchBean.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "word", "name.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "pos", "pos.required");

    }

}
