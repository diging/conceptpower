package edu.asu.conceptpower.servlet.rest;

import java.security.Principal;

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

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    public void addConceptWithInvalidTypeTest()
            throws IllegalAccessException, LuceneException, IndexerRunningException {

        String input = " { \"word\": \"kitty\",  \"pos\": \"noun\", \"conceptlist\": \"mylist\", \"description\": \"Soft kitty, sleepy kitty, little ball of fur.\",\"type\": \"3b755111-545a-4c1c-929c-a2c0d4c3913b\"}";
        Principal principal = new Principal() {
            public String getName() {
                return "admin";
            }
        };

        ResponseEntity<String> response = concepts.addConcept(input, principal);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assert.assertEquals("The type id you are submitting doesn't match any existing type.", response.getBody());
    }

    @Test
    public void addConceptTest() throws IllegalAccessException, LuceneException, IndexerRunningException,
            DictionaryDoesNotExistException, DictionaryModifyException {
        
        String input = " { \"word\": \"kitty\",  \"pos\": \"noun\", \"conceptlist\": \"mylist\", \"description\": \"Soft kitty, sleepy kitty, little ball of fur.\",\"type\": \"c7d0bec3-ea90-4cde-8698-3bb08c47d4f2\"}";
        Principal principal = new Principal() {
            public String getName() {
                return "admin";
            }
        };
        
        ConceptType type = new ConceptType();
        type.setTypeId("c7d0bec3-ea90-4cde-8698-3bb08c47d4f2");
        type.setTypeName("Type-Name");
        type.setDescription("Type Description");

        Mockito.when(typeManager.getType("c7d0bec3-ea90-4cde-8698-3bb08c47d4f2")).thenReturn(type);
        Mockito.when(conceptManager.addConceptListEntry(Mockito.argThat(Mockito.isA(ConceptEntry.class)),
                principal.getName()))
                .thenReturn("id-1");
        
        ResponseEntity<String> response = concepts.addConcept(input, principal);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        System.out.println(response.getBody());
    }
}
