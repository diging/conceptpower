package edu.asu.conceptpower.servlet.rest;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import edu.asu.conceptpower.servlet.core.POS;

/**
 * This class validates the Concept power search parameters. Checks whether user
 * has submitted word and pos details for searching the concept In addition, the
 * validator checks for the operators (OR and AND)
 * 
 * @author karthikeyanmohan
 *
 */
@Component
public class ConceptSearchParameterValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return ConceptSearchParameters.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ConceptSearchParameters conceptSearchParameter = (ConceptSearchParameters) target;

        ValidationUtils.rejectIfEmpty(errors, "word", "word", "Word cannot be empty.");
        ValidationUtils.rejectIfEmpty(errors, "pos", "pos", "Pos cannot be empty.");

        if (!POS.posValues.contains(conceptSearchParameter.getPos())) {
            errors.reject("pos", "Please enter correct pos value.");
        }

        if (conceptSearchParameter.getOperator() != null) {
            if (SearchParamters.OP_OR.equalsIgnoreCase(conceptSearchParameter.getOperator())
                    || SearchParamters.OP_AND.equalsIgnoreCase(conceptSearchParameter.getOperator())) {
                errors.reject("operator", "Please enter correct operator value.");
            }
        }
    }

}
