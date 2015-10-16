package edu.asu.conceptpower.core;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import edu.asu.conceptpower.core.impl.ConceptManager;
import edu.asu.conceptpower.db4o.IConceptDBManager;
import edu.asu.conceptpower.wordnet.WordNetManager;

public class ConceptManagerTest {

	@Mock
	private WordNetManager wordNetManager;
	
	@Mock
	private IConceptDBManager dbManager;
	
	@InjectMocks
	private ConceptManager managerToTest;

	private ConceptEntry addedConcept;

	private ConceptEntry wordnetConcept1;
	private ConceptEntry wordnetConcept2;
	
	@Before
	public void init() {
		wordNetManager = Mockito.mock(WordNetManager.class);
		dbManager = Mockito.mock(IConceptDBManager.class);
		MockitoAnnotations.initMocks(this);
		
		addedConcept = new ConceptEntry();
		addedConcept.setId("id1");
		addedConcept.setConceptList("list1");
		addedConcept.setCreatorId("testuser");
		addedConcept.setDescription("description");
		addedConcept.setPos("noun");
		addedConcept.setWord("test");
		addedConcept.setWordnetId("WID-1");
		
		Mockito.when(dbManager.getEntry("id1")).thenReturn(addedConcept);
		
		wordnetConcept1 = new ConceptEntry();
		wordnetConcept1.setId("WID-1");
		wordnetConcept1.setConceptList("list1");
		wordnetConcept1.setCreatorId("testuser");
		wordnetConcept1.setDescription("description wid 1");
		wordnetConcept1.setPos("noun");
		wordnetConcept1.setWord("test wid 1");
		wordnetConcept1.setWordnetId("WID-1");
		wordnetConcept1.setSynonymIds("WID-2");
		
		wordnetConcept2 = new ConceptEntry();
		wordnetConcept2.setId("WID-2");
		wordnetConcept2.setConceptList("list1");
		wordnetConcept2.setCreatorId("testuser");
		wordnetConcept2.setDescription("description wid");
		wordnetConcept2.setPos("noun");
		wordnetConcept2.setWord("test wid");
		wordnetConcept2.setWordnetId("WID-2");
		
		Mockito.when(wordNetManager.getConcept("WID-1")).thenReturn(wordnetConcept1);
		Mockito.when(wordNetManager.getConcept("WID-2")).thenReturn(wordnetConcept2);
	}
	
	@Test
	public void testGetConceptEntryForAddedConcept() {
		ConceptEntry entry = managerToTest.getConceptEntry("id1");
		Assert.assertEquals(addedConcept, entry);
		
		Assert.assertTrue(entry.getSynonymIds().contains("WID-2"));
	}
	
	@Test 
	public void testGetConceptEntryForWordnetConcept() {
		ConceptEntry entry = managerToTest.getConceptEntry("WID-2");
		Assert.assertEquals(wordnetConcept2, entry);
	}
	
	@Test
	public void testGetConceptEntryNoExisting() {
		ConceptEntry entry = managerToTest.getConceptEntry("id-non");
		Assert.assertNull(entry);
	}
}
