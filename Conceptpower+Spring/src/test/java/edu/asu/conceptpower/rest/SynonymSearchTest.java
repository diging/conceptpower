package edu.asu.conceptpower.rest;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtil;
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

    private final String synonymId = "WID-02084071-N-02-domestic_dog";

    @Before
    public void setup() throws LuceneException {
        MockitoAnnotations.initMocks(this);
        Mockito.when(messageFactory.getMessageFactory(MediaType.APPLICATION_XML_VALUE)).thenReturn(xmlMessageFactory);
        Mockito.when(messageFactory.getMessageFactory(MediaType.APPLICATION_XML_VALUE).createConceptMessage())
                .thenReturn(new XMLConceptMessage(uriCreator));

        Mockito.when(messageFactory.getMessageFactory(MediaType.APPLICATION_JSON_VALUE)).thenReturn(jsonMessageFactory);
        Mockito.when(messageFactory.getMessageFactory(MediaType.APPLICATION_JSON_VALUE).createConceptMessage())
                .thenReturn(new JsonConceptMessage(uriCreator));

        ConceptEntry[] entries = new ConceptEntry[1];
        ConceptEntry entry = new ConceptEntry();
        entry.setId(synonymId);
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
    public void test_getSynonymsForId_invalidId() throws JsonProcessingException {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        Mockito.when(req.getParameter("id")).thenReturn(null);
        ResponseEntity<String> output = synonymSearch.getSynonymsForId(req, MediaType.APPLICATION_XML_VALUE);
        Assert.assertEquals(null, output.getBody());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, output.getStatusCode());
    }

    @Test
    public void test_getSynonymsForId_nonExistingId() throws JsonProcessingException, LuceneException {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        final String wordnet = "WID-PONY-123";
        Mockito.when(req.getParameter("id")).thenReturn("http://www.digitalhps.org/concepts/" + wordnet);
        Mockito.when(dictManager.getSynonymsForConcept(wordnet))
                .thenReturn(Collections.<ConceptEntry> emptyList().toArray(new ConceptEntry[0]));
        ResponseEntity<String> output = synonymSearch.getSynonymsForId(req, MediaType.APPLICATION_XML_VALUE);
        Assert.assertEquals(null, output.getBody());
        Assert.assertEquals(HttpStatus.OK, output.getStatusCode());
    }

    @Test
    public void test_getSynonymsForId_successInXml() throws ParserConfigurationException, SAXException, IOException {
        final String expectedOutput = IOUtil
                .toString(this.getClass().getClassLoader().getResourceAsStream("unitTestOutput/synonym.xml"));
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        Mockito.when(req.getParameter("id")).thenReturn("http://www.digitalhps.org/concepts/" + synonymId);
        ResponseEntity<String> output = synonymSearch.getSynonymsForId(req, MediaType.APPLICATION_XML_VALUE);
        RestTestUtility.testValidXml(output.getBody());
        Assert.assertEquals(expectedOutput, output.getBody());
        Assert.assertEquals(HttpStatus.OK, output.getStatusCode());
    }

    @Test
    public void test_getSynonymsForId_successInJson() throws JSONException, IOException {
        final String expectedOutput = IOUtil
                .toString(this.getClass().getClassLoader().getResourceAsStream("unitTestOutput/synonym.json"));
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        Mockito.when(req.getParameter("id")).thenReturn("http://www.digitalhps.org/concepts/" + synonymId);
        ResponseEntity<String> output = synonymSearch.getSynonymsForId(req, MediaType.APPLICATION_JSON_VALUE);
        RestTestUtility.testValidJson(output.getBody());
        Assert.assertEquals(expectedOutput, output.getBody());
        Assert.assertEquals(HttpStatus.OK, output.getStatusCode());
    }

}
