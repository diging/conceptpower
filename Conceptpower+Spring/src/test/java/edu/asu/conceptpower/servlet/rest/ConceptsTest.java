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
}
