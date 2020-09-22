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

import edu.asu.conceptpower.app.core.impl.ConceptListService;
import edu.asu.conceptpower.app.model.ConceptList;
import edu.asu.conceptpower.app.repository.IConceptListRepository;

public class ConceptListManagerTest {

	@Mock
	private IConceptListRepository client = Mockito.mock(IConceptListRepository.class);

	@InjectMocks
	private ConceptListService conceptListManager;

	private ConceptList conceptList = new ConceptList();

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);

		List<ConceptList> list = new ArrayList<>();
		conceptList.setConceptListName("First List");
		conceptList.setDescription("First List Description");
		list.add(conceptList);

		Mockito.when(client.findAll()).thenReturn(list);
		Mockito.when(client.findByConceptListName("First List")).thenReturn(conceptList);
	}

	@Test
	public void addConceptListTest() {
		conceptListManager.addConceptList("List Name", "List Description");
		Mockito.verify(client).save(ArgumentMatchers.any(ConceptList.class));

	}

	@Test
	public void deleteConceptListTest() {
		conceptListManager.deleteConceptList("List Name");
		Mockito.verify(client).deleteByConceptListName(Mockito.eq("List Name"));
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
		Mockito.verify(client).save(Mockito.eq(list));

	}
}
