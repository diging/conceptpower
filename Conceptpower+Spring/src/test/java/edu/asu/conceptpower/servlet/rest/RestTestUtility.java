package edu.asu.conceptpower.servlet.rest;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class RestTestUtility {

    public static void testValidXml(String xml) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        db.parse(new InputSource(new ByteArrayInputStream(xml.getBytes("utf-8"))));
    }

    public static void testValidJson(String json) throws JSONException {
        new JSONObject(json);
    }
}
