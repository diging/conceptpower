package edu.asu.conceptpower.servlet.rest;

import java.security.Principal;

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
import edu.asu.conceptpower.rest.Concepts;
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

    private Principal principal=new Principal(){public String getName(){return"admin";}};

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void addConceptWithInvalidTypeTest()
            throws IllegalAccessException, LuceneException, IndexerRunningException {

        String input = " { \"word\": \"kitty\",  \"pos\": \"noun\", \"conceptlist\": \"mylist\", \"description\": \"Soft kitty, sleepy kitty, little ball of fur.\",\"type\": \"3b755111-545a-4c1c-929c-a2c0d4c3913b\"}";
        ResponseEntity<String> response = concepts.addConcept(input, principal);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assert.assertEquals("The type id you are submitting doesn't match any existing type.", response.getBody());
    }

    @Test
    public void addConceptWithInvalidPosTest() throws IllegalAccessException, LuceneException, IndexerRunningException {
        String input = " { \"word\": \"kitty\", \"conceptlist\": \"mylist\", \"description\": \"Soft kitty, sleepy kitty, little ball of fur.\",\"type\": \"3b755111-545a-4c1c-929c-a2c0d4c3913b\"}";
        ResponseEntity<String> response = concepts.addConcept(input, principal);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assert.assertEquals("Error parsing request: please provide a POS ('pos' attribute).", response.getBody());
    }

    @Test
    public void addConceptWithInvalidWordTest()
            throws IllegalAccessException, LuceneException, IndexerRunningException {
        String input = " {\"pos\": \"noun\", \"conceptlist\": \"mylist\", \"type\": \"3b755111-545a-4c1c-929c-a2c0d4c3913b\"}";
        ResponseEntity<String> response = concepts.addConcept(input, principal);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assert.assertEquals("Error parsing request: please provide a word for the concept ('word' attribute).",
                response.getBody());
    }

    @Test
    public void addConceptWithInvalidConceptListTest()
            throws IllegalAccessException, LuceneException, IndexerRunningException {
        String input = " { \"word\": \"kitty\",  \"pos\": \"noun\", \"description\": \"Soft kitty, sleepy kitty, little ball of fur.\",\"type\": \"3b755111-545a-4c1c-929c-a2c0d4c3913b\"}";
        ResponseEntity<String> response = concepts.addConcept(input, principal);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assert.assertEquals("Error parsing request: please provide a list name for the concept ('list' attribute).",
                response.getBody());
    }

    @Test
    public void addConceptTest() throws IllegalAccessException, LuceneException, IndexerRunningException,
            DictionaryDoesNotExistException, DictionaryModifyException, JSONException {
        
        String input = " { \"word\": \"kitty\",  \"pos\": \"noun\", \"conceptlist\": \"mylist\", \"description\": \"Soft kitty, sleepy kitty, little ball of fur.\",\"type\": \"c7d0bec3-ea90-4cde-8698-3bb08c47d4f2\"}";
        ConceptType type = new ConceptType();
        type.setTypeId("c7d0bec3-ea90-4cde-8698-3bb08c47d4f2");
        type.setTypeName("Type-Name");
        type.setDescription("Type Description");

        Mockito.when(typeManager.getType("c7d0bec3-ea90-4cde-8698-3bb08c47d4f2")).thenReturn(type);
        Mockito.when(
                conceptManager.addConceptListEntry(Mockito.eq(Mockito.isA(ConceptEntry.class)),
                principal.getName()))
                .thenReturn("id-1");
        
        ResponseEntity<String> response = concepts.addConcept(input, principal);
        RestTestUtility.testValidJson(response.getBody());
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testForDictionaryException() throws IllegalAccessException, DictionaryDoesNotExistException,
            DictionaryModifyException, LuceneException, IndexerRunningException {
        String input = " { \"word\": \"kitty\",  \"pos\": \"noun\", \"conceptlist\": \"mylist\", \"description\": \"Soft kitty, sleepy kitty, little ball of fur.\",\"type\": \"c7d0bec3-ea90-4cde-8698-3bb08c47d4f2\"}";
        ConceptType type = new ConceptType();
        type.setTypeId("c7d0bec3-ea90-4cde-8698-3bb08c47d4f2");
        type.setTypeName("Type-Name");
        type.setDescription("Type Description");

        Mockito.when(typeManager.getType("c7d0bec3-ea90-4cde-8698-3bb08c47d4f2")).thenReturn(type);
        Mockito.doThrow(new DictionaryDoesNotExistException()).when(conceptManager)
                .addConceptListEntry(Mockito.isA(ConceptEntry.class), Mockito.eq(principal.getName()));

        ResponseEntity<String> response = concepts.addConcept(input, principal);
        Assert.assertEquals("Specified concept list does not exist in Conceptpower.", response.getBody());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testForDictionaryModifiedException() throws IllegalAccessException, DictionaryDoesNotExistException,
            DictionaryModifyException, LuceneException, IndexerRunningException {
        String input = " { \"word\": \"kitty\",  \"pos\": \"noun\", \"conceptlist\": \"mylist\", \"description\": \"Soft kitty, sleepy kitty, little ball of fur.\",\"type\": \"c7d0bec3-ea90-4cde-8698-3bb08c47d4f2\"}";
        ConceptType type = new ConceptType();
        type.setTypeId("c7d0bec3-ea90-4cde-8698-3bb08c47d4f2");
        type.setTypeName("Type-Name");
        type.setDescription("Type Description");

        Mockito.when(typeManager.getType("c7d0bec3-ea90-4cde-8698-3bb08c47d4f2")).thenReturn(type);
        Mockito.doThrow(new DictionaryModifyException()).when(conceptManager)
                .addConceptListEntry(Mockito.isA(ConceptEntry.class), Mockito.eq(principal.getName()));

        ResponseEntity<String> response = concepts.addConcept(input, principal);
        Assert.assertEquals("Specified concept list can't be modified.", response.getBody());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testForLuceneException() throws IllegalAccessException, DictionaryDoesNotExistException,
            DictionaryModifyException, LuceneException, IndexerRunningException {
        String input = " { \"word\": \"kitty\",  \"pos\": \"noun\", \"conceptlist\": \"mylist\", \"description\": \"Soft kitty, sleepy kitty, little ball of fur.\",\"type\": \"c7d0bec3-ea90-4cde-8698-3bb08c47d4f2\"}";
        ConceptType type = new ConceptType();
        type.setTypeId("c7d0bec3-ea90-4cde-8698-3bb08c47d4f2");
        type.setTypeName("Type-Name");
        type.setDescription("Type Description");

        Mockito.when(typeManager.getType("c7d0bec3-ea90-4cde-8698-3bb08c47d4f2")).thenReturn(type);
        Mockito.doThrow(new LuceneException()).when(conceptManager)
                .addConceptListEntry(Mockito.isA(ConceptEntry.class), Mockito.eq(principal.getName()));

        ResponseEntity<String> response = concepts.addConcept(input, principal);
        Assert.assertEquals("Concept Cannot be added", response.getBody());
        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testForIllegalAccessException() throws IllegalAccessException, DictionaryDoesNotExistException,
            DictionaryModifyException, LuceneException, IndexerRunningException {
        String input = " { \"word\": \"kitty\",  \"pos\": \"noun\", \"conceptlist\": \"mylist\", \"description\": \"Soft kitty, sleepy kitty, little ball of fur.\",\"type\": \"c7d0bec3-ea90-4cde-8698-3bb08c47d4f2\"}";
        ConceptType type = new ConceptType();
        type.setTypeId("c7d0bec3-ea90-4cde-8698-3bb08c47d4f2");
        type.setTypeName("Type-Name");
        type.setDescription("Type Description");

        Mockito.when(typeManager.getType("c7d0bec3-ea90-4cde-8698-3bb08c47d4f2")).thenReturn(type);
        Mockito.doThrow(new IllegalAccessException()).when(conceptManager)
                .addConceptListEntry(Mockito.isA(ConceptEntry.class), Mockito.eq(principal.getName()));

        ResponseEntity<String> response = concepts.addConcept(input, principal);
        Assert.assertEquals("Illegal Access", response.getBody());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testForCheckJsonObjectWrapperWithoutPos()
            throws IllegalAccessException, LuceneException, IndexerRunningException {
        String input = " { \"word\": \"kitty\",\"wordnetIds\" : \"WID-02382750-N-01-Welsh_pony\", \"conceptlist\": \"mylist\", \"description\": \"Soft kitty, sleepy kitty, little ball of fur.\"}";
        ResponseEntity<String> response = concepts.addConcept(input, principal);
        Assert.assertEquals("Error parsing request: please provide a POS ('pos' attribute).", response.getBody());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    public void testForCheckJsonObjectWrapperWithoutType()
            throws IllegalAccessException, LuceneException, IndexerRunningException {
        String wordnetId = "WID-02382750-N-01-Welsh_pony";
        String input = " { \"word\": \"kitty\",\"wordnetIds\" : \"" + wordnetId
                + "\",  \"pos\": \"noun\", \"conceptlist\": \"mylist\", \"description\": \"Soft kitty, sleepy kitty, little ball of fur.\"}";
        ResponseEntity<String> response = concepts.addConcept(input, principal);
        Assert.assertEquals("Error parsing request: please provide a type for the concept ('type' attribute).",
                response.getBody());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }
    
    @Test
    public void testForCheckJsonObjectWrapperWithInvalidType()
            throws IllegalAccessException, LuceneException, IndexerRunningException {
        String wordnetId = "WID-02382750-N-01-Welsh_pony";
        String input = " { \"word\": \"kitty\",\"wordnetIds\" : \"" + wordnetId
                + "\",  \"pos\": \"noun\", \"conceptlist\": \"mylist\", \"description\": \"Soft kitty, sleepy kitty, little ball of fur.\", \"type\": \"c7d0bec3-ea90-4cde-8698-3bb08c47d4f2\"}";

        ConceptEntry entry = new ConceptEntry();
        entry.setWord("kitty");
        entry.setId(wordnetId);
        entry.setPos("noun");

        Mockito.when(conceptManager.getConceptEntry(wordnetId)).thenReturn(entry);

        ResponseEntity<String> response = concepts.addConcept(input, principal);
        Assert.assertEquals("The type id you are submitting doesn't match any existing type.", response.getBody());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    public void testForCheckJsonObjectWrapperWithInvalidWordnet()
            throws IllegalAccessException, LuceneException, IndexerRunningException {
        String wordnetId = "WI-02382750-N-01-Welsh_pony";
        String input = " { \"word\": \"kitty\", \"wordnetIds\" : \"" + wordnetId
                + "\",  \"pos\": \"noun\", \"conceptlist\": \"mylist\", \"description\": \"Soft kitty, sleepy kitty, little ball of fur.\", \"type\": \"c7d0bec3-ea90-4cde-8698-3bb08c47d4f2\"}";

        ConceptType type = new ConceptType();
        type.setTypeId("c7d0bec3-ea90-4cde-8698-3bb08c47d4f2");
        type.setTypeName("Type-Name");
        type.setDescription("Type Description");

        Mockito.when(typeManager.getType("c7d0bec3-ea90-4cde-8698-3bb08c47d4f2")).thenReturn(type);

        ResponseEntity<String> response = concepts.addConcept(input, principal);
        Assert.assertEquals(
                "Error parsing request: please provide a valid list of wordnet ids seperated by commas. Wordnet id "
                        + wordnetId + " doesn't exist.",
                response.getBody());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    public void testCheckForPosWithEntry() throws IllegalAccessException, LuceneException, IndexerRunningException {

        String wordnetId = "WID-02382750-N-01-Welsh_pony";
        String input = " { \"word\": \"kitty\",\"wordnetIds\" : \"" + wordnetId
                + "\",  \"pos\": \"noun\", \"conceptlist\": \"mylist\", \"description\": \"Soft kitty, sleepy kitty, little ball of fur.\", \"type\": \"c7d0bec3-ea90-4cde-8698-3bb08c47d4f2\"}";

        ConceptEntry entry = new ConceptEntry();
        entry.setWord("kitty");
        entry.setId(wordnetId);
        entry.setPos("verb");

        Mockito.when(conceptManager.getConceptEntry(wordnetId)).thenReturn(entry);

        ResponseEntity<String> response = concepts.addConcept(input, principal);
        Assert.assertEquals(
                "Error parsing request: please enter POS that matches with the wordnet POS " + entry.getPos(),
                response.getBody());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    public void testForExistingWordnetID() throws IllegalAccessException, LuceneException, IndexerRunningException {
        String wordnetId = "WID-02382750-N-01-Welsh_pony";
        String input = " { \"word\": \"kitty\",\"wordnetIds\" : \"" + wordnetId
                + "\",  \"pos\": \"noun\", \"conceptlist\": \"mylist\", \"description\": \"Soft kitty, sleepy kitty, little ball of fur.\", \"type\": \"c7d0bec3-ea90-4cde-8698-3bb08c47d4f2\"}";

        ConceptEntry entry = new ConceptEntry();
        entry.setWord("kitty");
        entry.setId(wordnetId);
        entry.setPos("verb");
        
        Mockito.when(conceptManager.getConceptWrappedEntryByWordNetId(wordnetId)).thenReturn(entry);

        ResponseEntity<String> response = concepts.addConcept(input, principal);
        Assert.assertEquals("Error parsing request: the WordNet concept you are trying to wrap is already wrapped.",
                response.getBody());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void addConceptsTest() throws IllegalAccessException, LuceneException, IndexerRunningException,
            DictionaryDoesNotExistException, DictionaryModifyException, JSONException {
        
        String input = "[{ \"word\": \"kitty\",  \"pos\": \"noun\", \"conceptlist\": \"mylist\", \"description\": \"Soft kitty, sleepy kitty, little ball of fur.\",\"type\": \"c7d0bec3-ea90-4cde-8698-3bb08c47d4f2\"},{ \"word\": \"pony\",  \"pos\": \"noun\", \"conceptlist\": \"mylist\", \"description\": \"Indian pony.\",\"type\": \"c7d0bec3-ea90-4cde-8698-3bb08c47d4r4\"}]";
        
        ConceptType type = new ConceptType();
        type.setTypeId("c7d0bec3-ea90-4cde-8698-3bb08c47d4f2");
        type.setTypeName("Type-Name");
        type.setDescription("Type Description");

        ConceptType type2 = new ConceptType();
        type2.setTypeId("c7d0bec3-ea90-4cde-8698-3bb08c47d4r4");
        type.setTypeName("Test-List");
        type.setDescription("Pony types");

        Mockito.when(typeManager.getType("c7d0bec3-ea90-4cde-8698-3bb08c47d4f2")).thenReturn(type);
        Mockito.when(typeManager.getType("c7d0bec3-ea90-4cde-8698-3bb08c47d4r4")).thenReturn(type2);

        ResponseEntity<String> output = concepts.addConcepts(input, principal);

        Mockito.verify(conceptManager, Mockito.times(2)).addConceptListEntry(Mockito.isA(ConceptEntry.class),
                Mockito.eq(principal.getName()));

        RestTestUtility.testValidJson(output.getBody());
        Assert.assertEquals(HttpStatus.OK, output.getStatusCode());
    }
}
