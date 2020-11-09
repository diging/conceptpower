package edu.asu.conceptpower.validation;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import edu.asu.conceptpower.app.manager.IConceptListManager;
import edu.asu.conceptpower.app.validation.ConceptListAddValidator;
import edu.asu.conceptpower.app.wordnet.Constants;
import edu.asu.conceptpower.web.ConceptListAddForm;

public class ConceptListAddValidatorTest {

    @Mock
    private IConceptListManager conceptListService;

    @InjectMocks
    private ConceptListAddValidator conceptListAddValidator;

    private ConceptListAddForm emptyListName;
    private ConceptListAddForm emptyDescription;
    private ConceptListAddForm wordNetConcepts;
    private ConceptListAddForm existingListName;
    private ConceptListAddForm nameDescription;

    @Before
    public void init() {
        conceptListService = Mockito.mock(IConceptListManager.class);
        MockitoAnnotations.initMocks(this);

        emptyListName = new ConceptListAddForm();
        emptyListName.setListName("");

        emptyDescription = new ConceptListAddForm();
        emptyDescription.setListName("test");
        emptyDescription.setDescription("");

        wordNetConcepts = new ConceptListAddForm();
        wordNetConcepts.setListName(Constants.WORDNET_DICTIONARY);
        wordNetConcepts.setDescription("WordNet Concept");

        existingListName = new ConceptListAddForm();
        existingListName.setListName("ExistingList");
        existingListName.setDescription("Description");
        existingListName.setOldListName("OldExistingList");
        Mockito.when(conceptListService.checkExistingConceptList("ExistingList")).thenReturn(true);
        
        nameDescription = new ConceptListAddForm();
        nameDescription.setListName("NewListName");
        nameDescription.setDescription("Description");
        Mockito.when(conceptListService.checkExistingConceptList("NewListName")).thenReturn(false);
    }

    @Test
    public void testValidListName() {
        Errors errors = new BindException(emptyListName, "conceptListAddForm");
        ValidationUtils.invokeValidator(conceptListAddValidator, emptyListName, errors);
        Assert.assertEquals(2, errors.getFieldErrorCount());
        Assert.assertEquals(errors.getFieldError("listName").getCode(), "concept_name.required");
        Assert.assertEquals(errors.getFieldError("description").getCode(), "concept_description.required");
    }

    @Test
    public void testValidDescription() {
        Errors errors = new BindException(emptyDescription, "conceptListAddForm");
        ValidationUtils.invokeValidator(conceptListAddValidator, emptyDescription, errors);
        Assert.assertEquals(1, errors.getFieldErrorCount());
        Assert.assertNull(errors.getFieldError("listName"));
        Assert.assertEquals(errors.getFieldError("description").getCode(), "concept_description.required");

    }

    @Test
    public void testWordNetConcepts() {
        Errors errors = new BindException(wordNetConcepts, "conceptListAddForm");
        ValidationUtils.invokeValidator(conceptListAddValidator, wordNetConcepts, errors);
        Assert.assertEquals(1, errors.getFieldErrorCount());
        Assert.assertNull(errors.getFieldError("description"));
        Assert.assertEquals(errors.getFieldError("listName").getCode(), "concept_list_name.wordnet");
    }

    @Test
    public void testExistingListName() {
        Errors errors = new BindException(existingListName, "conceptListAddForm");
        ValidationUtils.invokeValidator(conceptListAddValidator, existingListName, errors);
        Assert.assertEquals(1, errors.getFieldErrorCount());
        Assert.assertNull(errors.getFieldError("description"));
        Assert.assertEquals(errors.getFieldError("listName").getCode(), "concept_unique.required");
    }
    
    @Test
    public void testNameDescription() {
        Errors errors = new BindException(nameDescription, "conceptListAddForm");
        ValidationUtils.invokeValidator(conceptListAddValidator, nameDescription, errors);
        Assert.assertEquals(0, errors.getFieldErrorCount());
        Assert.assertNull(errors.getFieldError("listName"));
        Assert.assertNull(errors.getFieldError("description"));
    }
}
