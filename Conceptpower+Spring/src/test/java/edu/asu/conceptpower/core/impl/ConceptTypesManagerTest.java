package edu.asu.conceptpower.core.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import edu.asu.conceptpower.core.ConceptType;
import edu.asu.conceptpower.db4o.TypeDatabaseClient;

public class ConceptTypesManagerTest {

    @Mock
    private TypeDatabaseClient client = Mockito.mock(TypeDatabaseClient.class);

    @InjectMocks
    private ConceptTypesManager conceptTypesManager;

    private String id = "CONCEPT_ID";

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        ConceptType type = new ConceptType();
        type.setTypeId("TYPE_ID");
        type.setTypeName("TYPE_NAME");

        ConceptType type2 = new ConceptType();
        type2.setTypeId("Type Id 2");
        type2.setTypeName("Type Name 2");

        ConceptType[] types = new ConceptType[2];
        types[0] = type;
        types[1] = type2;
        Mockito.when(client.getAllTypes()).thenReturn(types);

        Mockito.when(client.getType(type.getTypeId())).thenReturn(type);
        // Mockito.when(client.update(type)).thenReturn(type);

        // Mockito.when(uuid.randomUUID().toString()).thenReturn("TYPE_ID");
    }

    @Test
    public void addConceptType() {

        // Not able to mock UUID class since it is a final class.
    }

    @Test
    public void storeModifiedConceptTypeTest() {

        // Method returning only void and not changing the state of the object
    }

    @Test
    public void getAllTypesTest() {
        assertNotNull(conceptTypesManager.getAllTypes());
    }

    @Test
    public void getTypeTest() {
        assertNotNull(conceptTypesManager.getType("TYPE_ID"));
    }
}
