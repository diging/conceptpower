package edu.asu.conceptpower.rest;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.apache.commons.io.IOUtil;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import edu.asu.conceptpower.IntegrationTest;

public class SynonymSearchIT extends IntegrationTest {

    @Test
    public void test_getSynonymsForId_successInJson() throws Exception {
        final String output = IOUtil
                .toString(this.getClass().getClassLoader().getResourceAsStream("output/synonym.json"));
        this.mockMvc.perform(MockMvcRequestBuilders.get("/SynonymSearch").accept(MediaType.APPLICATION_JSON_VALUE)
                .with(new RequestPostProcessor() {
                    public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                        request.setParameter("id", "WID-02382039-N-02-Indian_pony");
                        return request;
                    }
                })).andExpect(content().string(output)).andExpect(status().isOk());
    }

    @Test
    public void test_getSynonymsForId_emptySynonymIdInJson() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/SynonymSearch").accept(MediaType.APPLICATION_JSON_VALUE)
                .with(new RequestPostProcessor() {
                    public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                        request.setParameter("id", "");
                        return request;
                    }
                })).andExpect(status().isBadRequest());
    }

    @Test
    public void test_getSynonymsForId_nullSynonymIdInJson() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/SynonymSearch").accept(MediaType.APPLICATION_JSON_VALUE)
                .with(new RequestPostProcessor() {
                    public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                        return request;
                    }
                })).andExpect(status().isBadRequest());
    }

    @Test
    public void test_getSynonymsForId_successInXml() throws Exception {
        final String output = IOUtil
                .toString(this.getClass().getClassLoader().getResourceAsStream("output/synonym.xml"));
        this.mockMvc.perform(MockMvcRequestBuilders.get("/SynonymSearch").accept(MediaType.APPLICATION_XML_VALUE)
                .with(new RequestPostProcessor() {
                    public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                        request.setParameter("id", "WID-02382039-N-02-Indian_pony");
                        return request;
                    }
                })).andExpect(content().string(output)).andExpect(status().isOk());
    }

    @Test
    public void test_getSynonymsForId_emptySynonymIdInXml() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/SynonymSearch").accept(MediaType.APPLICATION_XML_VALUE)
                .with(new RequestPostProcessor() {
                    public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                        request.setParameter("id", "");
                        return request;
                    }
                })).andExpect(status().isBadRequest());
    }

    @Test
    public void test_getSynonymsForId_nullSynonymIdInXml() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/SynonymSearch").accept(MediaType.APPLICATION_XML_VALUE)
                .with(new RequestPostProcessor() {
                    public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                        return request;
                    }
                })).andExpect(status().isBadRequest());
    }

}
