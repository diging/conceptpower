package edu.asu.conceptpower.servlet.rest;

import java.io.IOException;

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

import edu.asu.conceptpower.app.core.Constants;
import edu.asu.conceptpower.app.core.IConceptManager;
import edu.asu.conceptpower.app.core.POS;
import edu.asu.conceptpower.app.db.TypeDatabaseClient;
import edu.asu.conceptpower.app.exceptions.IndexerRunningException;
import edu.asu.conceptpower.app.exceptions.LuceneException;
import edu.asu.conceptpower.app.rdf.RDFMessageFactory;
import edu.asu.conceptpower.app.util.URIHelper;
import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptType;
import edu.asu.conceptpower.rest.ConceptLookup;
import edu.asu.conceptpower.rest.msg.IMessageConverter;
import edu.asu.conceptpower.rest.msg.IMessageRegistry;
import edu.asu.conceptpower.rest.msg.json.JsonConceptMessage;
import edu.asu.conceptpower.rest.msg.xml.XMLConceptMessage;
import junit.framework.Assert;

public class ConceptLookupTest {

    @Mock
    private IConceptManager dictManager;

    @Mock
    private TypeDatabaseClient typeManager;

    @Mock
    private IMessageRegistry messageFactory;

    @InjectMocks
    private ConceptLookup conceptLookup;

    @Mock
    private RDFMessageFactory rdfFactory;

    @Mock
    private IMessageConverter xmlMEssageFactory;

    @Mock(name = "jsonMessageFactory")
    private IMessageConverter jsonMessageFactory;

    @Mock
    private URIHelper uriCreator;

    private String PONY = "pony";

    @Before
    public void setup() throws IOException, IllegalAccessException, LuceneException, IndexerRunningException {
        MockitoAnnotations.initMocks(this);
        String pos = POS.NOUN;
        String typeId1 = "TYPE-1";
        String typeId2 = "TYPE-2";

        ConceptEntry[] entries = new ConceptEntry[2];
        ConceptEntry entry = new ConceptEntry();
        entry.setWord(PONY);
        entry.setPos(pos);
        entry.setId("WID-123");
        entry.setConceptList(Constants.WORDNET_DICTIONARY);
        entry.setTypeId(typeId1);
        entries[0] = entry;

        ConceptEntry entry2 = new ConceptEntry();
        entry2.setWord(PONY);
        entry2.setPos(pos);
        entry2.setConceptList("Test List");
        entry2.setTypeId(typeId1);
        entries[1] = entry2;

        ConceptType type1 = new ConceptType();
        type1.setTypeId(typeId1);
        type1.setDescription("TypeID 1");
        type1.setTypeName("TYPE-1");

        ConceptType type2 = new ConceptType();
        type2.setTypeId(typeId2);
        type1.setDescription(typeId2);
        type1.setTypeName("TYPE-2");

        Mockito.when(dictManager.getConceptListEntriesForWordPOS(PONY, POS.NOUN, null)).thenReturn(entries);
        Mockito.when(typeManager.getType(typeId1)).thenReturn(type1);
        Mockito.when(typeManager.getType(typeId2)).thenReturn(type2);
        Mockito.when(messageFactory.getMessageFactory(MediaType.APPLICATION_XML_VALUE)).thenReturn(xmlMEssageFactory);
        Mockito.when(messageFactory.getMessageFactory(MediaType.APPLICATION_JSON_VALUE)).thenReturn(jsonMessageFactory);

        Mockito.when(xmlMEssageFactory.createConceptMessage()).thenReturn(new XMLConceptMessage(uriCreator));
        Mockito.when(jsonMessageFactory.createConceptMessage()).thenReturn(new JsonConceptMessage(uriCreator));
    }

    public void test_getWordNetEntry_successINXML()
            throws IndexerRunningException, ParserConfigurationException, SAXException, IOException {
        ResponseEntity<String> response = conceptLookup.getWordNetEntry(PONY, POS.NOUN,
                MediaType.APPLICATION_XML_VALUE);
        RestTestUtility.testValidXml(response.getBody());
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void test_getWordNetEntry_emptyResult() throws IndexerRunningException, ParserConfigurationException,
            SAXException, IOException, IllegalAccessException, LuceneException {
        String noResult = "noResult";
        Mockito.when(dictManager.getConceptListEntriesForWordPOS(PONY, POS.NOUN, null)).thenReturn(null);
        ResponseEntity<String> response = conceptLookup.getWordNetEntry(noResult, POS.NOUN,
                MediaType.APPLICATION_XML_VALUE);
        Assert.assertEquals("No search results.", response.getBody());
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void test_getWordNetEntryTest_successInJSON()
            throws IndexerRunningException, ParserConfigurationException, SAXException, IOException, JSONException {

        ResponseEntity<String> response = conceptLookup.getWordNetEntry(PONY, POS.NOUN,
                MediaType.APPLICATION_JSON_VALUE);
        RestTestUtility.testValidJson(response.getBody());
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void test_getWordNetEntry_successInRDF()
            throws IndexerRunningException, IllegalAccessException, LuceneException {

        String ROBERT = "robert";
        String ROBERT_1 = "robert-1";
        
        ConceptEntry[] entries = new ConceptEntry[2];
        ConceptEntry entry1 = new ConceptEntry();
        entry1.setWord(ROBERT);
        entry1.setPos(POS.NOUN);
        entry1.setId("WID-123");

        ConceptEntry entry2 = new ConceptEntry();
        entry2.setId("WID-456");
        entry2.setWord(ROBERT_1);
        entry1.setPos(POS.NOUN);

        entries[0] = entry1;
        entries[1] = entry2;

        Mockito.when(dictManager.getConceptListEntriesForWordPOS(ROBERT, POS.NOUN, null)).thenReturn(entries);
        ResponseEntity<String> response = conceptLookup.getWordNetEntryInRdf(ROBERT, POS.NOUN);
        Mockito.verify(rdfFactory).generateRDF(entries);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());

    }

}
