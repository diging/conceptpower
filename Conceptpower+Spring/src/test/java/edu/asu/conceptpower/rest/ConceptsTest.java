package edu.asu.conceptpower.rest;

import java.io.IOException;
import java.security.Principal;

import org.apache.commons.io.IOUtil;
import org.codehaus.jettison.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import edu.asu.conceptpower.app.core.IConceptManager;
import edu.asu.conceptpower.app.core.IConceptTypeManger;
import edu.asu.conceptpower.app.exceptions.DictionaryDoesNotExistException;
import edu.asu.conceptpower.app.exceptions.DictionaryModifyException;
import edu.asu.conceptpower.app.exceptions.IndexerRunningException;
import edu.asu.conceptpower.app.exceptions.LuceneException;
import edu.asu.conceptpower.app.util.URIHelper;
import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptType;
import junit.framework.Assert;

public class ConceptsTest {

    @Mock
    private IConceptManager conceptManager;

    @Mock
    private IConceptTypeManger typeManager;

    @Mock
    private URIHelper uriHelper;

    @InjectMocks
    private Concepts concepts;

    private Principal principal = new Principal() {
        public String getName() {
            return "admin";
        }
    };

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_addConcept_invalidType()
            throws IllegalAccessException, LuceneException, IndexerRunningException, IOException {
        final String input = IOUtil
                .toString(this.getClass().getClassLoader().getResourceAsStream("input/invalidType.json"));
        ResponseEntity<String> response = concepts.addConcept(input, principal);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assert.assertEquals("The type id you are submitting doesn't match any existing type.", response.getBody());
    }

    @Test
    public void test_addConcept_invalidPos()
            throws IllegalAccessException, LuceneException, IndexerRunningException, IOException {
        final String input = IOUtil
                .toString(this.getClass().getClassLoader().getResourceAsStream("input/invalidPOS.json"));
        ResponseEntity<String> response = concepts.addConcept(input, principal);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assert.assertEquals("Error parsing request: please provide a POS ('pos' attribute).", response.getBody());
    }

    @Test
    public void test_addConcept_emptyWord()
            throws IllegalAccessException, LuceneException, IndexerRunningException, IOException {
        final String input = IOUtil
                .toString(this.getClass().getClassLoader().getResourceAsStream("input/emptyWord.json"));
        ResponseEntity<String> response = concepts.addConcept(input, principal);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assert.assertEquals("Error parsing request: please provide a word for the concept ('word' attribute).",
                response.getBody());
    }

    @Test
    public void test_addConcept_invalidListName()
            throws IllegalAccessException, LuceneException, IndexerRunningException, IOException {
        final String input = IOUtil
                .toString(this.getClass().getClassLoader().getResourceAsStream("input/conceptWithoutListName.json"));
        ResponseEntity<String> response = concepts.addConcept(input, principal);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assert.assertEquals("Error parsing request: please provide a list name for the concept ('list' attribute).",
                response.getBody());
    }

