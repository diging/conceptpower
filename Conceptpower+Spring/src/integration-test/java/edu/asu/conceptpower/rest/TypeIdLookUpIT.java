package edu.asu.conceptpower.rest;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import edu.asu.conceptpower.IntegrationTest;
import edu.asu.conceptpower.rest.msg.xml.XMLConstants;

public class TypeIdLookUpIT extends IntegrationTest {

    @Test
    public void test_getTypeById_successWithId() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/Type").accept(MediaType.APPLICATION_JSON_VALUE)
                .with(new RequestPostProcessor() {
                    public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                        request.setParameter("id", "7c8745be-d06f-4feb-b749-910efa1b986d");
                        return request;
                    }
                })).andExpect(status().isOk())
                .andExpect(jsonPath("$.type_id", is("7c8745be-d06f-4feb-b749-910efa1b986d")))
                .andExpect(jsonPath("$.type_uri",
                        is("http://www.digitalhps.org/types/TYPE_7c8745be-d06f-4feb-b749-910efa1b986d")))
                .andExpect(jsonPath("$.creator_id", is("admin"))).andExpect(jsonPath("$.description", is("Test-Type")));
    }

    @Test
    public void test_getTypeById_successWithTypeId() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/Type").accept(MediaType.APPLICATION_JSON_VALUE)
                .with(new RequestPostProcessor() {
                    public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                        request.setParameter("id", XMLConstants.TYPE_PREFIX + "7c8745be-d06f-4feb-b749-910efa1b986d");
                        return request;
                    }
                })).andExpect(status().isOk())
                .andExpect(jsonPath("$.type_id", is("7c8745be-d06f-4feb-b749-910efa1b986d")))
                .andExpect(jsonPath("$.type_uri",
                        is("http://www.digitalhps.org/types/TYPE_7c8745be-d06f-4feb-b749-910efa1b986d")))
                .andExpect(jsonPath("$.creator_id", is("admin"))).andExpect(jsonPath("$.description", is("Test-Type")));
    }

    @Test
    public void test_getTypeById_emptyResult() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/Type").accept(MediaType.APPLICATION_JSON_VALUE)
                .with(new RequestPostProcessor() {
                    public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                        request.setParameter("id", XMLConstants.TYPE_PREFIX + "7c8745be-d06f-4feb-b749-910efa1b986e");
                        return request;
                    }
                })).andExpect(status().isNotFound());
    }

    @Test
    public void test_getTypeById_nullTypeId() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/Type").accept(MediaType.APPLICATION_JSON_VALUE)
                .with(new RequestPostProcessor() {
                    public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                        return request;
                    }
                })).andExpect(status().isBadRequest());

    }

    @Test
    public void test_getTypeById_emptyTypeId() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/Type").accept(MediaType.APPLICATION_JSON_VALUE)
                .with(new RequestPostProcessor() {
                    public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                        request.setParameter("id", "");
                        return request;
                    }
                })).andExpect(status().isNotFound());
    }

}
