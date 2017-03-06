package edu.asu.conceptpower.rest;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import edu.asu.conceptpower.IntegrationTest;

public class ConceptSearchIT extends IntegrationTest {

    @Test
    public void test_searchConcept_searchWithWordAndPos() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders.get("/ConceptSearch").param("word", "einstein").param("pos", "noun")
                .param("operator", SearchParamters.OP_AND).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.conceptEntries[0].id", is("WID-10954498-N-01-Einstein")))
                .andExpect(jsonPath("$.conceptEntries[0].lemma", is("einstein")))
                .andExpect(jsonPath("$.conceptEntries[0].pos", is("noun")))
                .andExpect(jsonPath("$.conceptEntries[0].description",
                        is("physicist born in Germany who formulated the special theory of relativity and the general theory of relativity; Einstein also proposed that light consists of discrete quantized bundles of energy (later called photons) (1879-1955)")))
                .andExpect(jsonPath("$.conceptEntries[0].conceptList", is("WordNet")))
                .andExpect(jsonPath("$.conceptEntries[0].deleted", is(false)))
                .andExpect(jsonPath("$.conceptEntries[0].concept_uri",
                        is("http://www.digitalhps.org/concepts/WID-10954498-N-01-Einstein")))
                .andExpect(jsonPath("$.conceptEntries[0].synonym_ids", is("WID-10954498-N-02-Albert_Einstein,")))
                .andExpect(jsonPath("$.conceptEntries[0].wordnet_id", is("WID-10954498-N-01-Einstein")))
                .andExpect(jsonPath("$.conceptEntries[1].id", is("WID-10126926-N-05-Einstein")))
                .andExpect(jsonPath("$.conceptEntries[1].lemma", is("einstein")))
                .andExpect(jsonPath("$.conceptEntries[1].pos", is("noun")))
                .andExpect(jsonPath("$.conceptEntries[1].description",
                        is("someone who has exceptional intellectual ability and originality; \"Mozart was a child genius\"; \"he's smart but he's no Einstein\"")))
                .andExpect(jsonPath("$.conceptEntries[1].conceptList", is("WordNet")))
                .andExpect(jsonPath("$.conceptEntries[1].deleted", is(false)))
                .andExpect(jsonPath("$.conceptEntries[1].concept_uri",
                        is("http://www.digitalhps.org/concepts/WID-10126926-N-05-Einstein")))
                .andExpect(jsonPath("$.conceptEntries[1].synonym_ids",
                        is("WID-10126926-N-01-genius,WID-10126926-N-02-mastermind,WID-10126926-N-03-brain,WID-10126926-N-04-brainiac,")))
                .andExpect(jsonPath("$.conceptEntries[1].wordnet_id", is("WID-10126926-N-05-Einstein")))
                .andExpect(jsonPath("$.pagination.pageNumber", is(1)))
                .andExpect(jsonPath("$.pagination.totalNumberOfRecords", is(4)))
                .andExpect(status().isOk());

    }

    @Test
    public void test_searchConcept_searchWithWordAndPosAndConceptList() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/ConceptSearch").param("word", "Gustav Robert Kirchhoff")
                        .param("pos", "noun").param("concept_list", "TestList")
                        .param("operator", SearchParamters.OP_AND).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.conceptEntries[0].id", is("CONzpOmUhTDbJV4")))
                .andExpect(jsonPath("$.conceptEntries[0].lemma", is("Gustav Robert Kirchhoff")))
                .andExpect(jsonPath("$.conceptEntries[0].pos", is("noun")))
                .andExpect(jsonPath("$.conceptEntries[0].description", is("Gustav Robert Kirchhoff wrapper")))
                .andExpect(jsonPath("$.conceptEntries[0].conceptList", is("TestList")))
                .andExpect(jsonPath("$.conceptEntries[0].type.type_id", is("7c8745be-d06f-4feb-b749-910efa1b986d")))
                .andExpect(jsonPath("$.conceptEntries[0].type.type_uri",
                        is("http://www.digitalhps.org/types/TYPE_7c8745be-d06f-4feb-b749-910efa1b986d")))
                .andExpect(jsonPath("$.conceptEntries[0].type.type_name", is("Test-Type")))
                .andExpect(jsonPath("$.conceptEntries[0].deleted", is(false)))
                .andExpect(jsonPath("$.conceptEntries[0].concept_uri",
                        is("http://www.digitalhps.org/concepts/CONzpOmUhTDbJV4")))
                .andExpect(jsonPath("$.conceptEntries[0].creator_id", is("admin")))
                .andExpect(jsonPath("$.conceptEntries[0].equal_to", is("http://viaf.org/viaf/2587371")))
                .andExpect(jsonPath("$.conceptEntries[0].similar_to", is("http://viaf.org/viaf/2587372")))
                .andExpect(jsonPath("$.conceptEntries[0].wordnet_id", is("WID-11105945-N-03-Gustav_Robert_Kirchhoff")))
                .andExpect(jsonPath("$.pagination.pageNumber", is(1)))
                .andExpect(jsonPath("$.pagination.totalNumberOfRecords", is(1)))
                .andExpect(status().isOk());

    }

    @Test
    public void test_searchConcept_noResults() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders.get("/ConceptSearch").param("word", "Gustav Robert Kirchhoff")
                .param("pos", "verb").param("concept_list", "TestList").param("operator", SearchParamters.OP_AND)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", is("No records found for the search condition.")))
                .andExpect(status().isOk());

    }

    @Test
    public void test_searchConcept_searchWithWordAndPosAndEqualTo() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/ConceptSearch").param("word", "Gustav Robert Kirchhoff")
                        .param("pos", "noun").param("equal_to", "http://viaf.org/viaf/2587371")
                        .param("operator", SearchParamters.OP_AND).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.conceptEntries[0].id", is("CONzpOmUhTDbJV4")))
                .andExpect(jsonPath("$.conceptEntries[0].lemma", is("Gustav Robert Kirchhoff")))
                .andExpect(jsonPath("$.conceptEntries[0].pos", is("noun")))
                .andExpect(jsonPath("$.conceptEntries[0].description", is("Gustav Robert Kirchhoff wrapper")))
                .andExpect(jsonPath("$.conceptEntries[0].conceptList", is("TestList")))
                .andExpect(jsonPath("$.conceptEntries[0].type.type_id", is("7c8745be-d06f-4feb-b749-910efa1b986d")))
                .andExpect(jsonPath("$.conceptEntries[0].type.type_uri",
                        is("http://www.digitalhps.org/types/TYPE_7c8745be-d06f-4feb-b749-910efa1b986d")))
                .andExpect(jsonPath("$.conceptEntries[0].type.type_name", is("Test-Type")))
                .andExpect(jsonPath("$.conceptEntries[0].deleted", is(false)))
                .andExpect(jsonPath("$.conceptEntries[0].concept_uri",
                        is("http://www.digitalhps.org/concepts/CONzpOmUhTDbJV4")))
                .andExpect(jsonPath("$.conceptEntries[0].creator_id", is("admin")))
                .andExpect(jsonPath("$.conceptEntries[0].equal_to", is("http://viaf.org/viaf/2587371")))
                .andExpect(jsonPath("$.conceptEntries[0].similar_to", is("http://viaf.org/viaf/2587372")))
                .andExpect(jsonPath("$.conceptEntries[0].wordnet_id", is("WID-11105945-N-03-Gustav_Robert_Kirchhoff")))
                .andExpect(jsonPath("$.pagination.pageNumber", is(1)))
                .andExpect(jsonPath("$.pagination.totalNumberOfRecords", is(1)))
                .andExpect(status().isOk());

    }

    @Test
    public void test_searchConcept_searchWithWordAndPosAndSimilarTo() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/ConceptSearch").param("word", "Gustav Robert Kirchhoff")
                        .param("pos", "noun").param("similar_to", "http://viaf.org/viaf/2587372")
                        .param("operator", SearchParamters.OP_AND).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.conceptEntries[0].id", is("CONzpOmUhTDbJV4")))
                .andExpect(jsonPath("$.conceptEntries[0].lemma", is("Gustav Robert Kirchhoff")))
                .andExpect(jsonPath("$.conceptEntries[0].pos", is("noun")))
                .andExpect(jsonPath("$.conceptEntries[0].description", is("Gustav Robert Kirchhoff wrapper")))
                .andExpect(jsonPath("$.conceptEntries[0].conceptList", is("TestList")))
                .andExpect(jsonPath("$.conceptEntries[0].type.type_id", is("7c8745be-d06f-4feb-b749-910efa1b986d")))
                .andExpect(jsonPath("$.conceptEntries[0].type.type_uri",
                        is("http://www.digitalhps.org/types/TYPE_7c8745be-d06f-4feb-b749-910efa1b986d")))
                .andExpect(jsonPath("$.conceptEntries[0].type.type_name", is("Test-Type")))
                .andExpect(jsonPath("$.conceptEntries[0].deleted", is(false)))
                .andExpect(jsonPath("$.conceptEntries[0].concept_uri",
                        is("http://www.digitalhps.org/concepts/CONzpOmUhTDbJV4")))
                .andExpect(jsonPath("$.conceptEntries[0].creator_id", is("admin")))
                .andExpect(jsonPath("$.conceptEntries[0].equal_to", is("http://viaf.org/viaf/2587371")))
                .andExpect(jsonPath("$.conceptEntries[0].similar_to", is("http://viaf.org/viaf/2587372")))
                .andExpect(jsonPath("$.conceptEntries[0].wordnet_id", is("WID-11105945-N-03-Gustav_Robert_Kirchhoff")))
                .andExpect(jsonPath("$.pagination.pageNumber", is(1)))
                .andExpect(jsonPath("$.pagination.totalNumberOfRecords", is(1)))
                .andExpect(status().isOk());
    }

    @Test
    public void test_searchConcept_searchWithWordPosAndPagination() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders.get("/ConceptSearch").param("word", "einstein").param("pos", "noun")
                .param("number_of_records_per_page", "2").param("page", "1").param("operator", SearchParamters.OP_AND)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.conceptEntries.length()", is(2)))
                .andExpect(jsonPath("$.conceptEntries[0].id", is("WID-10954498-N-01-Einstein")))
                .andExpect(jsonPath("$.conceptEntries[0].lemma", is("einstein")))
                .andExpect(jsonPath("$.conceptEntries[0].pos", is("noun")))
                .andExpect(jsonPath("$.conceptEntries[0].description",
                        is("physicist born in Germany who formulated the special theory of relativity and the general theory of relativity; Einstein also proposed that light consists of discrete quantized bundles of energy (later called photons) (1879-1955)")))
                .andExpect(jsonPath("$.conceptEntries[0].conceptList", is("WordNet")))
                .andExpect(jsonPath("$.conceptEntries[0].deleted", is(false)))
                .andExpect(jsonPath("$.conceptEntries[0].concept_uri",
                        is("http://www.digitalhps.org/concepts/WID-10954498-N-01-Einstein")))
                .andExpect(jsonPath("$.conceptEntries[0].synonym_ids", is("WID-10954498-N-02-Albert_Einstein,")))
                .andExpect(jsonPath("$.conceptEntries[0].wordnet_id", is("WID-10954498-N-01-Einstein")))
                .andExpect(jsonPath("$.conceptEntries[1].id", is("WID-10126926-N-05-Einstein")))
                .andExpect(jsonPath("$.conceptEntries[1].lemma", is("einstein")))
                .andExpect(jsonPath("$.conceptEntries[1].pos", is("noun")))
                .andExpect(jsonPath("$.conceptEntries[1].description",
                        is("someone who has exceptional intellectual ability and originality; \"Mozart was a child genius\"; \"he's smart but he's no Einstein\"")))
                .andExpect(jsonPath("$.conceptEntries[1].conceptList", is("WordNet")))
                .andExpect(jsonPath("$.conceptEntries[1].deleted", is(false)))
                .andExpect(jsonPath("$.conceptEntries[1].concept_uri",
                        is("http://www.digitalhps.org/concepts/WID-10126926-N-05-Einstein")))
                .andExpect(jsonPath("$.conceptEntries[1].synonym_ids",
                        is("WID-10126926-N-01-genius,WID-10126926-N-02-mastermind,WID-10126926-N-03-brain,WID-10126926-N-04-brainiac,")))
                .andExpect(jsonPath("$.conceptEntries[1].wordnet_id", is("WID-10126926-N-05-Einstein")))
                .andExpect(jsonPath("$.pagination.pageNumber", is(1)))
                .andExpect(jsonPath("$.pagination.totalNumberOfRecords", is(4)))
                .andExpect(status().isOk());

        this.mockMvc.perform(MockMvcRequestBuilders.get("/ConceptSearch").param("word", "einstein").param("pos", "noun")
                .param("number_of_records_per_page", "2").param("page", "2").param("operator", SearchParamters.OP_AND)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.conceptEntries.length()", is(2)))
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
                .andExpect(jsonPath("$.conceptEntries[1].id", is("WID-05875723-N-01-Bose-Einstein_statistics")))
                .andExpect(jsonPath("$.conceptEntries[1].lemma", is("bose-einstein statistics")))
                .andExpect(jsonPath("$.conceptEntries[1].pos", is("noun")))
                .andExpect(jsonPath("$.conceptEntries[1].description",
                        is("(physics) statistical law obeyed by a system of particles whose wave function is not changed when two particles are interchanged (the Pauli exclusion principle does not apply)")))
                .andExpect(jsonPath("$.conceptEntries[1].conceptList", is("WordNet")))
                .andExpect(jsonPath("$.conceptEntries[1].deleted", is(false)))
                .andExpect(jsonPath("$.conceptEntries[1].concept_uri",
                        is("http://www.digitalhps.org/concepts/WID-05875723-N-01-Bose-Einstein_statistics")))
                .andExpect(jsonPath("$.conceptEntries[1].wordnet_id", is("WID-05875723-N-01-Bose-Einstein_statistics")))
                .andExpect(jsonPath("$.pagination.pageNumber", is(2)))
                .andExpect(jsonPath("$.pagination.totalNumberOfRecords", is(4)))
                .andExpect(status().isOk());

    }

    @Test
    public void test_searchConcept_searchWithWordPosAndTypeId() throws Exception {

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/ConceptSearch").param("word", "Gustav Robert Kirchhoff")
                        .param("pos", "noun").param("type_id", "7c8745be-d06f-4feb-b749-910efa1b986d")
                        .param("operator", SearchParamters.OP_AND).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.conceptEntries[0].id", is("CONzpOmUhTDbJV4")))
                .andExpect(jsonPath("$.conceptEntries[0].lemma", is("Gustav Robert Kirchhoff")))
                .andExpect(jsonPath("$.conceptEntries[0].pos", is("noun")))
                .andExpect(jsonPath("$.conceptEntries[0].description", is("Gustav Robert Kirchhoff wrapper")))
                .andExpect(jsonPath("$.conceptEntries[0].conceptList", is("TestList")))
                .andExpect(jsonPath("$.conceptEntries[0].type.type_id", is("7c8745be-d06f-4feb-b749-910efa1b986d")))
                .andExpect(jsonPath("$.conceptEntries[0].type.type_uri",
                        is("http://www.digitalhps.org/types/TYPE_7c8745be-d06f-4feb-b749-910efa1b986d")))
                .andExpect(jsonPath("$.conceptEntries[0].type.type_name", is("Test-Type")))
                .andExpect(jsonPath("$.conceptEntries[0].deleted", is(false)))
                .andExpect(jsonPath("$.conceptEntries[0].concept_uri",
                        is("http://www.digitalhps.org/concepts/CONzpOmUhTDbJV4")))
                .andExpect(jsonPath("$.conceptEntries[0].creator_id", is("admin")))
                .andExpect(jsonPath("$.conceptEntries[0].equal_to", is("http://viaf.org/viaf/2587371")))
                .andExpect(jsonPath("$.conceptEntries[0].similar_to", is("http://viaf.org/viaf/2587372")))
                .andExpect(jsonPath("$.conceptEntries[0].wordnet_id", is("WID-11105945-N-03-Gustav_Robert_Kirchhoff")))
                .andExpect(jsonPath("$.pagination.pageNumber", is(1)))
                .andExpect(jsonPath("$.pagination.totalNumberOfRecords", is(1)))
                .andExpect(status().isOk());

    }

    @Test
    public void test_searchConcept_searchWithWordPosAndTypeUri() throws Exception {

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/ConceptSearch").param("word", "Gustav Robert Kirchhoff")
                        .param("pos", "noun")
                        .param("type_uri", "http://www.digitalhps.org/types/TYPE_7c8745be-d06f-4feb-b749-910efa1b986d")
                        .param("operator", SearchParamters.OP_AND).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.conceptEntries[0].id", is("CONzpOmUhTDbJV4")))
                .andExpect(jsonPath("$.conceptEntries[0].lemma", is("Gustav Robert Kirchhoff")))
                .andExpect(jsonPath("$.conceptEntries[0].pos", is("noun")))
                .andExpect(jsonPath("$.conceptEntries[0].description", is("Gustav Robert Kirchhoff wrapper")))
                .andExpect(jsonPath("$.conceptEntries[0].conceptList", is("TestList")))
                .andExpect(jsonPath("$.conceptEntries[0].type.type_id", is("7c8745be-d06f-4feb-b749-910efa1b986d")))
                .andExpect(jsonPath("$.conceptEntries[0].type.type_uri",
                        is("http://www.digitalhps.org/types/TYPE_7c8745be-d06f-4feb-b749-910efa1b986d")))
                .andExpect(jsonPath("$.conceptEntries[0].type.type_name", is("Test-Type")))
                .andExpect(jsonPath("$.conceptEntries[0].deleted", is(false)))
                .andExpect(jsonPath("$.conceptEntries[0].concept_uri",
                        is("http://www.digitalhps.org/concepts/CONzpOmUhTDbJV4")))
                .andExpect(jsonPath("$.conceptEntries[0].creator_id", is("admin")))
                .andExpect(jsonPath("$.conceptEntries[0].equal_to", is("http://viaf.org/viaf/2587371")))
                .andExpect(jsonPath("$.conceptEntries[0].similar_to", is("http://viaf.org/viaf/2587372")))
                .andExpect(jsonPath("$.conceptEntries[0].wordnet_id", is("WID-11105945-N-03-Gustav_Robert_Kirchhoff")))
                .andExpect(jsonPath("$.pagination.pageNumber", is(1)))
                .andExpect(jsonPath("$.pagination.totalNumberOfRecords", is(1)))
                .andExpect(status().isOk());

    }

    @Test
    public void test_searchConcept_searchWithWordPosAndDescription() throws Exception {

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/ConceptSearch").param("word", "Gustav Robert Kirchhoff")
                        .param("pos", "noun").param("description", "Gustav Robert Kirchhoff wrapper")
                        .param("operator", SearchParamters.OP_AND).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.conceptEntries[0].id", is("CONzpOmUhTDbJV4")))
                .andExpect(jsonPath("$.conceptEntries[0].lemma", is("Gustav Robert Kirchhoff")))
                .andExpect(jsonPath("$.conceptEntries[0].pos", is("noun")))
                .andExpect(jsonPath("$.conceptEntries[0].description", is("Gustav Robert Kirchhoff wrapper")))
                .andExpect(jsonPath("$.conceptEntries[0].conceptList", is("TestList")))
                .andExpect(jsonPath("$.conceptEntries[0].type.type_id", is("7c8745be-d06f-4feb-b749-910efa1b986d")))
                .andExpect(jsonPath("$.conceptEntries[0].type.type_uri",
                        is("http://www.digitalhps.org/types/TYPE_7c8745be-d06f-4feb-b749-910efa1b986d")))
                .andExpect(jsonPath("$.conceptEntries[0].type.type_name", is("Test-Type")))
                .andExpect(jsonPath("$.conceptEntries[0].deleted", is(false)))
                .andExpect(jsonPath("$.conceptEntries[0].concept_uri",
                        is("http://www.digitalhps.org/concepts/CONzpOmUhTDbJV4")))
                .andExpect(jsonPath("$.conceptEntries[0].creator_id", is("admin")))
                .andExpect(jsonPath("$.conceptEntries[0].equal_to", is("http://viaf.org/viaf/2587371")))
                .andExpect(jsonPath("$.conceptEntries[0].similar_to", is("http://viaf.org/viaf/2587372")))
                .andExpect(jsonPath("$.conceptEntries[0].wordnet_id", is("WID-11105945-N-03-Gustav_Robert_Kirchhoff")))
                .andExpect(jsonPath("$.pagination.pageNumber", is(1)))
                .andExpect(jsonPath("$.pagination.totalNumberOfRecords", is(1)))
                .andExpect(status().isOk());

    }

}
