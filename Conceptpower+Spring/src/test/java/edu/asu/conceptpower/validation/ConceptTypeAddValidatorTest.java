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

import edu.asu.conceptpower.app.db.TypeDatabaseClient;
import edu.asu.conceptpower.app.validation.ConceptTypeAddValidator;
import edu.asu.conceptpower.core.ConceptType;
import edu.asu.conceptpower.web.ConceptTypeAddForm;

public class ConceptTypeAddValidatorTest {

    @Mock
    private TypeDatabaseClient client;

    @InjectMocks
    private ConceptTypeAddValidator validator;

    private ConceptTypeAddForm emptyTypeName;
    private ConceptTypeAddForm emptyTypeDescripton;
    private ConceptType duplicateTypeName;
    private ConceptTypeAddForm duplicateType;
    private ConceptTypeAddForm emptyNameWithDescription;
    private ConceptTypeAddForm typeNameDescription;

    @Before
    public void init() {

        client = Mockito.mock(TypeDatabaseClient.class);
        MockitoAnnotations.initMocks(this);

        emptyTypeName = new ConceptTypeAddForm();
        emptyTypeName.setTypeName("");

        emptyTypeDescripton = new ConceptTypeAddForm();
        emptyTypeDescripton.setTypeName("TypeName");
        emptyTypeDescripton.setTypeDescription("");

        duplicateType = new ConceptTypeAddForm();
        duplicateType.setTypeName("ExistingList");
        duplicateType.setTypeDescription("Existing Description");

        typeNameDescription = new ConceptTypeAddForm();
        typeNameDescription.setTypeName("New Type");
        typeNameDescription.setTypeDescription("Type Decription");

        emptyNameWithDescription = new ConceptTypeAddForm();
        emptyNameWithDescription.setTypeDescription("Type Description");

        duplicateTypeName = new ConceptType();
        duplicateTypeName.setTypeName("ExistingList");
        duplicateTypeName.setDescription("Existing Description");
        Mockito.when(client.findType("ExistingList")).thenReturn(duplicateTypeName);
    }

    @Test
    public void testValidName() {

        Errors errors = new BindException(emptyTypeName, "conceptListAddForm");
        ValidationUtils.invokeValidator(validator, emptyTypeName, errors);
        Assert.assertEquals(2, errors.getFieldErrorCount());
        Assert.assertEquals(errors.getFieldError("typeName").getCode(), "required.type_name");
        Assert.assertEquals(errors.getFieldError("typeDescription").getCode(), "required.type_description");
    }

    @Test
    public void testExistingName() {
        Errors errors = new BindException(duplicateType, "conceptListAddForm");
        ValidationUtils.invokeValidator(validator, duplicateType, errors);
        Assert.assertEquals(1, errors.getFieldErrorCount());
        Assert.assertEquals(errors.getFieldError("typeName").getCode(), "required.unique.type_name");
    }

    @Test
    public void testNameWithoutDescription() {
        Errors errors = new BindException(emptyTypeDescripton, "conceptListAddForm");
        ValidationUtils.invokeValidator(validator, emptyTypeDescripton, errors);
        Assert.assertEquals(1, errors.getFieldErrorCount());
        Assert.assertEquals(errors.getFieldError("typeDescription").getCode(), "required.type_description");
    }

    @Test
    public void testDescritionWithoutName() {
        Errors errors = new BindException(emptyNameWithDescription, "conceptListAddForm");
        ValidationUtils.invokeValidator(validator, emptyNameWithDescription, errors);
        Assert.assertEquals(1, errors.getFieldErrorCount());
        Assert.assertEquals(errors.getFieldError("typeName").getCode(), "required.type_name");
    }

    @Test
    public void testNameDescription() {
        Errors errors = new BindException(typeNameDescription, "conceptListAddForm");
        ValidationUtils.invokeValidator(validator, typeNameDescription, errors);
        Assert.assertEquals(0, errors.getFieldErrorCount());
    }
}
