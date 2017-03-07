package edu.asu.conceptpower.rest;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.apache.commons.io.IOUtil;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import edu.asu.conceptpower.IntegrationTest;

public class ConceptSearchIT extends IntegrationTest {

    @Test
    public void test_searchConcept_searchWithWordAndPos() throws Exception {

        final String output = IOUtil
                .toString(this.getClass().getClassLoader().getResourceAsStream("output/conceptWithWordAndPos.json"));

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/ConceptSearch").param("word", "einstein").param("pos", "noun")
                        .param("operator", SearchParamters.OP_AND).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(output)).andExpect(status().isOk());

    }

    @Test
    public void test_searchConcept_searchWithWordAndPosAndConceptList() throws Exception {
        final String output = IOUtil.toString(
                this.getClass().getClassLoader().getResourceAsStream("output/conceptWithWordPosAndConceptList.json"));

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/ConceptSearch").param("word", "Gustav Robert Kirchhoff")
                        .param("pos", "noun").param("concept_list", "TestList")
                        .param("operator", SearchParamters.OP_AND).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(output)).andExpect(status().isOk());
    }

    @Test
    public void test_searchConcept_noResults() throws Exception {

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/ConceptSearch").param("word", "Gustav Robert Kirchhoff")
                        .param("pos", "verb").param("concept_list", "TestList")
                        .param("operator", SearchParamters.OP_AND).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", is("No records found for the search condition.")))
                .andExpect(status().isOk());

    }

    @Test
    public void test_searchConcept_searchWithWordAndPosAndEqualTo() throws Exception {
        final String output = IOUtil.toString(
                this.getClass().getClassLoader().getResourceAsStream("output/conceptWithWordPosAndEqualTo.json"));

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/ConceptSearch").param("word", "Gustav Robert Kirchhoff")
                        .param("pos", "noun").param("equal_to", "http://viaf.org/viaf/2587371")
                        .param("operator", SearchParamters.OP_AND).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(output)).andExpect(status().isOk());

    }

    @Test
    public void test_searchConcept_searchWithWordAndPosAndSimilarTo() throws Exception {

        final String output = IOUtil.toString(
                this.getClass().getClassLoader().getResourceAsStream("output/conceptWithWordPosAndSimilarTo.json"));

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/ConceptSearch").param("word", "Gustav Robert Kirchhoff")
                        .param("pos", "noun").param("similar_to", "http://viaf.org/viaf/2587372")
                        .param("operator", SearchParamters.OP_AND).accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(content().string(output)).andExpect(status().isOk());
    }

    @Test
    public void test_searchConcept_searchWithWordPosAndPagination() throws Exception {
        final String output = IOUtil.toString(
                this.getClass().getClassLoader().getResourceAsStream("output/conceptWithWordPosAndPagination1.json"));

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/ConceptSearch").param("word", "einstein").param("pos", "noun")
                        .param("number_of_records_per_page", "2").param("page", "1")
                        .param("operator", SearchParamters.OP_AND).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(output)).andExpect(status().isOk());

        final String output2 = IOUtil.toString(
                this.getClass().getClassLoader().getResourceAsStream("output/conceptWithWordPosAndPagination2.json"));

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/ConceptSearch").param("word", "einstein").param("pos", "noun")
                        .param("number_of_records_per_page", "2").param("page", "2")
                        .param("operator", SearchParamters.OP_AND).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(output2)).andExpect(status().isOk());

    }

    @Test
    public void test_searchConcept_searchWithWordPosAndTypeId() throws Exception {

        final String output = IOUtil.toString(
                this.getClass().getClassLoader().getResourceAsStream("output/conceptWithWordPosAndTypeId.json"));

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/ConceptSearch").param("word", "Gustav Robert Kirchhoff")
                        .param("pos", "noun").param("type_id", "7c8745be-d06f-4feb-b749-910efa1b986d")
                        .param("operator", SearchParamters.OP_AND).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(output)).andExpect(status().isOk());

    }

    @Test
    public void test_searchConcept_searchWithWordPosAndTypeUri() throws Exception {

        final String output = IOUtil.toString(
                this.getClass().getClassLoader().getResourceAsStream("output/conceptWithWordPosAndTypeUri.json"));

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/ConceptSearch").param("word", "Gustav Robert Kirchhoff")
                        .param("pos", "noun")
                        .param("type_uri", "http://www.digitalhps.org/types/TYPE_7c8745be-d06f-4feb-b749-910efa1b986d")
                        .param("operator", SearchParamters.OP_AND).accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(content().string(output)).andExpect(status().isOk());

    }

    @Test
    public void test_searchConcept_searchWithWordPosAndDescription() throws Exception {

        final String output = IOUtil.toString(
                this.getClass().getClassLoader().getResourceAsStream("output/conceptWithWordPosAndDescription.json"));

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/ConceptSearch").param("word", "Gustav Robert Kirchhoff")
                        .param("pos", "noun").param("description", "Gustav Robert Kirchhoff wrapper")
                        .param("operator", SearchParamters.OP_AND).accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(content().string(output)).andExpect(status().isOk());

    }

}
