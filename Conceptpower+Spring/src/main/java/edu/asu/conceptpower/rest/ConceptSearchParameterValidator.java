package edu.asu.conceptpower.rest;

import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import edu.asu.conceptpower.app.core.POS;

/**
 * This class validates the Conceptpower search parameters. Checks whether a
 * user has submitted word and pos details when searching for a concept. In
 * addition, the validator checks for the operators (OR and AND).
 * 
 * @author karthikeyanmohan
 *
 */
@Component
public class ConceptSearchParameterValidator implements Validator {

    private final Logger logger = LoggerFactory.getLogger(ConceptSearchParameterValidator.class);

    @Override
    public boolean supports(Class<?> clazz) {
        return ConceptSearchParameters.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ConceptSearchParameters conceptSearchParameter = (ConceptSearchParameters) target;

        if (!hasAtLeastOneNonEmpty(conceptSearchParameter)) {
            errors.reject("invalidSearch", "No valid search parameters present.");
        }
        if (conceptSearchParameter.getPos() != null && !POS.posValues.contains(conceptSearchParameter.getPos().toLowerCase())) {
            errors.reject("pos", "Please enter correct pos value.");
        }
        if (conceptSearchParameter.getOperator() != null) {
            if (!SearchParamters.OP_OR.equalsIgnoreCase(conceptSearchParameter.getOperator())
                    && !SearchParamters.OP_AND.equalsIgnoreCase(conceptSearchParameter.getOperator())) {
                errors.reject("operator", "Please enter correct operator value.");
            }
        }
    }

    private boolean hasAtLeastOneNonEmpty(ConceptSearchParameters conceptSearchParameters) {
        Field[] fields = ConceptSearchParameters.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                if (field.get(conceptSearchParameters) != null) {
                    return true;
                }
            } catch (IllegalArgumentException e) {
                logger.error("IllegalArgumentException", e);
            } catch (IllegalAccessException ie) {
                logger.error("IllegalAccessException", ie);
            }
        }
        return false;
    }

}
