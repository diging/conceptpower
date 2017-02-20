package edu.asu.conceptpower.servlet.rest;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.servlet.ServletContext;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import edu.asu.conceptpower.IntegrationTest;

public class ConceptsImplIT extends IntegrationTest {

    @Test
    public void testForWebApplicationContext() {
        ServletContext servletContext = wac.getServletContext();
        Assert.assertNotNull(servletContext);
        Assert.assertTrue(servletContext instanceof MockServletContext);
    }

    @Test
    public void testForConceptSearchByID() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/Concept").param("id", "WID-02380464-N-01-polo_pony")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$conceptEntries[0].id", is("WID-02380464-N-01-polo_pony")))
                .andExpect(jsonPath("$conceptEntries[0].description",
                        is("a small agile horse specially bred and trained for playing polo")))
                .andExpect(jsonPath("$conceptEntries[0].conceptList", is("WordNet")))
                .andExpect(jsonPath("$conceptEntries[0].lemma", is("polo pony")))
                .andExpect(jsonPath("$conceptEntries[0].deleted", is(false)))
                .andExpect(jsonPath("$conceptEntries[0].pos", is("NOUN")))
                .andExpect(jsonPath("$conceptEntries[0].concept_uri",
                        is("http://www.digitalhps.org/concepts/WID-02380464-N-01-polo_pony")))
                .andExpect(
                        jsonPath("$conceptEntries[0].alternativeIds[0].concept_id", is("WID-02380464-N-01-polo_pony")))
                .andExpect(status().isOk());

    }

    @Test
    public void addConcept() throws Exception {
        String input = "{ \"word\": \"kitty\", \"pos\": \"noun\", \"conceptlist\": \"TestList\", \"description\": \"Soft kitty, sleepy kitty, little ball of fur.\", \"type\": \"7c8745be-d06f-4feb-b749-910efa1b986d\"}";
        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/concept/add").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(input).principal(principal))
                .andDo(MockMvcResultHandlers.print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(String.class)));
    }

    @Test
    public void addConcepts() throws Exception {
        String input = "[{\"word\": \"kitty\", \"pos\": \"noun\", \"conceptlist\": \"TestList\", \"description\": \"Soft kitty, sleepy kitty.\", \"type\": \"7c8745be-d06f-4feb-b749-910efa1b986d\"}]";
        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/concepts/add").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(input).principal(principal))
                .andDo(MockMvcResultHandlers.print()).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(String.class)));
    }

}
