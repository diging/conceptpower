package edu.asu.conceptpower.core.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import edu.asu.conceptpower.app.db.TypeDatabaseClient;
import edu.asu.conceptpower.app.manager.impl.ConceptTypesManager;
import edu.asu.conceptpower.app.model.ConceptType;

public class ConceptTypesManagerTest {

    @Mock
    private TypeDatabaseClient client = Mockito.mock(TypeDatabaseClient.class);

    @InjectMocks
    private ConceptTypesManager conceptTypesManager;

    private ConceptType type = new ConceptType();

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        type.setTypeId("TYPE_ID");
        type.setTypeName("TYPE_NAME");

        ConceptType type2 = new ConceptType();
        type2.setTypeId("Type Id 2");
        type2.setTypeName("Type Name 2");

        List<ConceptType> types = new ArrayList<>();
        types.add(type);
        types.add(type2);
        Mockito.when(client.getAllTypes()).thenReturn(types);

        Mockito.when(client.getType(type.getTypeId())).thenReturn(type);
        Mockito.when(client.addType(type)).thenReturn(type);
    }

    @Test
    public void addConceptType() {
        conceptTypesManager.addConceptType(type);
        verify(client).addType(type);
    }

    @Test
    public void storeModifiedConceptTypeTest() {
        conceptTypesManager.storeModifiedConceptType(type);
        verify(client).update(type);
    }

    @Test
    public void getAllTypesTest() {
        List<ConceptType> conceptTypes = conceptTypesManager.getAllTypes();
        assertNotNull(conceptTypes);
        assertEquals(2, conceptTypes.size());
    }

    @Test
    public void getTypeTest() {
        ConceptType conceptType = conceptTypesManager.getType("TYPE_ID");
        assertNotNull(conceptType);
        assertEquals("TYPE_ID", conceptType.getTypeId());
        assertEquals("TYPE_NAME", conceptType.getTypeName());

    }
    
    @Test
    public void deleteTypeTest(){
    	conceptTypesManager.deleteType("ID");
    	Mockito.verify(client).deleteType(Mockito.eq("ID"));
    }
}
