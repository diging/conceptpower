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

}
