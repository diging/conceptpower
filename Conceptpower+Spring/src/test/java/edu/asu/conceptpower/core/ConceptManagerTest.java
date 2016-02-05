package edu.asu.conceptpower.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import edu.asu.conceptpower.core.impl.ConceptManager;
import edu.asu.conceptpower.db4o.DBNames;
import edu.asu.conceptpower.db4o.IConceptDBManager;
import edu.asu.conceptpower.exceptions.DictionaryDoesNotExistException;
import edu.asu.conceptpower.exceptions.DictionaryEntryExistsException;
import edu.asu.conceptpower.exceptions.DictionaryModifyException;
import edu.asu.conceptpower.wordnet.Constants;
import edu.asu.conceptpower.wordnet.WordNetManager;
import junit.framework.Assert;

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

    private ConceptList list1;
    private ConceptEntry ConceptEntryForSynonym;
    private ConceptEntry[] entries = new ConceptEntry[2];
    private ConceptEntry[] synonymEntries = new ConceptEntry[2];
    private ConceptEntry[] wordEntries = new ConceptEntry[1];
    private ConceptEntry[] wordNetwordEntries = new ConceptEntry[1];
    private List<ConceptEntry> conceptEntriesForList = new ArrayList<ConceptEntry>();

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

        list1 = new ConceptList();
        list1.setConceptListName("list1");
        list1.setDescription("test test");
        Mockito.when(dbManager.getConceptList("list1")).thenReturn(list1);

        ConceptList wordnet = new ConceptList();
        wordnet.setConceptListName(Constants.WORDNET_DICTIONARY);
        wordnet.setDescription("WordNet list");
        Mockito.when(dbManager.getConceptList(Constants.WORDNET_DICTIONARY)).thenReturn(wordnet);

        Mockito.when(wordNetManager.getConcept("WordNetId")).thenReturn(wordnetConcept2);

        entries[0] = wordnetConcept1;
        entries[1] = wordnetConcept2;

        Mockito.when(dbManager.getEntriesByFieldContains("word", "pony")).thenReturn(entries);

        ConceptEntryForSynonym = new ConceptEntry();
        ConceptEntryForSynonym.setId("ID");
        ConceptEntryForSynonym.setSynonymIds("SYN-1,SYN-2,SYN-3");
        Mockito.when(dbManager.getEntry("ID")).thenReturn(ConceptEntryForSynonym);

        ConceptEntry synonym = new ConceptEntry();
        synonym.setId("SYN-1");
        synonym.setDeleted(false);
        Mockito.when(dbManager.getEntry("SYN-1")).thenReturn(synonym);

        synonymEntries[0] = synonym;
        ConceptEntry differentSynonym = new ConceptEntry();
        differentSynonym.setId("SYN-5");
        synonymEntries[1] = differentSynonym;
        Mockito.when(dbManager.getSynonymsPointingToId("ID")).thenReturn(synonymEntries);

        ConceptEntry ponyConcept = new ConceptEntry();
        ponyConcept.setId("C-ID-1");
        ponyConcept.setWord("Pony");
        ConceptEntry ponyConcept2 = new ConceptEntry();
        ponyConcept2.setId("C-ID-2");
        ponyConcept2.setWord("Pony2");

        wordEntries[0] = ponyConcept;
        wordNetwordEntries[0] = ponyConcept2;
        Mockito.when(dbManager.getEntriesForWord("pony")).thenReturn(wordEntries);
        Mockito.when(wordNetManager.getEntriesForWord("pony")).thenReturn(wordNetwordEntries);

        ConceptEntry listEntry = new ConceptEntry();
        listEntry.setConceptList("listName1");
        listEntry.setDeleted(true);
        listEntry.setId("L-1");

        ConceptEntry listEntry2 = new ConceptEntry();
        listEntry2.setConceptList("listName2");
        listEntry2.setId("L-2");

        conceptEntriesForList.add(listEntry);
        conceptEntriesForList.add(listEntry2);
        Mockito.when(dbManager.getAllEntriesFromList("ListName")).thenReturn(conceptEntriesForList);

        ConceptList listForWordNetConcept = new ConceptList();
        listForWordNetConcept.setConceptListName(Constants.WORDNET_DICTIONARY);

        Mockito.when(dbManager.getConceptList(Constants.WORDNET_DICTIONARY)).thenReturn(listForWordNetConcept);

        ConceptList listForConcept = new ConceptList();
        listForConcept.setConceptListName("ConceptListName");
        listForConcept.setDescription("Description");
        Mockito.when(dbManager.getConceptList("ConceptListName")).thenReturn(listForConcept);
        
        

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

    @Test
    public void testAddConceptListEntry() throws DictionaryDoesNotExistException, DictionaryModifyException {
        ConceptEntry newConcept = new ConceptEntry();
        newConcept.setConceptList("list1");
        newConcept.setCreatorId("testuser");
        newConcept.setDescription("description");
        newConcept.setPos("noun");
        newConcept.setWord("test new");
        newConcept.setWordnetId("WID-1");

        String id = managerToTest.addConceptListEntry(newConcept);
        Assert.assertNotNull(id);
    }

    @Test(expected = DictionaryDoesNotExistException.class)
    public void testAddConceptListEntryWrongDict() throws DictionaryModifyException, DictionaryDoesNotExistException {
        ConceptEntry newConcept = new ConceptEntry();
        newConcept.setConceptList("list-not-exist");
        newConcept.setCreatorId("testuser");
        newConcept.setDescription("description");
        newConcept.setPos("noun");
        newConcept.setWord("test new");
        newConcept.setWordnetId("WID-1");
        managerToTest.addConceptListEntry(newConcept);
    }

    @Test(expected = DictionaryModifyException.class)
    public void testAddConceptListEntryToWordnet() throws DictionaryDoesNotExistException, DictionaryModifyException {
        ConceptEntry newConcept = new ConceptEntry();
        newConcept.setConceptList(Constants.WORDNET_DICTIONARY);
        newConcept.setCreatorId("testuser");
        newConcept.setDescription("description");
        newConcept.setPos("noun");
        newConcept.setWord("test new");
        newConcept.setWordnetId("WID-1");
        managerToTest.addConceptListEntry(newConcept);
    }

    @Test
    public void getWordnetConceptEntryTest() {
        ConceptEntry entry = managerToTest.getWordnetConceptEntry("WordNetId");
        Assert.assertEquals(wordnetConcept2, entry);
    }

    @Test
    public void searchForConceptsConnectedByOrTest() {
        Map<String, String> fieldMap = new HashMap<String, String>();
        fieldMap.put("word", "pony");
        ConceptEntry[] entriesFromTest = managerToTest.searchForConceptsConnectedByOr(fieldMap);
        Assert.assertEquals(entriesFromTest.length, entries.length);
        Assert.assertEquals(entriesFromTest[0], entries[0]);
        Assert.assertEquals(entriesFromTest[1], entries[1]);
    }

    @Test
    public void searchForConceptsConnectedByAndTest() {
        Map<String, String> fieldMap = new HashMap<String, String>();
        fieldMap.put("word", "pony");
        ConceptEntry[] entriesFromTest = managerToTest.searchForConceptsConnectedByAnd(fieldMap);
        Assert.assertEquals(entriesFromTest[0], entries[0]);
        Assert.assertEquals(entriesFromTest[1], entries[1]);
    }

    @Test
    public void getSynonymsForConceptTest() {
        ConceptEntry[] entries = managerToTest.getSynonymsForConcept("ID");
        Assert.assertEquals(entries.length, synonymEntries.length);
    }

    @Test
    public void getConceptListEntriesForWordTest() {
        ConceptEntry[] concepts = managerToTest.getConceptListEntriesForWord("pony");
        Assert.assertEquals(concepts.length, wordEntries.length + wordNetwordEntries.length);
    }

    @Test
    public void getConceptListEntriesTest() {
        List<ConceptEntry> entries = managerToTest.getConceptListEntries("ListName");
        // Because one of the entry would be deleted since isDeleted is set to
        // true
        Assert.assertEquals(entries.size(), conceptEntriesForList.size() - 1);
    }

    @Test(expected = DictionaryDoesNotExistException.class)
    public void addConceptListEntryEmptyDictTest()
            throws DictionaryDoesNotExistException, DictionaryModifyException, DictionaryEntryExistsException {
        managerToTest.addConceptListEntry("word", "noun", "Test", "listNameForEmptyDict", "Type-1", "", "", "", "", "",
                "");
    }

    @Test(expected = DictionaryModifyException.class)
    public void addConceptListForWordNetTest()
            throws DictionaryDoesNotExistException, DictionaryModifyException, DictionaryEntryExistsException {
        managerToTest.addConceptListEntry("pony", "noun", "Test", Constants.WORDNET_DICTIONARY, "Type-1", "", "", "",
                "", "", "");
    }

    @Test
    public void addConceptListTest()
            throws DictionaryDoesNotExistException, DictionaryModifyException, DictionaryEntryExistsException {
        managerToTest.addConceptListEntry("pony", "noun", "Test", "ConceptListName", "Type-1", "", "", "", "", "", "");
        Mockito.verify(dbManager).store(Matchers.any(ConceptEntry.class), Mockito.eq(DBNames.DICTIONARY_DB));
    }
    
    @Test
    public void getConceptListEntriesForWordAndPosTest() {
        ConceptEntry[] concepts = managerToTest.getConceptListEntriesForWord("pony",POS.NOUN);
        Assert.assertEquals();
    }
    
}
