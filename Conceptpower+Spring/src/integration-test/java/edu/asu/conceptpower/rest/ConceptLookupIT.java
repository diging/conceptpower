package edu.asu.conceptpower.rest;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import edu.asu.conceptpower.IntegrationTest;

public class ConceptLookupIT extends IntegrationTest {

    @Test
    public void test_getWordNetEntry_successForSingleEntry() throws Exception {

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/ConceptLookup/Gustav Robert Kirchhoff/noun")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.conceptEntries[0].id", is("CONzpOmUhTDbJV4")))
                .andExpect(jsonPath("$.conceptEntries[0].description", is("Gustav Robert Kirchhoff wrapper")))
                .andExpect(jsonPath("$.conceptEntries[0].pos", is("noun")))
                .andExpect(jsonPath("$.conceptEntries[0].conceptList", is("TestList")))
                .andExpect(jsonPath("$.conceptEntries[0].type.type_id", is("7c8745be-d06f-4feb-b749-910efa1b986d")))
                .andExpect(jsonPath("$.conceptEntries[0].type.type_uri",
                        is("http://www.digitalhps.org/types/TYPE_7c8745be-d06f-4feb-b749-910efa1b986d")))
                .andExpect(jsonPath("$.conceptEntries[0].type.type_name", is("Test-Type")))
                .andExpect(jsonPath("$.conceptEntries[0].deleted", is(false)))
                .andExpect(jsonPath("$.conceptEntries[0].creator_id", is("admin")))
                .andExpect(jsonPath("$.conceptEntries[0].equal_to", is("http://viaf.org/viaf/2587371")))
                .andExpect(jsonPath("$.conceptEntries[0].wordnet_id", is("WID-11105945-N-03-Gustav_Robert_Kirchhoff")))
                .andExpect(jsonPath("$.conceptEntries[0].equal_to", is("http://viaf.org/viaf/2587371")))
                .andExpect(status().isOk());
    }

    @Test
    public void test_getWordNetEntry_successForMultipleEntry() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/ConceptLookup/einstein/noun")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.conceptEntries.length()", is(4)))
                .andExpect(jsonPath("$.conceptEntries[0].id", is("WID-10954498-N-02-Albert_Einstein")))
                .andExpect(jsonPath("$.conceptEntries[0].lemma", is("albert einstein")))
                .andExpect(jsonPath("$.conceptEntries[0].pos", is("noun")))
                .andExpect(jsonPath("$.conceptEntries[0].description",
                        is("physicist born in Germany who formulated the special theory of relativity and the general theory of relativity; Einstein also proposed that light consists of discrete quantized bundles of energy (later called photons) (1879-1955)")))
                .andExpect(jsonPath("$.conceptEntries[0].conceptList", is("WordNet")))
                .andExpect(jsonPath("$.conceptEntries[0].deleted", is(false)))
                .andExpect(jsonPath("$.conceptEntries[0].concept_uri",
                        is("http://www.digitalhps.org/concepts/WID-10954498-N-02-Albert_Einstein")))
                .andExpect(jsonPath("$.conceptEntries[0].synonym_ids", is("WID-10954498-N-01-Einstein,")))
                .andExpect(jsonPath("$.conceptEntries[0].wordnet_id", is("WID-10954498-N-02-Albert_Einstein")))
                .andExpect(jsonPath("$.conceptEntries[1].id", is("WID-10954498-N-01-Einstein")))
                .andExpect(jsonPath("$.conceptEntries[1].lemma", is("einstein")))
                .andExpect(jsonPath("$.conceptEntries[1].pos", is("noun")))
                .andExpect(jsonPath("$.conceptEntries[1].description",
                        is("physicist born in Germany who formulated the special theory of relativity and the general theory of relativity; Einstein also proposed that light consists of discrete quantized bundles of energy (later called photons) (1879-1955)")))
                .andExpect(jsonPath("$.conceptEntries[1].conceptList", is("WordNet")))
                .andExpect(jsonPath("$.conceptEntries[1].deleted", is(false)))
                .andExpect(jsonPath("$.conceptEntries[1].concept_uri",
                        is("http://www.digitalhps.org/concepts/WID-10954498-N-01-Einstein")))
                .andExpect(jsonPath("$.conceptEntries[1].synonym_ids", is("WID-10954498-N-02-Albert_Einstein,")))
                .andExpect(jsonPath("$.conceptEntries[1].wordnet_id", is("WID-10954498-N-01-Einstein")))
                .andExpect(jsonPath("$.conceptEntries[2].id", is("WID-05875723-N-01-Bose-Einstein_statistics")))
                .andExpect(jsonPath("$.conceptEntries[2].lemma", is("bose-einstein statistics")))
                .andExpect(jsonPath("$.conceptEntries[2].pos", is("noun")))
                .andExpect(jsonPath("$.conceptEntries[2].description",
                        is("(physics) statistical law obeyed by a system of particles whose wave function is not changed when two particles are interchanged (the Pauli exclusion principle does not apply)")))
                .andExpect(jsonPath("$.conceptEntries[2].conceptList", is("WordNet")))
                .andExpect(jsonPath("$.conceptEntries[2].deleted", is(false)))
                .andExpect(jsonPath("$.conceptEntries[2].concept_uri",
                        is("http://www.digitalhps.org/concepts/WID-05875723-N-01-Bose-Einstein_statistics")))
                .andExpect(jsonPath("$.conceptEntries[2].wordnet_id", is("WID-05875723-N-01-Bose-Einstein_statistics")))
                .andExpect(jsonPath("$.conceptEntries[3].id", is("WID-10126926-N-05-Einstein")))
                .andExpect(jsonPath("$.conceptEntries[3].lemma", is("einstein")))
                .andExpect(jsonPath("$.conceptEntries[3].pos", is("noun")))
                .andExpect(jsonPath("$.conceptEntries[3].description",
                        is("someone who has exceptional intellectual ability and originality; \"Mozart was a child genius\"; \"he's smart but he's no Einstein\"")))
                .andExpect(jsonPath("$.conceptEntries[3].conceptList", is("WordNet")))
                .andExpect(jsonPath("$.conceptEntries[3].deleted", is(false)))
                .andExpect(jsonPath("$.conceptEntries[3].concept_uri",
                        is("http://www.digitalhps.org/concepts/WID-10126926-N-05-Einstein")))
                .andExpect(jsonPath("$.conceptEntries[3].synonym_ids",
                        is("WID-10126926-N-01-genius,WID-10126926-N-02-mastermind,WID-10126926-N-03-brain,WID-10126926-N-04-brainiac,")))
                .andExpect(jsonPath("$.conceptEntries[3].wordnet_id", is("WID-10126926-N-05-Einstein")))
                .andExpect(status().isOk());
    }

}
