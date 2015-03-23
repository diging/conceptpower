package edu.asu.conceptpower.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.beanutils.BeanUtils;

/*
 * Taken from 
 * http://stackoverflow.com/questions/1972933/cross-field-validation-with-hibernate-validator-jsr-303
 */
public class ValuesMatchValidator implements
		ConstraintValidator<ValuesMatch, Object> {

	private String firstFieldName;
	private String secondFieldName;

	@Override
	public void initialize(ValuesMatch arg0) {
		firstFieldName = arg0.first();
		secondFieldName = arg0.second();
	}

	@Override
	public boolean isValid(Object arg0, ConstraintValidatorContext arg1) {
		try {
			final Object firstObj = BeanUtils.getProperty(arg0, firstFieldName);
			final Object secondObj = BeanUtils.getProperty(arg0, secondFieldName);

			return firstObj == null && secondObj == null || firstObj != null
					&& firstObj.equals(secondObj);
		} catch (final Exception ignore) {
			ignore.printStackTrace();
		}
		return true;
	}

}
