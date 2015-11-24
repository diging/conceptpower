package edu.asu.conceptpower.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import edu.asu.conceptpower.db4o.TypeDatabaseClient;
import edu.asu.conceptpower.web.ConceptTypeAddForm;

/**
 * This class provides methods for validating name, description for concept Types
 * 
 * @author Karthikeyan
 * 
 */
@Component
public class ConceptTypeAddValidator implements Validator {

	@Autowired
	private TypeDatabaseClient client;

	@Override
	public boolean supports(Class<?> clazz) {
		return clazz.isAssignableFrom(ConceptTypeAddForm.class);
	}

	@Override
	public void validate(Object obj, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "typeName", "required.type_name");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "typeDescription", "required.type_description");

		if (!errors.hasErrors()) {
			ConceptTypeAddForm conceptTypeAddForm = (ConceptTypeAddForm) obj;
			// If the name is same as old name then user has not changed the
			// concept type name
			if (!conceptTypeAddForm.getOldTypeName().equalsIgnoreCase(conceptTypeAddForm.getTypeName())) {
				// To Check if its existing name
				if (client.findType(conceptTypeAddForm.getTypeName()) != null) {
					errors.rejectValue("typeName", "required.unique.type_name");
				}
			}
		}

	}

}
