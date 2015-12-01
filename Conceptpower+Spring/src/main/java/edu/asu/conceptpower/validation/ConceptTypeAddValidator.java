package edu.asu.conceptpower.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import edu.asu.conceptpower.web.ConceptTypeAddForm;

@Component
public class ConceptTypeAddValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		return clazz.isAssignableFrom(ConceptTypeAddForm.class);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "typeName", "required.type_name");
	}

}