    @Test
    public void test_addConcept_successInJson() throws IllegalAccessException, LuceneException, IndexerRunningException,
            DictionaryDoesNotExistException, DictionaryModifyException, JSONException, IOException {
        final String input = IOUtil
                .toString(this.getClass().getClassLoader().getResourceAsStream("input/addConcept.json"));

        final String expectedOutput = IOUtil
                .toString(this.getClass().getClassLoader().getResourceAsStream("output/addConcept.json"));

        final String typeId = "c7d0bec3-ea90-4cde-8698-3bb08c47d4f2";
        ConceptType type = new ConceptType();
        type.setTypeId(typeId);
        type.setTypeName("Type-Name");
        type.setDescription("Type Description");

        Mockito.when(typeManager.getType(typeId)).thenReturn(type);
        Mockito.when(
                conceptManager.addConceptListEntry(Mockito.eq(Mockito.isA(ConceptEntry.class)), principal.getName()))
                .thenReturn("id-1");

        ResponseEntity<String> response = concepts.addConcept(input, principal);
        RestTestUtility.testValidJson(response.getBody());
        Assert.assertEquals(expectedOutput, response.getBody());
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void test_addConcept_invalidListWithDictionaryException()
            throws IllegalAccessException, DictionaryDoesNotExistException, DictionaryModifyException, LuceneException,
            IndexerRunningException, IOException {
        final String input = IOUtil.toString(
                this.getClass().getClassLoader().getResourceAsStream("input/conceptWithInvalidListName.json"));
        final String typeId = "c7d0bec3-ea90-4cde-8698-3bb08c47d4f2";
        ConceptType type = new ConceptType();
        type.setTypeId(typeId);
        type.setTypeName("Type-Name");
        type.setDescription("Type Description");

        Mockito.when(typeManager.getType(typeId)).thenReturn(type);
        Mockito.doThrow(new DictionaryDoesNotExistException()).when(conceptManager)
                .addConceptListEntry(Mockito.isA(ConceptEntry.class), Mockito.eq(principal.getName()));

        ResponseEntity<String> response = concepts.addConcept(input, principal);
        Assert.assertEquals("Specified concept list does not exist in Conceptpower.", response.getBody());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void test_addConcept_dictionaryModifiedException()
            throws IllegalAccessException, DictionaryDoesNotExistException, DictionaryModifyException, LuceneException,
            IndexerRunningException, IOException {
        final String input = IOUtil.toString(
                this.getClass().getClassLoader().getResourceAsStream("input/conceptWithInvalidListName.json"));
        final String typeId = "c7d0bec3-ea90-4cde-8698-3bb08c47d4f2";
        ConceptType type = new ConceptType();
        type.setTypeId(typeId);
        type.setTypeName("Type-Name");
        type.setDescription("Type Description");

        Mockito.when(typeManager.getType(typeId)).thenReturn(type);
        Mockito.doThrow(new DictionaryModifyException()).when(conceptManager)
                .addConceptListEntry(Mockito.isA(ConceptEntry.class), Mockito.eq(principal.getName()));

        ResponseEntity<String> response = concepts.addConcept(input, principal);
        Assert.assertEquals("Specified concept list can't be modified.", response.getBody());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void test_addConcept_luceneException() throws IllegalAccessException, DictionaryDoesNotExistException,
            DictionaryModifyException, LuceneException, IndexerRunningException, IOException {
        final String input = IOUtil.toString(
                this.getClass().getClassLoader().getResourceAsStream("input/conceptWithInvalidListName.json"));
        final String typeId = "c7d0bec3-ea90-4cde-8698-3bb08c47d4f2";
        ConceptType type = new ConceptType();
        type.setTypeId(typeId);
        type.setTypeName("Type-Name");
        type.setDescription("Type Description");

        Mockito.when(typeManager.getType(typeId)).thenReturn(type);
        Mockito.doThrow(new LuceneException()).when(conceptManager).addConceptListEntry(Mockito.isA(ConceptEntry.class),
                Mockito.eq(principal.getName()));

        ResponseEntity<String> response = concepts.addConcept(input, principal);
        Assert.assertEquals("Concept Cannot be added", response.getBody());
        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void test_addConcept_illegalAccessException() throws IllegalAccessException, DictionaryDoesNotExistException,
            DictionaryModifyException, LuceneException, IndexerRunningException, IOException {
        final String input = IOUtil.toString(
                this.getClass().getClassLoader().getResourceAsStream("input/conceptWithInvalidListName.json"));
        final String typeId = "c7d0bec3-ea90-4cde-8698-3bb08c47d4f2";
        ConceptType type = new ConceptType();
        type.setTypeId(typeId);
        type.setTypeName("Type-Name");
        type.setDescription("Type Description");

        Mockito.when(typeManager.getType(typeId)).thenReturn(type);
        Mockito.doThrow(new IllegalAccessException()).when(conceptManager)
                .addConceptListEntry(Mockito.isA(ConceptEntry.class), Mockito.eq(principal.getName()));

        ResponseEntity<String> response = concepts.addConcept(input, principal);
        Assert.assertEquals("Illegal Access", response.getBody());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void test_addConcept_emptyPos()
            throws IllegalAccessException, LuceneException, IndexerRunningException, IOException {
        final String input = IOUtil.toString(
                this.getClass().getClassLoader().getResourceAsStream("input/conceptWithWordnetAndInvalidPos.json"));
        ResponseEntity<String> response = concepts.addConcept(input, principal);
        Assert.assertEquals("Error parsing request: please provide a POS ('pos' attribute).", response.getBody());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    public void test_addConcept_invalidConceptType()
            throws IllegalAccessException, LuceneException, IndexerRunningException, IOException {
        final String input = IOUtil
                .toString(this.getClass().getClassLoader().getResourceAsStream("input/conceptWithEmptyTypeId.json"));
        ResponseEntity<String> response = concepts.addConcept(input, principal);
        Assert.assertEquals("Error parsing request: please provide a type for the concept ('type' attribute).",
                response.getBody());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    public void test_addConcept_noMatchingConceptType()
            throws IllegalAccessException, LuceneException, IndexerRunningException, IOException {
        final String wordnetId = "WID-02382750-N-01-Welsh_pony";
        final String word = "kitty";
        final String pos = "noun";

        final String input = IOUtil.toString(
                this.getClass().getClassLoader().getResourceAsStream("input/conceptWithNonExistingTypeId.json"));

        ConceptEntry entry = new ConceptEntry();
        entry.setWord(word);
        entry.setId(wordnetId);
        entry.setPos(pos);

        Mockito.when(conceptManager.getConceptEntry(wordnetId)).thenReturn(entry);

        ResponseEntity<String> response = concepts.addConcept(input, principal);
        Assert.assertEquals("The type id you are submitting doesn't match any existing type.", response.getBody());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    public void test_addConcept_invalidWordNetIds()
            throws IllegalAccessException, LuceneException, IndexerRunningException, IOException {
        final String input = IOUtil.toString(
                this.getClass().getClassLoader().getResourceAsStream("input/conceptWithInvalidWordnetId.json"));

        ConceptType type = new ConceptType();
        type.setTypeId("c7d0bec3-ea90-4cde-8698-3bb08c47d4f2");
        type.setTypeName("Type-Name");
        type.setDescription("Type Description");

        Mockito.when(typeManager.getType("c7d0bec3-ea90-4cde-8698-3bb08c47d4f2")).thenReturn(type);

        ResponseEntity<String> response = concepts.addConcept(input, principal);
        Assert.assertEquals(
                "Error parsing request: please provide a valid list of wordnet ids seperated by commas. Wordnet id WI-02382750-N-01-Welsh_pony doesn't exist.",
                response.getBody());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    public void test_addConcept_posMismatch()
            throws IllegalAccessException, LuceneException, IndexerRunningException, IOException {
        final String wordnetId = "WID-02382750-N-01-Welsh_pony";
        final String word = "kitty";
        final String pos = "verb";
        final String input = IOUtil.toString(
                this.getClass().getClassLoader().getResourceAsStream("input/conceptWrapperWithmismatchPOS.json"));

        ConceptEntry entry = new ConceptEntry();
        entry.setWord(word);
        entry.setId(wordnetId);
        entry.setPos(pos);

        Mockito.when(conceptManager.getConceptEntry(wordnetId)).thenReturn(entry);

        ResponseEntity<String> response = concepts.addConcept(input, principal);
        Assert.assertEquals(
                "Error parsing request: please enter POS that matches with the wordnet POS " + entry.getPos(),
                response.getBody());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    public void test_addConcept_existingWordnetIds()
            throws IllegalAccessException, LuceneException, IndexerRunningException, IOException {
        final String wordnetId = "WID-02382750-N-01-Welsh_pony";
        final String word = "kitty";
        final String pos = "verb";
        final String input = IOUtil
                .toString(this.getClass().getClassLoader().getResourceAsStream("input/existingWordnetIdsInput.json"));

        ConceptEntry entry = new ConceptEntry();
        entry.setWord(word);
        entry.setId(wordnetId);
        entry.setPos(pos);

        Mockito.when(conceptManager.getConceptWrappedEntryByWordNetId(wordnetId)).thenReturn(entry);

        ResponseEntity<String> response = concepts.addConcept(input, principal);
        Assert.assertEquals("Error parsing request: the WordNet concept you are trying to wrap is already wrapped.",
                response.getBody());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void test_addConcepts_successInJsonWithMultipleTypes()
            throws IllegalAccessException, LuceneException, IndexerRunningException, DictionaryDoesNotExistException,
            DictionaryModifyException, JSONException, IOException {

        final String input = IOUtil.toString(
                this.getClass().getClassLoader().getResourceAsStream("input/multipleConceptMultipleTypes.json"));

        final String expectedOutput = IOUtil.toString(
                this.getClass().getClassLoader().getResourceAsStream("output/multipleConceptMultipleTypes.json"));

        final String typeId1 = "c7d0bec3-ea90-4cde-8698-3bb08c47d4f2";
        final String typeId2 = "c7d0bec3-ea90-4cde-8698-3bb08c47d4r4";

        ConceptType type = new ConceptType();
        type.setTypeId(typeId1);
        type.setTypeName("Type-Name");
        type.setDescription("Type Description");

        ConceptType type2 = new ConceptType();
        type2.setTypeId(typeId2);
        type.setTypeName("Test-List");
        type.setDescription("Pony types");

        Mockito.when(typeManager.getType(typeId1)).thenReturn(type);
        Mockito.when(typeManager.getType(typeId2)).thenReturn(type2);

        ResponseEntity<String> output = concepts.addConcepts(input, principal);

        Mockito.verify(conceptManager, Mockito.times(2)).addConceptListEntry(Mockito.isA(ConceptEntry.class),
                Mockito.eq(principal.getName()));

        RestTestUtility.testValidJson(output.getBody());
        Assert.assertEquals(expectedOutput, output.getBody());
        Assert.assertEquals(HttpStatus.OK, output.getStatusCode());
    }
}
