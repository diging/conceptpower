package edu.asu.conceptpower.servlet.rest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.root.TypeDatabaseClient;
import edu.asu.conceptpower.root.URIHelper;
import edu.asu.conceptpower.servlet.core.ConceptTypesService;
import edu.asu.conceptpower.servlet.core.ConceptTypesService.ConceptTypes;
import edu.asu.conceptpower.servlet.core.IConceptManager;
import edu.asu.conceptpower.servlet.xml.XMLConceptMessage;
import edu.asu.conceptpower.servlet.xml.XMLMessageFactory;
import junit.framework.Assert;

public class ConceptIDLookupTest {

    @Mock
    private IConceptManager dictManager;

    @Mock
    private TypeDatabaseClient typeManager;

    @Mock
    private XMLMessageFactory messageFactory;

    @Mock
    private IConceptManager conceptManager;

    @Mock
    private ConceptTypesService conceptTypesService;

    @Mock
    private URIHelper uriCreator;

    @InjectMocks
    private ConceptIDLookup conceptIDLookup;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(messageFactory.createXMLConceptMessage()).thenReturn(new XMLConceptMessage(uriCreator));
    }

    @Test
    public void testGetConceptByIdForAlternativeIDs() throws Exception {
        ConceptEntry entry = new ConceptEntry();
        entry.setId("CONf375adff-dde7-4536-9e62-f80328f800d0");
        entry.setWordnetId("W-ID1, W-ID2");
        Mockito.when(dictManager.getConceptEntry("CONf375adff-dde7-4536-9e62-f80328f800d0")).thenReturn(entry);
        ResponseEntity<String> response = conceptIDLookup
                .getConceptById("http://www.digitalhps.org/concepts/CONf375adff-dde7-4536-9e62-f80328f800d0");
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals(true, response.getBody().contains("W-ID1"));
        Assert.assertEquals(true, response.getBody().contains("W-ID2"));

    }

    @Test
    public void testGetConcepyByIdWithGenericIDs() throws Exception {
        ConceptEntry entry = new ConceptEntry();
        entry.setId("W-ID01");
        entry.setWordnetId("W-ID01, W-ID02");
        Mockito.when(dictManager.getConceptEntry("W-ID??")).thenReturn(entry);
        Mockito.when(conceptTypesService.getConceptTypeByConceptId("W-ID??"))
                .thenReturn(ConceptTypes.GENERIC_WORDNET_CONCEPT);

        ConceptEntry newEntry = new ConceptEntry();

        newEntry.setId("CONf375adff-dde7-4536-9e62-f80328f800d0");
        newEntry.setTypeId("T-123");
        newEntry.setDescription("Description");
        newEntry.setSynonymIds("SYN-01");
        newEntry.setEqualTo("equals");
        newEntry.setSimilarTo("similar");
        newEntry.setPos("NOUN");
        newEntry.setWord("pony");
        newEntry.setWordnetId("W-ID01, W-ID02");

        Mockito.when(conceptManager.getConceptEntry("W-ID01")).thenReturn(newEntry);

        ResponseEntity<String> response = conceptIDLookup.getConceptById("W-ID??");

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals(false, response.getBody().contains("W-ID1"));
        Assert.assertEquals(false, response.getBody().contains("W-ID2"));
        Assert.assertEquals(true, response.getBody().contains("CONf375adff-dde7-4536-9e62-f80328f800d0"));

    }
}
