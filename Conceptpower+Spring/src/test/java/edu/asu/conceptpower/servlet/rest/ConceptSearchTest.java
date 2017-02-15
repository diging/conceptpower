package edu.asu.conceptpower.servlet.rest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
import edu.asu.conceptpower.rest.ConceptSearch;
import edu.asu.conceptpower.rest.ConceptSearchParameterValidator;
import edu.asu.conceptpower.rest.ConceptSearchParameters;
import edu.asu.conceptpower.rest.SearchParamters;
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
    
    private String xmlNoRecordsFoundError = "<conceptpowerReply xmlns:digitalHPS=\"http://www.digitalhps.org/\"><digitalHPS:error error_message=\"No records found for the search condition.\" ></digitalHPS:error></conceptpowerReply>";
    private int numberOfRecordsPerPage = 20;
    private String jsonNoRecordsFoundError = "{\"message\":\"No records found for the search condition.\"}";

    @Before
    public void init() throws IllegalAccessException, LuceneException, IndexerRunningException {
        MockitoAnnotations.initMocks(this);
        Mockito.when(messageFactory.getMessageFactory(MediaType.APPLICATION_XML_VALUE)).thenReturn(xmlMessageFactory);
        Mockito.when(messageFactory.getMessageFactory(MediaType.APPLICATION_XML_VALUE).createConceptMessage())
                .thenReturn(new XMLConceptMessage(uriCreator));

        Mockito.when(messageFactory.getMessageFactory(MediaType.APPLICATION_JSON_VALUE)).thenReturn(jsonMessageFactory);
        Mockito.when(messageFactory.getMessageFactory(MediaType.APPLICATION_JSON_VALUE).createConceptMessage())
                .thenReturn(new JsonConceptMessage(uriCreator));
        conceptSearchParameter = new ConceptSearchParameters();
        conceptSearchParameter.setPos("noun");
        conceptSearchParameter.setWord("pony");
        conceptSearchParameter.setConcept_list("Test-List");
        conceptSearchParameter.setType_id("TYPE-123");
        conceptSearchParameter.setOperator(SearchParamters.OP_AND);
        conceptSearchParameter.setNumber_of_records_per_page(numberOfRecordsPerPage);
        
        String typeId = "TYPE-123";

        ConceptType type = new ConceptType();
        type.setCreatorId("admin");
        type.setDescription("Type-=123 description");
        type.setTypeId(typeId);
        Mockito.when(typeManager.getType("TYPE-123")).thenReturn(type);
        
        Map<String, String> map = new HashMap<>();
        map.put("pos", "noun");
        map.put("type-id", "TYPE-123");
        map.put("concept_list", "Test-List");
        map.put("word", "pony");

        ConceptEntry[] entries = new ConceptEntry[1];
        ConceptEntry entry = new ConceptEntry();
        entry.setPos("noun");
        entry.setWord("pony");
        entry.setTypeId(typeId);
        entry.setConceptList("Test-List");

        entries[0] = entry;

        int numberOfResults = 100;

        Mockito.when(manager.getTotalNumberOfRecordsForSearch(Mockito.anyMapOf(String.class, String.class),
                Mockito.anyString())).thenReturn(numberOfResults);

        Mockito.when(manager.searchForConceptByPageNumberAndFieldMap(Mockito.anyMapOf(String.class, String.class),
                Mockito.anyString(), Mockito.eq(1), Mockito.eq(numberOfRecordsPerPage))).thenReturn(entries);

    }

    @Test
    public void searchConceptTestInXML() throws IllegalArgumentException, IllegalAccessException,
            IndexerRunningException, LuceneException, ParserConfigurationException, SAXException, IOException {

        Mockito.when(result.hasErrors()).thenReturn(false);

        ResponseEntity<String> output = conceptSearch.searchConcept(conceptSearchParameter, result,
                MediaType.APPLICATION_XML_VALUE);

        Mockito.verify(manager).getTotalNumberOfRecordsForSearch(Mockito.anyMapOf(String.class, String.class),
                Mockito.anyString());
        
        Mockito.verify(manager).searchForConceptByPageNumberAndFieldMap(Mockito.anyMapOf(String.class, String.class),
                Mockito.anyString(), Mockito.eq(1), Mockito.eq(numberOfRecordsPerPage));

        RestTestUtility.testValidXml(output.getBody());

        Assert.assertEquals(HttpStatus.OK, output.getStatusCode());

    }

    @Test
    public void searchConceptInJson() throws IllegalAccessException, LuceneException, IndexerRunningException,
            JsonProcessingException, IllegalArgumentException, JSONException {

        Mockito.when(result.hasErrors()).thenReturn(false);

        ResponseEntity<String> output = conceptSearch.searchConcept(conceptSearchParameter, result,
                MediaType.APPLICATION_JSON_VALUE);

        Mockito.verify(manager).getTotalNumberOfRecordsForSearch(Mockito.anyMapOf(String.class, String.class),
                Mockito.anyString());

        Mockito.verify(manager).searchForConceptByPageNumberAndFieldMap(Mockito.anyMapOf(String.class, String.class),
                Mockito.anyString(), Mockito.eq(1), Mockito.eq(numberOfRecordsPerPage));

        RestTestUtility.testValidJson(output.getBody());

        Assert.assertEquals(HttpStatus.OK, output.getStatusCode());

    }

    @Test
    public void searchConceptForNoResultInJSON() throws IllegalAccessException, LuceneException,
            IndexerRunningException,
            JsonProcessingException, IllegalArgumentException, JSONException {

        Mockito.when(result.hasErrors()).thenReturn(false);
        Map<String, String> map = new HashMap<>();
        map.put("pos", "noun");
        map.put("type-id", "TYPE-123");
        map.put("concept_list", "Test-List");
        map.put("word", "pony");

        int numberOfResults = 100;

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
    public void searchConceptForNoResultInXML() throws IllegalAccessException, LuceneException,
            IndexerRunningException, JsonProcessingException, IllegalArgumentException, JSONException {

        Mockito.when(result.hasErrors()).thenReturn(false);
        Map<String, String> map = new HashMap<>();
        map.put("pos", "noun");
        map.put("type-id", "TYPE-123");
        map.put("concept_list", "Test-List");
        map.put("word", "pony");

        int numberOfResults = 100;

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
    public void testForError()
            throws JsonProcessingException, IllegalArgumentException, IllegalAccessException, IndexerRunningException {
        
        String posError = "Please enter correct pos value.";
        
        ConceptSearchParameters conceptSearchparam = new ConceptSearchParameters();
        conceptSearchparam.setPos("");
        
        BindingResult result = new BindException(conceptSearchparam, "ConceptSearchParameters");
        ObjectError error = new ObjectError("pos", posError);
        result.addError(error);
        
        String returnErrorMessage = "<conceptpowerReply xmlns:digitalHPS=\"http://www.digitalhps.org/\"><digitalHPS:error error_message=\""
                + posError + "\" ></digitalHPS:error></conceptpowerReply>";
        
        ResponseEntity<String> output = conceptSearch.searchConcept(conceptSearchparam, result,
                MediaType.APPLICATION_XML_VALUE);
        Assert.assertEquals(returnErrorMessage, output.getBody());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, output.getStatusCode());
        
    }

}
