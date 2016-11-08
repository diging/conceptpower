package edu.asu.conceptpower.core;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import edu.asu.conceptpower.app.core.IIndexService;
import edu.asu.conceptpower.app.core.impl.ConceptManager;
import edu.asu.conceptpower.app.db4o.IConceptDBManager;
import edu.asu.conceptpower.app.exceptions.DictionaryDoesNotExistException;
import edu.asu.conceptpower.app.exceptions.DictionaryModifyException;
import edu.asu.conceptpower.app.exceptions.IndexerRunningException;
import edu.asu.conceptpower.app.exceptions.LuceneException;
import edu.asu.conceptpower.app.lucene.ILuceneUtility;
import edu.asu.conceptpower.app.wordnet.Constants;
import edu.asu.conceptpower.app.wordnet.WordNetManager;
import edu.asu.conceptpower.servlet.core.ChangeEvent;
import edu.asu.conceptpower.servlet.core.ChangeEvent.ChangeEventTypes;
import junit.framework.Assert;

public class ConceptManagerTest {

    @Mock
    private ConceptManager conceptManager;

    @Mock
    private IConceptDBManager dbManager;

    @InjectMocks
    private ConceptManager managerToTest;

    @Mock
    private ILuceneUtility luceneIndex;

    @Mock
    private WordNetManager wordNetManager;

    @Mock
    private IIndexService indexService;

    private ConceptEntry addedConcept;

    private ConceptEntry wordnetConcept1;
    private ConceptEntry wordnetConcept2;
    private ConceptEntry addedConceptsForWordNet;
    private ConceptList list1;

    @Before
    public void init() throws LuceneException {
        conceptManager = Mockito.mock(ConceptManager.class);

        ChangeEvent changeEvent = new ChangeEvent();
        changeEvent.setUserName("testuser");
        changeEvent.setType(ChangeEventTypes.CREATION);
        changeEvent.setDate(new Date());

        wordNetManager = Mockito.mock(WordNetManager.class);
        dbManager = Mockito.mock(IConceptDBManager.class);
        MockitoAnnotations.initMocks(this);

        addedConcept = new ConceptEntry();
        addedConcept.setId("id1");
        addedConcept.setConceptList("list1");

        addedConcept.addNewChangeEvent(changeEvent);
        addedConcept.setDescription("description");
        addedConcept.setPos("noun");
        addedConcept.setWord("test");
        addedConcept.setWordnetId("WID-1");
        addedConcept.setSynonymIds("WID-2");

        Mockito.when(dbManager.getEntry("id1")).thenReturn(addedConcept);

        wordnetConcept1 = new ConceptEntry();
        wordnetConcept1.setId("WID-1");
        wordnetConcept1.setConceptList("list1");
        wordnetConcept1.addNewChangeEvent(changeEvent);
        wordnetConcept1.setDescription("description wid 1");
        wordnetConcept1.setPos("noun");
        wordnetConcept1.setWord("test wid 1");
        wordnetConcept1.setWordnetId("WID-1");
        wordnetConcept1.setSynonymIds("WID-2");

        wordnetConcept2 = new ConceptEntry();
        wordnetConcept2.setId("WID-2");
        wordnetConcept2.setConceptList("list1");
        wordnetConcept2.addNewChangeEvent(changeEvent);
        wordnetConcept2.setDescription("description wid");
        wordnetConcept2.setPos("noun");
        wordnetConcept2.setWord("test wid");
        wordnetConcept2.setWordnetId("WID-2");

        Mockito.when(conceptManager.getConceptEntry("WID-1")).thenReturn(wordnetConcept1);
        Mockito.when(conceptManager.getConceptEntry("WID-2")).thenReturn(wordnetConcept2);

        list1 = new ConceptList();
        list1.setConceptListName("list1");
        list1.setDescription("test test");
        Mockito.when(dbManager.getConceptList("list1")).thenReturn(list1);

        ConceptList wordnet = new ConceptList();
        wordnet.setConceptListName(Constants.WORDNET_DICTIONARY);
        wordnet.setDescription("WordNet list");
        Mockito.when(dbManager.getConceptList(Constants.WORDNET_DICTIONARY)).thenReturn(wordnet);

        addedConceptsForWordNet = new ConceptEntry();
        addedConceptsForWordNet.setWord("wordnetConcept2");
        addedConceptsForWordNet.setId("WID-2");
        Mockito.when(wordNetManager.getConcept("WID-2")).thenReturn(addedConceptsForWordNet);
    }

    @Test
    public void testGetConceptEntryForAddedConcept() throws LuceneException {
        ConceptEntry entry = managerToTest.getConceptEntry("id1");
        Assert.assertEquals(addedConcept, entry);
        // Test for fetching the creatorId from changeEvent
        Assert.assertEquals("testuser", entry.getCreatorId());
        Assert.assertTrue(entry.getSynonymIds().contains("WID-2"));
    }

    @Test
    public void testGetConceptEntryForWordnetConcept() throws LuceneException {
        ConceptEntry entry = managerToTest.getConceptEntry("WID-2");
        Assert.assertEquals(addedConceptsForWordNet, entry);
    }

    @Test
    public void testGetConceptEntryNoExisting() throws LuceneException {
        ConceptEntry entry = managerToTest.getConceptEntry("id-non");
        Assert.assertNull(entry);
    }

    @Test
    public void testAddConceptListEntry()
            throws DictionaryDoesNotExistException, DictionaryModifyException, LuceneException, IllegalAccessException, IndexerRunningException {
        ConceptEntry newConcept = new ConceptEntry();
        newConcept.setConceptList("list1");
        newConcept.setDescription("description");
        newConcept.setPos("noun");
        newConcept.setWord("test new");
        newConcept.setWordnetId("WID-1");
        String id = managerToTest.addConceptListEntry(newConcept, "testuser");
        Assert.assertNotNull(newConcept.getChangeEvents());
        Mockito.verify(indexService).insertConcept(newConcept);
        Assert.assertNotNull(id);
    }

    @Test(expected = DictionaryDoesNotExistException.class)
    public void testAddConceptListEntryWrongDict()
            throws DictionaryModifyException, LuceneException, IllegalAccessException, DictionaryDoesNotExistException, IndexerRunningException {
        ConceptEntry newConcept = new ConceptEntry();
        newConcept.setConceptList("list-not-exist");
        newConcept.setDescription("description");
        newConcept.setPos("noun");
        newConcept.setWord("test new");
        newConcept.setWordnetId("WID-1");

        String id = null;
        id = managerToTest.addConceptListEntry(newConcept, "testuser");

        Assert.assertNull(id);
    }

    @Test(expected = DictionaryModifyException.class)
    public void testAddConceptListEntryToWordnet()
            throws DictionaryDoesNotExistException, LuceneException, IllegalAccessException, DictionaryModifyException, IndexerRunningException {
        ConceptEntry newConcept = new ConceptEntry();
        newConcept.setConceptList(Constants.WORDNET_DICTIONARY);
        newConcept.setDescription("description");
        newConcept.setPos("noun");
        newConcept.setWord("test new");
        newConcept.setWordnetId("WID-1");

        String id = null;
        id = managerToTest.addConceptListEntry(newConcept, "testuser");

        Assert.assertNull(id);
    }
}
