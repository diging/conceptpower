package edu.asu.conceptpower.rest;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.apache.commons.io.IOUtil;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.examples.RecursiveElementNameAndTextQualifier;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import edu.asu.conceptpower.IntegrationTest;

public class ConceptLookupIT extends IntegrationTest {

    @Test
    public void test_getWordNetEntry_successForSingleEntryInJson() throws Exception {
        final String output = IOUtil.toString(
                this.getClass().getClassLoader().getResourceAsStream("output/conceptLookUpWordNetEntry.json"));
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/ConceptLookup/Douglas Weiner/noun")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(output, false)).andExpect(status().isOk());
    }

    @Test
    public void test_getWordNetEntry_noResultsInJson() throws Exception {

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/ConceptLookup/Gustav Robert Kirchhoff/verb")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", is("No concept entry found."))).andExpect(status().isOk());
    }

    @Test
    public void test_getWordNetEntry_successForMultipleEntryInJson() throws Exception {
        final String output = IOUtil.toString(
                this.getClass().getClassLoader().getResourceAsStream("output/conceptLookUpForMultipletEntry.json"));
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/ConceptLookup/Douglas/noun")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.content().json(output, false)).andExpect(status().isOk());
    }

    @Test
    public void test_getWordNetEntry_successForSingleEntryInXml() throws Exception {
        final String output = IOUtil
                .toString(this.getClass().getClassLoader().getResourceAsStream("output/conceptLookUpWordNetEntry.xml"));
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/ConceptLookup/Douglas Weiner/noun")
                        .accept(MediaType.APPLICATION_XML_VALUE))
                .andExpect(content().xml(output)).andExpect(status().isOk());
    }

    @Test
    public void test_getWordNetEntry_noResultsInXml() throws Exception {
        final String output = IOUtil
                .toString(this.getClass().getClassLoader().getResourceAsStream("output/conceptEntryNotFound.xml"));
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/ConceptLookup/Gustav Robert Kirchhoff/verb")
                        .accept(MediaType.APPLICATION_XML_VALUE))
                .andExpect(content().xml(output)).andExpect(status().isOk());
    }

    @Test
    public void test_getWordNetEntry_successForMultipleEntryInXml() throws Exception {
        final String output = IOUtil.toString(
                this.getClass().getClassLoader().getResourceAsStream("output/conceptLookUpForMultipletEntry.xml"));
        MvcResult result = this.mockMvc.perform(
                MockMvcRequestBuilders.get("/ConceptLookup/Douglas/noun").accept(MediaType.APPLICATION_XML_VALUE))
                .andExpect(status().isOk()).andReturn();
        Diff myDiff = new Diff(output, result.getResponse().getContentAsString());
        myDiff.overrideElementQualifier(new RecursiveElementNameAndTextQualifier());
        XMLAssert.assertXMLEqual("Similarlity failed in test_getWordNetEntry_successForMultipleEntryInXml", myDiff,
                true);
    }

    @Test
    public void test_getWordNetEntry_searchWithWildCardOneOrMoreCharactersInXml() throws Exception {
        final String output = IOUtil.toString(
                this.getClass().getClassLoader().getResourceAsStream("output/wildCardSearchConceptLookUp1.xml"));
        MvcResult result = this.mockMvc.perform(
                MockMvcRequestBuilders.get("/ConceptLookup/Dougl*/noun").accept(MediaType.APPLICATION_XML_VALUE))
                .andExpect(status().isOk()).andReturn();

        Diff myDiff = new Diff(output, result.getResponse().getContentAsString());
        myDiff.overrideElementQualifier(new RecursiveElementNameAndTextQualifier());
        XMLAssert.assertXMLEqual(
                "Similarlity failed in test_getWordNetEntry_searchWithWildCardOneOrMoreCharactersInXml", myDiff,
                true);
    }

    @Test
    public void test_getWordNetEntry_searchWithWildCardOneOrMoreCharactersInJson() throws Exception {
        final String output = IOUtil.toString(
                this.getClass().getClassLoader().getResourceAsStream("output/wildCardSearchConceptLookUp1.json"));
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/ConceptLookup/Dougl*/noun")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(output)).andExpect(status().isOk());
    }
}
