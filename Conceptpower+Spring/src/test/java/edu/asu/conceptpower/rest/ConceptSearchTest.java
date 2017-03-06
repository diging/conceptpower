package edu.asu.conceptpower.rest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.core.JsonProcessingException;

import edu.asu.conceptpower.app.core.IIndexService;
import edu.asu.conceptpower.app.db.TypeDatabaseClient;
import edu.asu.conceptpower.app.exceptions.IndexerRunningException;
import edu.asu.conceptpower.app.exceptions.LuceneException;
import edu.asu.conceptpower.app.util.URIHelper;
import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptType;
import edu.asu.conceptpower.rest.msg.IMessageConverter;
import edu.asu.conceptpower.rest.msg.IMessageRegistry;
import edu.asu.conceptpower.rest.msg.json.JsonConceptMessage;
import edu.asu.conceptpower.rest.msg.xml.XMLConceptMessage;
import junit.framework.Assert;

public class ConceptSearchTest {

    @Mock
    private IIndexService manager;

    @Mock
    private TypeDatabaseClient typeManager;

    @Mock
    private IMessageRegistry messageFactory;

    @Mock
    private URIHelper uriHelper;

    @Mock
    private BindingResult result;

    @Mock
    private ConceptSearchParameterValidator validator;

    @InjectMocks
    private ConceptSearch conceptSearch;

    @Mock
    private IMessageConverter xmlMessageFactory;

    @Mock
    private URIHelper uriCreator;

    @Mock(name = "jsonMessageFactory")
    private IMessageConverter jsonMessageFactory;

    private ConceptSearchParameters conceptSearchParameter;

    private int numberOfRecordsPerPage = 20;
    private final String conceptListName = "Test-List";
    private final String pos = "noun";
    private final String word = "pony";
    private final String typeId = "TYPE-123";

    @Before
    public void setup() throws IllegalAccessException, LuceneException, IndexerRunningException {
        MockitoAnnotations.initMocks(this);
        Mockito.when(messageFactory.getMessageFactory(MediaType.APPLICATION_XML_VALUE)).thenReturn(xmlMessageFactory);
        Mockito.when(messageFactory.getMessageFactory(MediaType.APPLICATION_XML_VALUE).createConceptMessage())
                .thenReturn(new XMLConceptMessage(uriCreator));

        Mockito.when(messageFactory.getMessageFactory(MediaType.APPLICATION_JSON_VALUE)).thenReturn(jsonMessageFactory);
        Mockito.when(messageFactory.getMessageFactory(MediaType.APPLICATION_JSON_VALUE).createConceptMessage())
                .thenReturn(new JsonConceptMessage(uriCreator));

        conceptSearchParameter = new ConceptSearchParameters();
        conceptSearchParameter.setPos(pos);
        conceptSearchParameter.setWord(word);
        conceptSearchParameter.setConcept_list(conceptListName);
        conceptSearchParameter.setType_id(typeId);
        conceptSearchParameter.setOperator(SearchParamters.OP_AND);
        conceptSearchParameter.setNumber_of_records_per_page(numberOfRecordsPerPage);



        ConceptType type = new ConceptType();
        type.setCreatorId("admin");
        type.setDescription("Type-=123 description");
        type.setTypeId(typeId);
        Mockito.when(typeManager.getType(typeId)).thenReturn(type);

        Map<String, String> map = new HashMap<>();
        map.put("pos", pos);
        map.put("type-id", typeId);
        map.put("concept_list", conceptListName);
        map.put("word", word);

        ConceptEntry[] entries = new ConceptEntry[1];
        ConceptEntry entry = new ConceptEntry();
        entry.setPos(pos);
        entry.setWord(word);
        entry.setTypeId(typeId);
        entry.setConceptList(conceptListName);

        entries[0] = entry;

        int numberOfResults = 100;

        Mockito.when(manager.getTotalNumberOfRecordsForSearch(Mockito.anyMapOf(String.class, String.class),
                Mockito.anyString())).thenReturn(numberOfResults);

        Mockito.when(manager.searchForConceptByPageNumberAndFieldMap(Mockito.anyMapOf(String.class, String.class),
                Mockito.anyString(), Mockito.eq(1), Mockito.eq(numberOfRecordsPerPage))).thenReturn(entries);

    }

    @Test
    public void test_searchConcept_successInXml() throws IllegalArgumentException, IllegalAccessException,
            IndexerRunningException, LuceneException, ParserConfigurationException, SAXException, IOException {

        final String expectedOutput = IOUtil
                .toString(this.getClass().getClassLoader().getResourceAsStream("output/conceptSearch.xml"));
        Mockito.when(result.hasErrors()).thenReturn(false);
        ResponseEntity<String> output = conceptSearch.searchConcept(conceptSearchParameter, result,
                MediaType.APPLICATION_XML_VALUE);

        Mockito.verify(manager).getTotalNumberOfRecordsForSearch(Mockito.anyMapOf(String.class, String.class),
                Mockito.anyString());

        Mockito.verify(manager).searchForConceptByPageNumberAndFieldMap(Mockito.anyMapOf(String.class, String.class),
                Mockito.anyString(), Mockito.eq(1), Mockito.eq(numberOfRecordsPerPage));

        RestTestUtility.testValidXml(output.getBody());
        Assert.assertEquals(expectedOutput, output.getBody());
        Assert.assertEquals(HttpStatus.OK, output.getStatusCode());

    }

