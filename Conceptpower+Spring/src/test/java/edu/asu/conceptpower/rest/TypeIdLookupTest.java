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

import com.fasterxml.jackson.core.JsonProcessingException;

import edu.asu.conceptpower.app.db.TypeDatabaseClient;
import edu.asu.conceptpower.app.util.URIHelper;
import edu.asu.conceptpower.core.ConceptType;
import edu.asu.conceptpower.rest.msg.IMessageConverter;
import edu.asu.conceptpower.rest.msg.IMessageRegistry;
import edu.asu.conceptpower.rest.msg.json.JsonTypeMessage;
import edu.asu.conceptpower.rest.msg.xml.XMLTypeMessage;
import jakarta.servlet.http.HttpServletRequest;
import junit.framework.Assert;

public class TypeIdLookupTest {

    @Mock
    private TypeDatabaseClient typeManager;

    @Mock
    private IMessageRegistry messageFactory;

    @InjectMocks
    private TypeIdLookup typeIdLookup;

    @Mock
    private IMessageConverter xmlMessageFactory;

    @Mock
    private URIHelper uriCreator;

    @Mock(name = "jsonMessageFactory")
    private IMessageConverter jsonMessageFactory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(messageFactory.getMessageFactory(MediaType.APPLICATION_XML_VALUE)).thenReturn(xmlMessageFactory);
        Mockito.when(messageFactory.getMessageFactory(MediaType.APPLICATION_XML_VALUE).createTypeMessage())
                .thenReturn(new XMLTypeMessage(uriCreator));

        Mockito.when(messageFactory.getMessageFactory(MediaType.APPLICATION_JSON_VALUE)).thenReturn(jsonMessageFactory);
        Mockito.when(messageFactory.getMessageFactory(MediaType.APPLICATION_JSON_VALUE).createTypeMessage())
                .thenReturn(new JsonTypeMessage(uriCreator));

    }

    @Test
    public void test_getTypeById_successInXml() throws ParserConfigurationException, SAXException, IOException {
        final String typeId = "0c2a2a8c-c6d8-4d25-8f07-689fd8c27b0b";
        final String superTypeId = "0c2a2a8c-c6d8-4d25-8f07-689fd8c27b0c";

        final String expectedOutput = IOUtils
                .toString(this.getClass().getClassLoader().getResourceAsStream("unitTestOutput/conceptType.xml"));

        ConceptType type = new ConceptType();
        type.setTypeId(typeId);
        type.setDescription("Type Description");
        type.setTypeName("Type - Test");
        type.setSupertypeId(superTypeId);

        Mockito.when(typeManager.getType(typeId)).thenReturn(type);

        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        Mockito.when(req.getParameter("id"))
                .thenReturn("http://www.digitalhps.org/types/TYPE_0c2a2a8c-c6d8-4d25-8f07-689fd8c27b0b");

        ResponseEntity<String> typeMessage = typeIdLookup.getTypeById(req, MediaType.APPLICATION_XML_VALUE);

        RestTestUtility.testValidXml(typeMessage.getBody());
        Assert.assertEquals(expectedOutput, typeMessage.getBody());
        Assert.assertEquals(HttpStatus.OK, typeMessage.getStatusCode());

    }

    @Test
    public void test_getTypeById_successInJson() throws JSONException, IOException {
        final String typeId = "0c2a2a8c-c6d8-4d25-8f07-689fd8c27b0b";
        final String superTypeId = "0c2a2a8c-c6d8-4d25-8f07-689fd8c27b0c";

        final String expectedOutput = IOUtils
                .toString(this.getClass().getClassLoader().getResourceAsStream("unitTestOutput/conceptType.json"));

        ConceptType type = new ConceptType();
        type.setTypeId(typeId);
        type.setDescription("Type Description");
        type.setTypeName("Type - Test");
        type.setSupertypeId(superTypeId);

        Mockito.when(typeManager.getType(typeId)).thenReturn(type);

        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        Mockito.when(req.getParameter("id"))
                .thenReturn("http://www.digitalhps.org/types/TYPE_0c2a2a8c-c6d8-4d25-8f07-689fd8c27b0b");

        ResponseEntity<String> typeMessage = typeIdLookup.getTypeById(req, MediaType.APPLICATION_JSON_VALUE);

        RestTestUtility.testValidJson(typeMessage.getBody());
        Assert.assertEquals(expectedOutput, typeMessage.getBody());
        Assert.assertEquals(HttpStatus.OK, typeMessage.getStatusCode());

    }

    @Test
    public void test_getTypeById_emptyResult() throws JsonProcessingException {
        final String typeId = "0c2a2a8c-c6d8-4d25-8f07-689fd8c27b0w";
        Mockito.when(typeManager.getType(typeId)).thenReturn(null);
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        Mockito.when(req.getParameter("id"))
                .thenReturn("http://www.digitalhps.org/types/TYPE_0c2a2a8c-c6d8-4d25-8f07-689fd8c27b0w");

        ResponseEntity<String> typeMessage = typeIdLookup.getTypeById(req, MediaType.APPLICATION_JSON_VALUE);
        Assert.assertEquals(HttpStatus.NOT_FOUND, typeMessage.getStatusCode());
        Assert.assertEquals(null, typeMessage.getBody());

    }

    @Test
    public void test_getTypeById_nullId() throws ParserConfigurationException, SAXException, IOException {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        Mockito.when(req.getParameter("id")).thenReturn(null);

        ResponseEntity<String> typeMessage = typeIdLookup.getTypeById(req, MediaType.APPLICATION_XML_VALUE);
        Assert.assertEquals(null, typeMessage.getBody());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, typeMessage.getStatusCode());
    }

}
