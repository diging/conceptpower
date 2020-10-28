package edu.asu.conceptpower.core.impl;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import edu.asu.conceptpower.app.db4o.DBNames;
import edu.asu.conceptpower.app.db4o.IConceptDBManager;
import edu.asu.conceptpower.app.manager.ConceptListManager;
import edu.asu.conceptpower.app.model.ConceptList;

public class ConceptListManagerTest {
    
	@Mock
    private IConceptDBManager client;
	
	@InjectMocks
    private ConceptListManager conceptListManager;

	private ConceptList conceptList = new ConceptList();

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);

		List<ConceptList> list = new ArrayList<>();
		conceptList.setConceptListName("First List");
		conceptList.setDescription("First List Description");
		list.add(conceptList);

		Mockito.when(client.getAllConceptLists()).thenReturn(list);
        Mockito.when(client.getConceptList("First List")).thenReturn(conceptList);
	}

	@Test
	public void addConceptListTest() {
		conceptListManager.addConceptList("List Name", "List Description");
		Mockito.verify(client).storeConceptList(ArgumentMatchers.any(ConceptList.class), Mockito.eq(DBNames.DICTIONARY_DB));

	}

	@Test
	public void deleteConceptListTest() {
		conceptListManager.deleteConceptList("List Name");
		Mockito.verify(client).deleteConceptList(Mockito.eq("List Name"));
	}

	@Test
	public void getAllConceptListsTest() {
		List<ConceptList> list = conceptListManager.getAllConceptLists();
		Assert.assertNotNull(list);
		Assert.assertEquals(1, list.size());
	}
	
	@Test
	public void getConceptListTest() {
		String listName = "First List";
		ConceptList conceptList = conceptListManager.getConceptList(listName);
		Assert.assertNotNull(conceptList);
		Assert.assertEquals(conceptList, conceptList);
	}

	@Test
	public void getConceptListNullTest() {
		String listName = "Second List";
		ConceptList conceptList = conceptListManager.getConceptList(listName);
		Assert.assertNull(conceptList);
	}

	@Test
	public void checkExistingListForTrue() {
		String listName = "First List";
		boolean isExists = conceptListManager.checkExistingConceptList(listName);
		Assert.assertTrue(isExists);
	}

	@Test
	public void checkExistingListForFalse() {
		String listName = "Second List";
		boolean isExists = conceptListManager.checkExistingConceptList(listName);
		Assert.assertFalse(isExists);
	}

	@Test
	public void storeModifiedConceptList() {
		ConceptList list = new ConceptList();
		list.setConceptListName("List Name");
		conceptListManager.storeModifiedConceptList(list, "List Name 2");
		Mockito.verify(client).update(Mockito.eq(list), Mockito.eq("List Name 2"), Mockito.eq(DBNames.DICTIONARY_DB));

	}
}
