package edu.asu.conceptpower.rest;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.apache.commons.io.IOUtil;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import edu.asu.conceptpower.IntegrationTest;

public class ConceptIDLookupIT extends IntegrationTest {

    @Test
    public void test_getConceptById_successForWordNetId() throws Exception {
        final String output = IOUtil
                .toString(this.getClass().getClassLoader().getResourceAsStream("output/wordnetConcept.json"));
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/Concept").param("id", "WID-02380464-N-01-polo_pony")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(output)).andExpect(status().isOk());

    }

    @Test
    public void test_getConceptById_successForGenericWordNetId() throws Exception {
        final String output = IOUtil
                .toString(this.getClass().getClassLoader().getResourceAsStream("output/conceptForGenericWordnet.json"));
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/Concept").param("id", "WID-02380464-N-??-polo_pony")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(output)).andExpect(status().isOk());

    }

    @Test(expected = IllegalArgumentException.class)
    public void test_getConceptById_invalidWordNetId() throws Exception {
        this.mockMvc.perform(
                MockMvcRequestBuilders.get("/Concept").param("id", null).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void test_getConceptById_successForLocalConceptId() throws Exception {
        final String output = IOUtil
                .toString(this.getClass().getClassLoader().getResourceAsStream("output/conceptForLocalId.json"));
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/Concept").param("id", "CONIHJWnURicfA3")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(output)).andExpect(status().isOk());
    }

    @Test
    public void test_getConceptById_successForConceptWrapper() throws Exception {
        final String output = IOUtil
                .toString(this.getClass().getClassLoader().getResourceAsStream("output/conceptWrapper.json"));
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/Concept").param("id", "CONzpOmUhTDbJV4")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print()).andExpect(content().string(output)).andExpect(status().isOk());
    }

}
