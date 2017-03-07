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
        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/concept/add").contentType(MediaType.APPLICATION_JSON_VALUE)
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
        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/concept/add").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(input).principal(principal))
                .andExpect(content().string("Specified concept list does not exist in Conceptpower."))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void test_addConcept_invalidPos() throws Exception {
        String input = "{ \"word\": \"kitty\", \"pos\": \"noun2\", \"conceptlist\": \"TestList\", \"description\": \"Soft kitty, sleepy kitty, little ball of fur.\", \"type\": \"7c8745be-d06f-4feb-b749-910efa1b986d\"}";
        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/concept/add").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(input).principal(principal))
                .andExpect(content().string("Error parsing request: please provide a valid POS ('pos' attribute)."))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void test_addConcept_nullWord() throws Exception {
        String input = "{\"pos\": \"noun\", \"conceptlist\": \"TestList\", \"description\": \"Soft kitty, sleepy kitty, little ball of fur.\", \"type\": \"7c8745be-d06f-4feb-b749-910efa1b986d\"}";
        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/concept/add").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(input).principal(principal))
                .andExpect(content()
                        .string("Error parsing request: please provide a word for the concept ('word' attribute)."))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void test_addConcept_emptyConceptList() throws Exception {

        String input = "{ \"word\": \"kitty\", \"pos\": \"noun\", \"description\": \"Soft kitty, sleepy kitty, little ball of fur.\", \"type\": \"7c8745be-d06f-4feb-b749-910efa1b986d\"}";
        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/concept/add").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(input).principal(principal))
                .andExpect(content()
                        .string("Error parsing request: please provide a list name for the concept ('list' attribute)."))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void test_addConcept_emptyType() throws Exception {

        String input = "{ \"word\": \"kitty\", \"pos\": \"noun\", \"conceptlist\": \"TestList\", \"description\": \"Soft kitty, sleepy kitty, little ball of fur.\"}";
        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/concept/add").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(input).principal(principal))
                .andExpect(content()
                        .string("Error parsing request: please provide a type for the concept ('type' attribute)."))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void test_addConcept_invalidWordnetIds() throws Exception {
        String wordnetId1 = "WORDNET-123";
        String wordnetId2 = "WORDNET-456";
        String input = "{ \"word\": \"kitty\", \"pos\": \"noun\", \"conceptlist\": \"TestList\", \"description\": \"Soft kitty, sleepy kitty, little ball of fur.\", \"type\": \"7c8745be-d06f-4feb-b749-910efa1b986d\",\"wordnetIds\":\""
                + wordnetId1 + "," + wordnetId2 + " \"}";
        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/concept/add").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(input).principal(principal))
                .andExpect(content()
                        .string("Error parsing request: please provide a valid list of wordnet ids seperated by commas. Wordnet id "
                                + wordnetId1 + " doesn't exist."))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void test_addConcept_wrappedWordnetIds() throws Exception {
        String wordnetId1 = "WID-11105945-N-03-Gustav_Robert_Kirchhoff";
        String input = "{ \"word\": \"kitty\", \"pos\": \"noun\", \"conceptlist\": \"TestList\", \"description\": \"Soft kitty, sleepy kitty, little ball of fur.\", \"type\": \"7c8745be-d06f-4feb-b749-910efa1b986d\",\"wordnetIds\": \""
                + wordnetId1 + "\"}";
        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/concept/add").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(input).principal(principal))
                .andExpect(content()
                        .string("Error parsing request: the WordNet concept you are trying to wrap is already wrapped."))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void test_addConcept_posMismatch() throws Exception {
        String wordnetId1 = "WID-02380335-N-01-pony";
        String input = "{ \"word\": \"pony\", \"pos\": \"verb\", \"conceptlist\": \"TestList\", \"description\": \"Soft kitty, sleepy kitty, little ball of fur.\", \"type\": \"7c8745be-d06f-4feb-b749-910efa1b986d\",\"wordnetIds\": \""
                + wordnetId1 + "\"}";
        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/concept/add").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(input).principal(principal))
                .andExpect(content()
                        .string("Error parsing request: please enter POS that matches with the wordnet POS NOUN"))
                .andExpect(status().isBadRequest());

    }

}
