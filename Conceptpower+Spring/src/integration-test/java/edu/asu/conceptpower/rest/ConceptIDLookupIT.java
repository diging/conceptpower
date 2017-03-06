package edu.asu.conceptpower.rest;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import edu.asu.conceptpower.IntegrationTest;

public class ConceptIDLookupIT extends IntegrationTest {

    @Test
    public void test_getConceptById_successForWordNetId() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/Concept").param("id", "WID-02380464-N-01-polo_pony")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.conceptEntries[0].id", is("WID-02380464-N-01-polo_pony")))
                .andExpect(jsonPath("$.conceptEntries[0].description",
                        is("a small agile horse specially bred and trained for playing polo")))
                .andExpect(jsonPath("$.conceptEntries[0].conceptList", is("WordNet")))
                .andExpect(jsonPath("$.conceptEntries[0].lemma", is("polo pony")))
                .andExpect(jsonPath("$.conceptEntries[0].deleted", is(false)))
                .andExpect(jsonPath("$.conceptEntries[0].pos", is("NOUN")))
                .andExpect(jsonPath("$.conceptEntries[0].concept_uri",
                        is("http://www.digitalhps.org/concepts/WID-02380464-N-01-polo_pony")))
                .andExpect(
                        jsonPath("$.conceptEntries[0].alternativeIds[0].concept_id", is("WID-02380464-N-01-polo_pony")))
                .andExpect(status().isOk());

    }

    @Test
    public void test_getConceptById_successForGenericWordNetId() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/Concept").param("id", "WID-02380464-N-??-polo_pony")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(jsonPath("$.conceptEntries[0].id", is("WID-02380464-N-01-polo_pony")))
                .andExpect(jsonPath("$.conceptEntries[0].description",
                        is("a small agile horse specially bred and trained for playing polo")))
                .andExpect(jsonPath("$.conceptEntries[0].conceptList", is("WordNet")))
                .andExpect(jsonPath("$.conceptEntries[0].lemma", is("polo pony")))
                .andExpect(jsonPath("$.conceptEntries[0].deleted", is(false)))
                .andExpect(jsonPath("$.conceptEntries[0].pos", is("NOUN")))
                .andExpect(jsonPath("$.conceptEntries[0].concept_uri",
                        is("http://www.digitalhps.org/concepts/WID-02380464-N-01-polo_pony")))
                .andExpect(
                        jsonPath("$.conceptEntries[0].alternativeIds[0].concept_id", is("WID-02380464-N-??-polo_pony")))
                .andExpect(
                        jsonPath("$.conceptEntries[0].alternativeIds[1].concept_id", is("WID-02380464-N-01-polo_pony")))
                .andExpect(status().isOk());

    }

    @Test(expected = IllegalArgumentException.class)
    public void test_getConceptById_invalidWordNetId() throws Exception {
        this.mockMvc.perform(
                MockMvcRequestBuilders.get("/Concept").param("id", null).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void test_getConceptById_successForLocalConceptId() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders.get("/Concept").param("id", "CONIHJWnURicfA3")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.conceptEntries[0].id", is("CONIHJWnURicfA3")))
                .andExpect(jsonPath("$.conceptEntries[0].description", is("pony test description")))
                .andExpect(jsonPath("$.conceptEntries[0].conceptList", is("TestList")))
                .andExpect(jsonPath("$.conceptEntries[0].lemma", is("pony")))
                .andExpect(jsonPath("$.conceptEntries[0].deleted", is(false)))
                .andExpect(jsonPath("$.conceptEntries[0].pos", is("noun")))
                .andExpect(jsonPath("$.conceptEntries[0].concept_uri",
                        is("http://www.digitalhps.org/concepts/CONIHJWnURicfA3")))
                .andExpect(status().isOk());
    }

    @Test
    public void test_getConceptById_successForConceptWrapper() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/Concept").param("id", "CONzpOmUhTDbJV4")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.conceptEntries[0].id", is("CONzpOmUhTDbJV4")))
                .andExpect(jsonPath("$.conceptEntries[0].description", is("Gustav Robert Kirchhoff wrapper")))
                .andExpect(jsonPath("$.conceptEntries[0].type.type_id", is("7c8745be-d06f-4feb-b749-910efa1b986d")))
                .andExpect(jsonPath("$.conceptEntries[0].type.type_uri",
                        is("http://www.digitalhps.org/types/TYPE_7c8745be-d06f-4feb-b749-910efa1b986d")))
                .andExpect(jsonPath("$.conceptEntries[0].type.type_name", is("Test-Type")))
                .andExpect(jsonPath("$.conceptEntries[0].alternativeIds[0].concept_id", is("CONzpOmUhTDbJV4")))
                .andExpect(jsonPath("$.conceptEntries[0].alternativeIds[0].concept_uri",
                        is("http://www.digitalhps.org/concepts/CONzpOmUhTDbJV4")))
                .andExpect(jsonPath("$.conceptEntries[0].alternativeIds[1].concept_id",
                        is("WID-11105945-N-03-Gustav_Robert_Kirchhoff")))
                .andExpect(jsonPath("$.conceptEntries[0].alternativeIds[1].concept_uri",
                        is("http://www.digitalhps.org/concepts/WID-11105945-N-03-Gustav_Robert_Kirchhoff")))
                .andExpect(jsonPath("$.conceptEntries[0].conceptList", is("TestList")))
                .andExpect(jsonPath("$.conceptEntries[0].creator_id", is("admin")))
                .andExpect(jsonPath("$.conceptEntries[0].modified_by", is("admin")))
                .andExpect(jsonPath("$.conceptEntries[0].synonym_ids",
                        is("WID-11105945-N-01-Kirchhoff,WID-11105945-N-02-G._R._Kirchhoff,")))
                .andExpect(jsonPath("$.conceptEntries[0].lemma", is("Gustav Robert Kirchhoff")))
                .andExpect(jsonPath("$.conceptEntries[0].deleted", is(false)))
                .andExpect(jsonPath("$.conceptEntries[0].pos", is("noun")))
                .andExpect(jsonPath("$.conceptEntries[0].wordnet_id", is("WID-11105945-N-03-Gustav_Robert_Kirchhoff,")))
                .andExpect(jsonPath("$.conceptEntries[0].equal_to", is("http://viaf.org/viaf/2587371")))
                .andExpect(jsonPath("$.conceptEntries[0].concept_uri",
                        is("http://www.digitalhps.org/concepts/CONzpOmUhTDbJV4")))
                .andExpect(status().isOk());
    }

}
