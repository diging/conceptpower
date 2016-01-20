package edu.asu.conceptpower.core.impl;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import edu.asu.conceptpower.core.ConceptList;
import edu.asu.conceptpower.db4o.IConceptDBManager;

public class ConceptListManagerTest {

    @Mock
    private IConceptDBManager client = Mockito.mock(IConceptDBManager.class);;

    @InjectMocks
    private ConceptListManager conceptListManager;

    private ConceptList conceptList = new ConceptList();
    
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        List list = new ArrayList();
        conceptList.setConceptListName("First List");
        conceptList.setDescription("First List Description");
        list.add(conceptList);

        Mockito.when(client.getAllElementsOfType(ConceptList.class)).thenReturn(list);
        Mockito.when(client.getConceptList("First List")).thenReturn(conceptList);

    }

    @Test
    public void getAllConceptListsTest() {
        List list = conceptListManager.getAllConceptLists();
        assertNotNull(list);
        assertEquals(1, list.size());
    }

    @Test
    public void getConceptListTest() {
        String listName = "First List";
        ConceptList conceptList = conceptListManager.getConceptList(listName);
        assertNotNull(conceptList);
        assertEquals(conceptList, conceptList);
    }

    @Test
    public void getConceptListNullTest() {
        String listName = "Second List";
        ConceptList conceptList = conceptListManager.getConceptList(listName);
        assertNull(conceptList);
    }

    @Test
    public void checkExistingListForTrue() {
        String listName = "First List";
        boolean isExists = conceptListManager.checkExistingConceptList(listName);
        assertTrue(isExists);
    }

    @Test
    public void checkExistingListForFalse() {
        String listName = "Second List";
        boolean isExists = conceptListManager.checkExistingConceptList(listName);
        assertFalse(isExists);
    }
}
