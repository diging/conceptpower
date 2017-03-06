package edu.asu.conceptpower.rest;

import static org.hamcrest.core.Is.isA;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import edu.asu.conceptpower.IntegrationTest;

public class ConceptsIT extends IntegrationTest {

    public void addConcept() throws Exception {
        String input = "{ \"word\": \"kitty\", \"pos\": \"noun\", \"conceptlist\": \"TestList\", \"description\": \"Soft kitty, sleepy kitty, little ball of fur.\", \"type\": \"7c8745be-d06f-4feb-b749-910efa1b986d\"}";
        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/concept/add").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(input).principal(principal))

.andExpect(jsonPath("$.id", isA(String.class)))
                .andExpect(status().isOk());
    }

    @Test
    public void addConcepts() throws Exception {
        String input = "[{\"word\": \"kitty\", \"pos\": \"noun\", \"conceptlist\": \"TestList\", \"description\": \"Soft kitty, sleepy kitty.\", \"type\": \"7c8745be-d06f-4feb-b749-910efa1b986d\"},"
                + "{\"word\": \"kitty-2\", \"pos\": \"noun\", \"conceptlist\": \"TestList\", \"description\": \"Soft kitty, sleepy kitty - 2.\", \"type\": \"7c8745be-d06f-4feb-b749-910efa1b986d\"}]";
        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/concepts/add").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(input).principal(principal))
                .andExpect(jsonPath("$[0].id", isA(String.class)))
                .andExpect(jsonPath("$[1].id", isA(String.class))).andExpect(status().isOk());
    }

}
