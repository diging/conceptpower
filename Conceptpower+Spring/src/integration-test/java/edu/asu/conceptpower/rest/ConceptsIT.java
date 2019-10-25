package edu.asu.conceptpower.rest;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.Is.isA;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.asu.conceptpower.IntegrationTest;

public class ConceptsIT extends IntegrationTest {

    public ObjectMapper objectMapper = new ObjectMapper();
    @Test
    public void test_addConcept_success() throws Exception {
        final String input = IOUtils
                .toString(this.getClass().getClassLoader().getResourceAsStream("input/addConcept.json"), "UTF-8");
        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/concept/add").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(input).principal(principal))
                .andExpect(jsonPath("$.conceptEntries[0].id", isA(String.class))).andExpect(status().isOk());
    }

    @Test
    public void test_addConcept_successForConceptWrapper() throws Exception {
        final String input = IOUtils
                .toString(this.getClass().getClassLoader().getResourceAsStream("input/addConceptWrapper.json"), "UTF-8");
        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/concept/add").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(input).principal(principal))
                .andExpect(jsonPath("$.conceptEntries[0].id", isA(String.class))).andExpect(status().isOk());
    }

    @Test
    public void test_addConcepts_success() throws Exception {
        final String input = IOUtils
                .toString(this.getClass().getClassLoader().getResourceAsStream("input/addConcepts.json"), "UTF-8");
        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/concepts/add").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(input).principal(principal))
                .andExpect(jsonPath("$.conceptEntries[0].id", is(instanceOf(String.class))))
                .andExpect(jsonPath("$.conceptEntries[1].id", is(instanceOf(String.class)))).andExpect(status().isOk());
    }

    @Test
    public void test_addConcepts_invalidConceptList() throws Exception {
        final String input = IOUtils.toString(
                this.getClass().getClassLoader().getResourceAsStream("input/addConceptsWithInvalidConceptList.json"), "UTF-8");
        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/concepts/add").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(input).principal(principal))
                .andExpect(jsonPath("$.message", is("No concept entry found.")));
    }

    @Test
    public void test_addConcept_invalidConceptList() throws Exception {
        final String input = IOUtils.toString(
                this.getClass().getClassLoader().getResourceAsStream("input/addConceptWithInvalidConceptList.json"), "UTF-8");
        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/concept/add").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(input).principal(principal))
                .andExpect(content().string(objectMapper.writeValueAsString("Specified concept list does not exist in Conceptpower.")))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void test_addConcept_invalidPos() throws Exception {
        final String input = IOUtils
                .toString(this.getClass().getClassLoader().getResourceAsStream("input/addConceptWithInvalidPos.json"), "UTF-8");
        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/concept/add").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(input).principal(principal))
                .andExpect(content().string(objectMapper.writeValueAsString("Error parsing request: please provide a valid POS ('pos' attribute).")))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void test_addConcept_nullWord() throws Exception {
        final String input = IOUtils
                .toString(this.getClass().getClassLoader().getResourceAsStream("input/addConceptWithNullWord.json"), "UTF-8");
        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/concept/add").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(input).principal(principal))
                .andExpect(content()
                        .string(objectMapper.writeValueAsString("Error parsing request: please provide a word for the concept ('word' attribute).")))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void test_addConcept_emptyConceptList() throws Exception {
        final String input = IOUtils
                .toString(this.getClass().getClassLoader().getResourceAsStream("input/addConceptWithEmptyList.json"), "UTF-8");
        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/concept/add").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(input).principal(principal))
                .andExpect(content()
                        .string(objectMapper.writeValueAsString("Error parsing request: please provide a list name for the concept ('list' attribute).")))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void test_addConcept_emptyType() throws Exception {
        final String input = IOUtils
                .toString(this.getClass().getClassLoader().getResourceAsStream("input/addConceptWithEmptyType.json"), "UTF-8");
        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/concept/add").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(input).principal(principal))
                .andExpect(content()
                        .string(new ObjectMapper().writeValueAsString("Error parsing request: please provide a type for the concept ('type' attribute).")))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void test_addConcept_invalidWordnetIds() throws Exception {
        String wordnetId = "WORDNET-123";
        final String input = IOUtils.toString(
                this.getClass().getClassLoader().getResourceAsStream("input/addConceptWithInvalidWordNetIds.json"), "UTF-8");
        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/concept/add").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(input).principal(principal))
                .andExpect(content()
                        .string(new ObjectMapper().writeValueAsString("Error parsing request: please provide a valid list of wordnet ids seperated by commas. Wordnet id "
                                + wordnetId + " doesn't exist.")))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void test_addConcept_existingwrappedWordnetIds() throws Exception {
        final String input = IOUtils
                .toString(this.getClass().getClassLoader().getResourceAsStream("input/addConceptWrapper.json"), "UTF-8");
        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/concept/add").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(input).principal(principal))
                .andExpect(content()
                        .string(objectMapper.writeValueAsString("Error parsing request: the WordNet concept you are trying to wrap is already wrapped.")))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void test_addConcept_posMismatch() throws Exception {
        final String input = IOUtils
                .toString(this.getClass().getClassLoader().getResourceAsStream("input/addConceptWithPosMisMatch.json"), "UTF-8");
        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/concept/add").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(input).principal(principal))
                .andExpect(content()
                        .string(objectMapper.writeValueAsString("Error parsing request: please enter POS that matches with the wordnet POS NOUN")))
                .andExpect(status().isBadRequest());

    }

}