    @Test
    public void test_searchConcept_successInJson() throws IllegalAccessException, LuceneException,
            IndexerRunningException, IllegalArgumentException, JSONException, IOException {

        final String expectedOutput = IOUtil
                .toString(this.getClass().getClassLoader().getResourceAsStream("output/conceptSearch.json"));
        Mockito.when(result.hasErrors()).thenReturn(false);

        ResponseEntity<String> output = conceptSearch.searchConcept(conceptSearchParameter, result,
                MediaType.APPLICATION_JSON_VALUE);

        Mockito.verify(manager).getTotalNumberOfRecordsForSearch(Mockito.anyMapOf(String.class, String.class),
                Mockito.anyString());

        Mockito.verify(manager).searchForConceptByPageNumberAndFieldMap(Mockito.anyMapOf(String.class, String.class),
                Mockito.anyString(), Mockito.eq(1), Mockito.eq(numberOfRecordsPerPage));

        RestTestUtility.testValidJson(output.getBody());
        Assert.assertEquals(expectedOutput, output.getBody());
        Assert.assertEquals(HttpStatus.OK, output.getStatusCode());

    }

    @Test
    public void test_searchConcept_noRecordsFoundInJson() throws IllegalAccessException, LuceneException,
            IndexerRunningException, IllegalArgumentException, JSONException, IOException {

        final String jsonNoRecordsFoundError = IOUtil
                .toString(this.getClass().getClassLoader().getResourceAsStream("output/recordsNotFound.json"));
        Mockito.when(result.hasErrors()).thenReturn(false);
        Map<String, String> map = new HashMap<>();
        map.put("pos", pos);
        map.put("type-id", typeId);
        map.put("concept_list", conceptListName);
        map.put("word", word);

        int numberOfResults = 0;

        Mockito.when(manager.getTotalNumberOfRecordsForSearch(Mockito.anyMapOf(String.class, String.class),
                Mockito.anyString())).thenReturn(numberOfResults);

        Mockito.when(manager.searchForConceptByPageNumberAndFieldMap(Mockito.anyMapOf(String.class, String.class),
                Mockito.anyString(), Mockito.eq(1), Mockito.eq(numberOfRecordsPerPage))).thenReturn(null);

        ResponseEntity<String> output = conceptSearch.searchConcept(conceptSearchParameter, result,
                MediaType.APPLICATION_JSON_VALUE);

        Mockito.verify(manager).getTotalNumberOfRecordsForSearch(Mockito.anyMapOf(String.class, String.class),
                Mockito.anyString());

        Mockito.verify(manager).searchForConceptByPageNumberAndFieldMap(Mockito.anyMapOf(String.class, String.class),
                Mockito.anyString(), Mockito.eq(1), Mockito.eq(numberOfRecordsPerPage));

        Assert.assertEquals(HttpStatus.OK, output.getStatusCode());
        Assert.assertEquals(jsonNoRecordsFoundError, output.getBody());
    }

    @Test
    public void test_searchConcept_noRecordsFoundInXml() throws IllegalAccessException, LuceneException,
            IndexerRunningException, IllegalArgumentException, JSONException, IOException {

        final String xmlNoRecordsFoundError = IOUtil
                .toString(this.getClass().getClassLoader().getResourceAsStream("output/recordsNotFound.xml"));

        Mockito.when(result.hasErrors()).thenReturn(false);
        Map<String, String> map = new HashMap<>();
        map.put("pos", pos);
        map.put("type-id", typeId);
        map.put("concept_list", conceptListName);
        map.put("word", word);

        int numberOfResults = 0;

        Mockito.when(manager.getTotalNumberOfRecordsForSearch(Mockito.anyMapOf(String.class, String.class),
                Mockito.anyString())).thenReturn(numberOfResults);

        Mockito.when(manager.searchForConceptByPageNumberAndFieldMap(Mockito.anyMapOf(String.class, String.class),
                Mockito.anyString(), Mockito.eq(1), Mockito.eq(numberOfRecordsPerPage))).thenReturn(null);

        ResponseEntity<String> output = conceptSearch.searchConcept(conceptSearchParameter, result,
                MediaType.APPLICATION_XML_VALUE);

        Mockito.verify(manager).getTotalNumberOfRecordsForSearch(Mockito.anyMapOf(String.class, String.class),
                Mockito.anyString());

        Mockito.verify(manager).searchForConceptByPageNumberAndFieldMap(Mockito.anyMapOf(String.class, String.class),
                Mockito.anyString(), Mockito.eq(1), Mockito.eq(numberOfRecordsPerPage));

        Assert.assertEquals(HttpStatus.OK, output.getStatusCode());
        Assert.assertEquals(xmlNoRecordsFoundError, output.getBody());
    }

    @Test
    public void test_searchConcept_invalidPos()
            throws JsonProcessingException, IllegalArgumentException, IllegalAccessException, IndexerRunningException {

        final String posError = "Please enter correct pos value.";

        ConceptSearchParameters conceptSearchparam = new ConceptSearchParameters();
        conceptSearchparam.setPos("subject");

        BindingResult result = new BindException(conceptSearchparam, "ConceptSearchParameters");
        ObjectError error = new ObjectError("pos", posError);
        result.addError(error);

        final String returnErrorMessage = "<conceptpowerReply xmlns:digitalHPS=\"http://www.digitalhps.org/\"><digitalHPS:error error_message=\""
                + posError + "\" ></digitalHPS:error></conceptpowerReply>";

        ResponseEntity<String> output = conceptSearch.searchConcept(conceptSearchparam, result,
                MediaType.APPLICATION_XML_VALUE);
        Assert.assertEquals(returnErrorMessage, output.getBody());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, output.getStatusCode());

    }

}
