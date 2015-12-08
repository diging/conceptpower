package edu.asu.conceptpower.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.beanutils.BeanUtils;

/*
 * Taken from 
 * http://stackoverflow.com/questions/1972933/cross-field-validation-with-hibernate-validator-jsr-303
 */
public class ValuesMatchValidator implements ConstraintValidator<ValuesMatch, Object> {

    private String firstFieldName;
    private String secondFieldName;

    @Override
    public void initialize(ValuesMatch arg0) {
        firstFieldName = arg0.first();
        secondFieldName = arg0.second();
    }

    @Override
    public boolean isValid(Object arg0, ConstraintValidatorContext context) {
        try {
            final Object firstObj = BeanUtils.getProperty(arg0, firstFieldName);
            final Object secondObj = BeanUtils.getProperty(arg0, secondFieldName);

            if (firstObj.equals("") || firstObj == null) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("This field cannot be empty").addNode(firstFieldName)
                        .addConstraintViolation();
                return false;
            }

            if (firstObj.toString().length() < 4 && !(firstObj == null)) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Password should be at least 4 characters long.")
                        .addNode(firstFieldName).addConstraintViolation();
                return false;
            }

            if (secondObj.equals("") || secondObj == null) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("This field cannot be empty").addNode(secondFieldName)
                        .addConstraintViolation();
                return false;
            }

            boolean matches = firstObj == null && secondObj == null || firstObj != null && firstObj.equals(secondObj);

            if (!matches) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Passwords do not match").addNode(secondFieldName)
                        .addConstraintViolation();
            }

            return matches;
        } catch (final Exception ignore) {
            ignore.printStackTrace();
        }
        return true;
    }

}
