package edu.asu.conceptpower.rest;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.apache.commons.io.IOUtil;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import edu.asu.conceptpower.IntegrationTest;

public class ConceptLookupIT extends IntegrationTest {

    @Test
    public void test_getWordNetEntry_successForSingleEntry() throws Exception {
        final String output = IOUtil.toString(
                this.getClass().getClassLoader().getResourceAsStream("output/conceptLookUpWordNetEntry.json"));
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/ConceptLookup/Gustav Robert Kirchhoff/noun")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(output))
                .andExpect(status().isOk());
    }

    @Test
    public void test_getWordNetEntry_noResults() throws Exception {

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/ConceptLookup/Gustav Robert Kirchhoff/verb")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", is("No concept entry found.")))
                .andExpect(status().isOk());
    }
    @Test
    public void test_getWordNetEntry_successForMultipleEntry() throws Exception {
        final String output = IOUtil.toString(
                this.getClass().getClassLoader().getResourceAsStream("output/conceptLookUpForMultipletEntry.json"));
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/ConceptLookup/einstein/noun")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(output))
                .andExpect(status().isOk());
    }

}
