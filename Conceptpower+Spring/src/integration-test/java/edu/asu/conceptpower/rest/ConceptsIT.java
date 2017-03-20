package edu.asu.conceptpower.rest;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.apache.commons.io.IOUtil;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import edu.asu.conceptpower.IntegrationTest;

public class ConceptsIT extends IntegrationTest {

    @Test
    public void test_addConcept_success() throws Exception {
        final String input = IOUtil
                .toString(this.getClass().getClassLoader().getResourceAsStream("input/addConcept.json"));
        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/rest/concept/add").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(input).principal(principal))
                .andExpect(jsonPath("$.id", is(instanceOf(String.class)))).andExpect(status().isOk());
    }

    @Test
    public void test_addConcepts_success() throws Exception {
        final String input = IOUtil
                .toString(this.getClass().getClassLoader().getResourceAsStream("input/addConcepts.json"));
        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/rest/concepts/add").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(input).principal(principal))
                .andExpect(jsonPath("$[0].id", is(instanceOf(String.class))))
                .andExpect(jsonPath("$[1].id", is(instanceOf(String.class))))
                .andExpect(status().isOk());
    }

    @Test
    public void test_addConcepts_invalidConceptList() throws Exception {
        final String input = IOUtil.toString(
                this.getClass().getClassLoader().getResourceAsStream("input/addConceptsWithInvalidConceptList.json"));
        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/rest/concepts/add").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(input).principal(principal))
                .andExpect(jsonPath("$.[0].error_message", is("Specified dictionary does not exist in Conceptpower.")))
                .andExpect(jsonPath("$.[0].success", is(false))).andExpect(jsonPath("$.[0].word", is("kitty")))
                .andExpect(jsonPath("$.[1].error_message", is("Specified dictionary does not exist in Conceptpower.")))
                .andExpect(jsonPath("$.[1].success", is(false))).andExpect(jsonPath("$.[1].word", is("kitty-2")));
    }

    @Test
    public void test_addConcept_invalidConceptList() throws Exception {
        final String input = IOUtil.toString(
                this.getClass().getClassLoader().getResourceAsStream("input/addConceptWithInvalidConceptList.json"));
        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/rest/concept/add").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(input).principal(principal))
                .andExpect(content().string("Specified concept list does not exist in Conceptpower."))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void test_addConcept_invalidPos() throws Exception {
        final String input = IOUtil
                .toString(this.getClass().getClassLoader().getResourceAsStream("input/addConceptWithInvalidPos.json"));
        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/rest/concept/add").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(input).principal(principal))
                .andExpect(content().string("POS 'noun2' does not exist.")).andExpect(status().isBadRequest());

    }

    @Test
    public void test_addConcept_nullWord() throws Exception {
        final String input = IOUtil
                .toString(this.getClass().getClassLoader().getResourceAsStream("input/addConceptWithNullWord.json"));
        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/rest/concept/add").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(input).principal(principal))
                .andExpect(content()
                        .string("Error parsing request: please provide a word for the concept ('word' attribute)."))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void test_addConcept_emptyConceptList() throws Exception {
        final String input = IOUtil
                .toString(this.getClass().getClassLoader().getResourceAsStream("input/addConceptWithEmptyList.json"));
        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/rest/concept/add").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(input).principal(principal))
                .andExpect(content()
                        .string("Error parsing request: please provide a concept list for the concept ('conceptlist' attribute)."))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void test_addConcept_emptyType() throws Exception {
        final String input = IOUtil
                .toString(this.getClass().getClassLoader().getResourceAsStream("input/addConceptWithEmptyType.json"));
        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/rest/concept/add").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(input).principal(principal))
                .andExpect(content()
                        .string("Error parsing request: please provide a type for the concept ('type' attribute)."))
                .andExpect(status().isBadRequest());

    }

}
