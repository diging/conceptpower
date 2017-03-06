package edu.asu.conceptpower.rest;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.Is.isA;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import edu.asu.conceptpower.IntegrationTest;

public class ConceptsIT extends IntegrationTest {

    public void test_addConcept_success() throws Exception {
        String input = "{ \"word\": \"kitty\", \"pos\": \"noun\", \"conceptlist\": \"TestList\", \"description\": \"Soft kitty, sleepy kitty, little ball of fur.\", \"type\": \"7c8745be-d06f-4feb-b749-910efa1b986d\"}";
        this.mockMvc.perform(MockMvcRequestBuilders.post("/concept/add").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(input).principal(principal))
                .andExpect(jsonPath("$.id", isA(String.class))).andExpect(status().isOk());
    }

    @Test
    public void test_addConcepts_success() throws Exception {
        String input = "[{\"word\": \"kitty\", \"pos\": \"noun\", \"conceptlist\": \"TestList\", \"description\": \"Soft kitty, sleepy kitty.\", \"type\": \"7c8745be-d06f-4feb-b749-910efa1b986d\"},"
                + "{\"word\": \"kitty-2\", \"pos\": \"noun\", \"conceptlist\": \"TestList\", \"description\": \"Soft kitty, sleepy kitty - 2.\", \"type\": \"7c8745be-d06f-4feb-b749-910efa1b986d\"}]";
        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/concepts/add").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(input).principal(principal))
                .andExpect(jsonPath("$[0].id", isA(String.class))).andExpect(jsonPath("$[1].id", isA(String.class)))
                .andExpect(status().isOk());
    }

    @Test
    public void test_addConcepts_invalidConceptList() throws Exception {
        String input = "[{\"word\": \"kitty\", \"pos\": \"noun\", \"conceptlist\": \"ConceptListInvalid\", \"description\": \"Soft kitty, sleepy kitty.\", \"type\": \"7c8745be-d06f-4feb-b749-910efa1b986d\"},"
                + "{\"word\": \"kitty-2\", \"pos\": \"noun\", \"conceptlist\": \"ConceptListInvalid\", \"description\": \"Soft kitty, sleepy kitty - 2.\", \"type\": \"7c8745be-d06f-4feb-b749-910efa1b986d\"}]";
        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/concepts/add").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(input).principal(principal))
                .andExpect(jsonPath("$.[0].error_message", is("Specified dictionary does not exist in Conceptpower.")))
                .andExpect(jsonPath("$.[0].success", is(false))).andExpect(jsonPath("$.[0].word", is("kitty")))
                .andExpect(jsonPath("$.[1].error_message", is("Specified dictionary does not exist in Conceptpower.")))
                .andExpect(jsonPath("$.[1].success", is(false))).andExpect(jsonPath("$.[1].word", is("kitty-2")));
    }

    @Test
    public void test_addConcept_invalidConceptList() throws Exception {
        String input = "{ \"word\": \"kitty\", \"pos\": \"noun\", \"conceptlist\": \"ConceptListInvalid\", \"description\": \"Soft kitty, sleepy kitty, little ball of fur.\", \"type\": \"7c8745be-d06f-4feb-b749-910efa1b986d\"}";
        this.mockMvc.perform(MockMvcRequestBuilders.post("/concept/add").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(input).principal(principal))
                .andExpect(content().string("Specified concept list does not exist in Conceptpower."))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void test_addConcept_invalidPos() throws Exception {
        String input = "{ \"word\": \"kitty\", \"pos\": \"noun2\", \"conceptlist\": \"TestList\", \"description\": \"Soft kitty, sleepy kitty, little ball of fur.\", \"type\": \"7c8745be-d06f-4feb-b749-910efa1b986d\"}";
        this.mockMvc.perform(MockMvcRequestBuilders.post("/concept/add").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(input).principal(principal))
                .andExpect(content().string("Error parsing request: please provide a valid POS ('pos' attribute)."))
                .andExpect(status().isBadRequest());

    }

}
