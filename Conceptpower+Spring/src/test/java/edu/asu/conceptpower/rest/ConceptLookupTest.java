package edu.asu.conceptpower.rest;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
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
import edu.asu.conceptpower.app.core.POS;
import edu.asu.conceptpower.app.db.TypeDatabaseClient;
import edu.asu.conceptpower.app.exceptions.IndexerRunningException;
import edu.asu.conceptpower.app.exceptions.LuceneException;
import edu.asu.conceptpower.app.manager.IConceptManager;
import edu.asu.conceptpower.app.rdf.RDFMessageFactory;
import edu.asu.conceptpower.app.util.URIHelper;
import edu.asu.conceptpower.app.model.ConceptEntry;
import edu.asu.conceptpower.app.model.ConceptType;
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

    private final String PONY = "pony";
    private final String typeId1 = "TYPE-1";
    private final String typeId2 = "TYPE-2";

    @Before
    public void setup() throws IOException, IllegalAccessException, LuceneException, IndexerRunningException {
        MockitoAnnotations.initMocks(this);
        final String pos = POS.NOUN;


        ConceptEntry[] entries = new ConceptEntry[2];
        ConceptEntry entry = new ConceptEntry();
        entry.setWord(PONY);
        entry.setPos(pos);
        entry.setId("WID-123");
        entry.setConceptList(Constants.WORDNET_DICTIONARY);
        entry.setTypeId(typeId1);
        entry.setDescription("WordnetID-123 !!@?#$%^&*()+=/.,';[]}{:\"<>?");
        entries[0] = entry;

        ConceptEntry entry2 = new ConceptEntry();
        entry2.setWord(PONY);
        entry2.setPos(pos);
        entry2.setConceptList("Test List");
        entry2.setTypeId(typeId1);
        entry2.setId("CCP - 123");
        entry2.setDescription("WordnetID-123 !!@?#$%^&*()+=/.,';[]}{:\"<>?");
        entries[1] = entry2;

        ConceptType type1 = new ConceptType();
        type1.setTypeId(typeId1);
        type1.setDescription("TypeID 1!!@?#$%^&*()+=/.,';[]}{:\"<>?");
        type1.setTypeName("TYPE-1");

        ConceptType type2 = new ConceptType();
        type2.setTypeId(typeId2);
        type1.setDescription("TypeID 2!!@?#$%^&*()+=/.,';[]}{:\"<>?");
        type1.setTypeName("TYPE-2");

        Mockito.when(dictManager.getConceptListEntriesForWordPOS(PONY, POS.NOUN, null)).thenReturn(entries);
        Mockito.when(typeManager.getType(typeId1)).thenReturn(type1);
        Mockito.when(typeManager.getType(typeId2)).thenReturn(type2);
        Mockito.when(messageFactory.getMessageFactory(MediaType.APPLICATION_XML_VALUE)).thenReturn(xmlMEssageFactory);
        Mockito.when(messageFactory.getMessageFactory(MediaType.APPLICATION_JSON_VALUE)).thenReturn(jsonMessageFactory);

        Mockito.when(xmlMEssageFactory.createConceptMessage()).thenReturn(new XMLConceptMessage(uriCreator));
        Mockito.when(jsonMessageFactory.createConceptMessage()).thenReturn(new JsonConceptMessage(uriCreator));
    }

    @Test
    public void test_getWordNetEntry_successInXml()
            throws IndexerRunningException, ParserConfigurationException, SAXException, IOException {
        final String expectedResponse = IOUtils
                .toString(this.getClass().getClassLoader().getResourceAsStream("unitTestOutput/wordNetEntry.xml"));
        ResponseEntity<String> response = conceptLookup.getWordNetEntry(PONY, POS.NOUN,
                MediaType.APPLICATION_XML_VALUE);
        RestTestUtility.testValidXml(response.getBody());
        Assert.assertEquals(expectedResponse, response.getBody());
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void test_getWordNetEntry_emptyResult() throws IndexerRunningException, ParserConfigurationException,
            SAXException, IOException, IllegalAccessException, LuceneException {

        final String noResult = "noResult";
        final String expectedResponse = IOUtils.toString(
                this.getClass().getClassLoader().getResourceAsStream("unitTestOutput/conceptLookupNoResult.xml"));
        Mockito.when(dictManager.getConceptListEntriesForWordPOS(PONY, POS.NOUN, null)).thenReturn(null);
        ResponseEntity<String> response = conceptLookup.getWordNetEntry(noResult, POS.NOUN,
                MediaType.APPLICATION_XML_VALUE);
        Assert.assertEquals(expectedResponse, response.getBody());
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void test_getWordNetEntry_resultWithSpecialCharactersInJson()
            throws IndexerRunningException, JSONException, IllegalAccessException, LuceneException, IOException {
        final String expectedResponse = IOUtils.toString(this.getClass().getClassLoader()
                .getResourceAsStream("unitTestOutput/wordNetEntryWithSpecialCharacters.json"));
        final String word = "einstein@albert";
        final String posNoun = POS.NOUN;

        ConceptEntry[] entries = new ConceptEntry[2];
        ConceptEntry entry = new ConceptEntry();
        entry.setWord(word);
        entry.setPos(posNoun);
        entry.setId("WID-123");
        entry.setConceptList(Constants.WORDNET_DICTIONARY);
        entry.setTypeId(typeId1);
        entry.setDescription("WordnetID-123 !!@?#$%^&*()+=/.,';[]}{:\"<>?");
        entries[0] = entry;

        ConceptEntry entry2 = new ConceptEntry();
        entry2.setWord("einstein");
        entry2.setPos(posNoun);
        entry2.setConceptList("Test List");
        entry2.setTypeId(typeId1);
        entry2.setDescription("WordnetID-123 !!@?#$%^&*()+=/.,';[]}{:\"<>?");
        entries[1] = entry2;

        Mockito.when(dictManager.getConceptListEntriesForWordPOS(word, POS.NOUN, null)).thenReturn(entries);

        ResponseEntity<String> response = conceptLookup.getWordNetEntry(word, posNoun,
                MediaType.APPLICATION_JSON_VALUE);
        RestTestUtility.testValidJson(response.getBody());
        Assert.assertEquals(expectedResponse, response.getBody());
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void test_getWordNetEntry_resultWithSpecialCharactersInXml() throws IOException, IllegalAccessException,
            LuceneException, IndexerRunningException, ParserConfigurationException, SAXException {
        final String expectedResponse = IOUtils.toString(this.getClass().getClassLoader()
                .getResourceAsStream("unitTestOutput/wordNetEntryWithSpecialCharacters.xml"));
        final String word = "einstein@albert";
        final String posNoun = POS.NOUN;

        ConceptEntry[] entries = new ConceptEntry[2];
        ConceptEntry entry = new ConceptEntry();
        entry.setWord(word);
        entry.setPos(posNoun);
        entry.setId("WID-123");
        entry.setConceptList(Constants.WORDNET_DICTIONARY);
        entry.setTypeId(typeId1);
        entry.setDescription("WordnetID-123 !!@?#$%^&*()+=/.,';[]}{:\"<>?");
        entries[0] = entry;

        ConceptEntry entry2 = new ConceptEntry();
        entry2.setWord("einstein");
        entry2.setPos(posNoun);
        entry2.setConceptList("Test List");
        entry2.setTypeId(typeId1);
        entry2.setDescription("WordnetID-123 !!@?#$%^&*()+=/.,';[]}{:\"<>?");
        entries[1] = entry2;

        Mockito.when(dictManager.getConceptListEntriesForWordPOS(word, POS.NOUN, null)).thenReturn(entries);

        ResponseEntity<String> response = conceptLookup.getWordNetEntry(word, posNoun, MediaType.APPLICATION_XML_VALUE);
        RestTestUtility.testValidXml(response.getBody());
        Assert.assertEquals(expectedResponse, response.getBody());
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void test_getWordNetEntryTest_successInJson()
            throws IndexerRunningException, ParserConfigurationException, SAXException, IOException, JSONException {

        ResponseEntity<String> response = conceptLookup.getWordNetEntry(PONY, POS.NOUN,
                MediaType.APPLICATION_JSON_VALUE);
        final String expectedResponse = IOUtils
                .toString(this.getClass().getClassLoader().getResourceAsStream("unitTestOutput/wordnetEntry.json"));
        RestTestUtility.testValidJson(response.getBody());
        Assert.assertEquals(expectedResponse, response.getBody());
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void test_getWordNetEntry_successInRdf()
            throws IndexerRunningException, IllegalAccessException, LuceneException {

        final String ROBERT = "robert";
        final String ROBERT_1 = "robert-1";
        
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
