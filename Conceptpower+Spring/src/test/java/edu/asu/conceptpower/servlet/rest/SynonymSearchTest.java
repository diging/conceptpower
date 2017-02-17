package edu.asu.conceptpower.servlet.rest;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;

import org.codehaus.jettison.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.core.JsonProcessingException;

import edu.asu.conceptpower.app.core.IConceptManager;
import edu.asu.conceptpower.app.db.TypeDatabaseClient;
import edu.asu.conceptpower.app.exceptions.LuceneException;
import edu.asu.conceptpower.app.util.URIHelper;
import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptType;
import edu.asu.conceptpower.rest.SynonymSearch;
import edu.asu.conceptpower.rest.msg.IMessageConverter;
import edu.asu.conceptpower.rest.msg.IMessageRegistry;
import edu.asu.conceptpower.rest.msg.json.JsonConceptMessage;
import edu.asu.conceptpower.rest.msg.xml.XMLConceptMessage;
import junit.framework.Assert;

public class SynonymSearchTest {

    @Mock
    private IConceptManager dictManager;

    @Mock
    private TypeDatabaseClient typeManager;

    @Mock
    private IMessageRegistry messageFactory;

    @InjectMocks
    private SynonymSearch synonymSearch;

    @Mock
    private IMessageConverter xmlMessageFactory;

    @Mock
    private URIHelper uriCreator;

    @Mock(name = "jsonMessageFactory")
    private IMessageConverter jsonMessageFactory;

    private String synonymId = "WID-02084071-N-02-domestic_dog";

    @Before
    public void init() throws LuceneException {
        MockitoAnnotations.initMocks(this);
        Mockito.when(messageFactory.getMessageFactory(MediaType.APPLICATION_XML_VALUE)).thenReturn(xmlMessageFactory);
        Mockito.when(messageFactory.getMessageFactory(MediaType.APPLICATION_XML_VALUE).createConceptMessage())
                .thenReturn(new XMLConceptMessage(uriCreator));

        Mockito.when(messageFactory.getMessageFactory(MediaType.APPLICATION_JSON_VALUE)).thenReturn(jsonMessageFactory);
        Mockito.when(messageFactory.getMessageFactory(MediaType.APPLICATION_JSON_VALUE).createConceptMessage())
                .thenReturn(new JsonConceptMessage(uriCreator));

        ConceptEntry[] entries = new ConceptEntry[1];
        ConceptEntry entry = new ConceptEntry();
        entry.setId("WID-02084071-N-02-domestic_dog");
        entry.setWord("pony");
        entry.setTypeId("TYPE-123");
        entries[0] = entry;

        ConceptType conceptType = new ConceptType();
        conceptType.setTypeId("TYPE-123");
        conceptType.setCreatorId("admin");
        conceptType.setDescription("Concept Type");

        Mockito.when(dictManager.getSynonymsForConcept(synonymId)).thenReturn(entries);
        Mockito.when(typeManager.getType("TYPE-123")).thenReturn(conceptType);

    }

    @Test
    public void getSynonymsForInvalidId() throws JsonProcessingException {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        Mockito.when(req.getParameter("id")).thenReturn(null);
        ResponseEntity<String> output = synonymSearch.getSynonymsForId(req, MediaType.APPLICATION_XML_VALUE);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, output.getStatusCode());
    }

    @Test
    public void getSynonymForIdInXml() throws ParserConfigurationException, SAXException, IOException {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        Mockito.when(req.getParameter("id")).thenReturn("http://www.digitalhps.org/concepts/" + synonymId);
        ResponseEntity<String> output = synonymSearch.getSynonymsForId(req, MediaType.APPLICATION_XML_VALUE);
        RestTestUtility.testValidXml(output.getBody());
        Assert.assertEquals(HttpStatus.OK, output.getStatusCode());
    }

    @Test
    public void getSynonymForIdInJson() throws JsonProcessingException, JSONException {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        Mockito.when(req.getParameter("id")).thenReturn("http://www.digitalhps.org/concepts/" + synonymId);
        ResponseEntity<String> output = synonymSearch.getSynonymsForId(req, MediaType.APPLICATION_JSON_VALUE);
        RestTestUtility.testValidJson(output.getBody());
        Assert.assertEquals(HttpStatus.OK, output.getStatusCode());
    }

}
