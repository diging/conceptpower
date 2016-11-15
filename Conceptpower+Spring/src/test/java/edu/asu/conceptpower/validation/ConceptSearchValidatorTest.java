package edu.asu.conceptpower.validation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import edu.asu.conceptpower.app.core.POS;
import edu.asu.conceptpower.app.validation.ConceptSearchValidator;
import edu.asu.conceptpower.web.ConceptSearchBean;
import junit.framework.Assert;

public class ConceptSearchValidatorTest {

    private ConceptSearchValidator conceptSearchValidator = new ConceptSearchValidator();

    private ConceptSearchBean testForWord;
    private ConceptSearchBean testForPos;
    private ConceptSearchBean testForEmptyWordWithPos;
    private ConceptSearchBean testForWordPos;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        testForWord = new ConceptSearchBean();
        testForWord.setWord("");

        testForPos = new ConceptSearchBean();
        testForPos.setWord("pony");

        testForWordPos = new ConceptSearchBean();
        testForWordPos.setWord("pony");
        testForWordPos.setPos(POS.NOUN);

        testForEmptyWordWithPos = new ConceptSearchBean();
        testForEmptyWordWithPos.setPos(POS.NOUN);

    }

    @Test
    public void testWord() {
        Errors errors = new BindException(testForWord, "conceptSearchBean");
        ValidationUtils.invokeValidator(conceptSearchValidator, testForWord, errors);
        Assert.assertEquals(2, errors.getFieldErrorCount());
        Assert.assertEquals(errors.getFieldError("word").getCode(), "name.required");
        Assert.assertEquals(errors.getFieldError("pos").getCode(), "pos.required");

    }

    @Test
    public void testPos() {
        Errors errors = new BindException(testForPos, "conceptListAddForm");
        ValidationUtils.invokeValidator(conceptSearchValidator, testForPos, errors);
        Assert.assertEquals(1, errors.getFieldErrorCount());
        Assert.assertNull(errors.getFieldError("word"));
        Assert.assertEquals(errors.getFieldError("pos").getCode(), "pos.required");
    }

    @Test
    public void testForEmptyWordWithPos() {
        Errors errors = new BindException(testForEmptyWordWithPos, "conceptListAddForm");
        ValidationUtils.invokeValidator(conceptSearchValidator, testForEmptyWordWithPos, errors);
        Assert.assertEquals(1, errors.getFieldErrorCount());
        Assert.assertNull(errors.getFieldError("pos"));
        Assert.assertEquals(errors.getFieldError("word").getCode(), "name.required");
    }

    @Test
    public void testForWordPos() {
        Errors errors = new BindException(testForWordPos, "conceptListAddForm");
        ValidationUtils.invokeValidator(conceptSearchValidator, testForWordPos, errors);
        Assert.assertEquals(0, errors.getFieldErrorCount());
    }

}
