package edu.asu.conceptpower.validation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import edu.asu.conceptpower.core.IConceptListManager;
import edu.asu.conceptpower.web.ConceptListAddForm;
import junit.framework.Assert;

public class ConceptListAddValidatorTest {

    @Mock
    private IConceptListManager conceptListManager;
    
    @InjectMocks
    private ConceptListAddValidator conceptListAddValidator;
    
    private ConceptListAddForm emptyListName;
    private ConceptListAddForm emptyDescription;
    private ConceptListAddForm wordNetConcepts;
    private ConceptListAddForm existingListName;
    
    @Before
    public void init(){
        conceptListManager = Mockito.mock(IConceptListManager.class);
        MockitoAnnotations.initMocks(this);
        
        emptyListName = new ConceptListAddForm();
        emptyListName.setListName("");
        
    }
    
    @Test
    public void testValidListName() {
     
        Errors errors = new BindException(emptyListName, "conceptListAddForm");
        ValidationUtils.invokeValidator(conceptListAddValidator, emptyListName, errors);
        Assert.assertEquals(2, errors.getFieldErrorCount());
        Assert.assertEquals(errors.getFieldError("listName").getCode(), "concept_name.required");
        Assert.assertEquals(errors.getFieldError("description").getCode(), "concept_description.required");
    }
    
    /*@Test
    public void testValidDescription() {
        
    }
    
    @Test
    public void testWordNetConcepts() {
        
    }
    
    @Test
    public void testExistingListName() {
        
    }*/
}
