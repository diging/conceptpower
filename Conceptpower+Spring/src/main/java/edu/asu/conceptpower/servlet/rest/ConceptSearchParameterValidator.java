package edu.asu.conceptpower.servlet.rest;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import edu.asu.conceptpower.servlet.core.POS;

@Component
public class ConceptSearchParameterValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return ConceptSearchParameters.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ConceptSearchParameters conceptSearchParameter = (ConceptSearchParameters) target;

        if (conceptSearchParameter.getWord() == null || conceptSearchParameter.getWord().isEmpty()) {
            errors.reject("word", "Word cannot be empty.");
        }

        if (conceptSearchParameter.getPos() == null) {
            errors.reject("pos", "Pos cannot be empty.");
        } else if (!POS.posValues.contains(conceptSearchParameter.getPos())) {
            errors.reject("pos", "Please enter correct pos value.");
        }

        if (conceptSearchParameter.getOperator() != null) {
            if (SearchParamters.OP_OR.equalsIgnoreCase(conceptSearchParameter.getOperator())
                    || SearchParamters.OP_AND.equalsIgnoreCase(conceptSearchParameter.getOperator())) {
                errors.reject("operator", "Please enter correct operator value.");
            }
        }

        // Not validating for null condition of operator.
        // If operator is null, by default we apply or filter
    }

}
