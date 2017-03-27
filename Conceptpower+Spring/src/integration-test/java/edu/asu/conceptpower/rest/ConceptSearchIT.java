package edu.asu.conceptpower.rest;

import static org.hamcrest.core.Is.is;
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
    public void test_searchConcept_searchWithWordAndPosInJson() throws Exception {

        final String output = IOUtil
                .toString(this.getClass().getClassLoader().getResourceAsStream("output/conceptWithWordAndPos.json"));

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/ConceptSearch").param("word", "einstein").param("pos", "noun")
                        .param("operator", SearchParamters.OP_AND).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(output)).andExpect(status().isOk());

    }

    @Test
    public void test_searchConcept_successWithDefaultOperator() throws Exception {

        final String output = IOUtil
                .toString(this.getClass().getClassLoader().getResourceAsStream("output/conceptWithWordAndPos.json"));

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/ConceptSearch").param("word", "einstein").param("pos", "noun")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(output)).andExpect(status().isOk());

    }

    @Test
    public void test_searchConcept_noResultsInJson() throws Exception {

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/ConceptSearch").param("word", "Gustav Robert Kirchhoff")
                        .param("pos", "verb").param("concept_list", "VogonWeb Concepts")
                        .param("operator", SearchParamters.OP_AND).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", is("No records found for the search condition.")))
                .andExpect(status().isOk());

    }

    @Test
    public void test_searchConcept_searchWithWordAndPosAndEqualToInJson() throws Exception {
        final String output = IOUtil.toString(
                this.getClass().getClassLoader().getResourceAsStream("output/conceptWithWordPosAndEqualTo.json"));

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/ConceptSearch").param("word", "Abbott Henderson Thayer")
                        .param("pos", "noun").param("equal_to", "http://viaf.org/viaf/55043769")
                        .param("operator", SearchParamters.OP_AND).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(output)).andExpect(status().isOk());

    }

    @Test
    public void test_searchConcept_searchWithWordAndPosAndSimilarToInJson() throws Exception {

        final String output = IOUtil.toString(
                this.getClass().getClassLoader().getResourceAsStream("output/conceptWithWordPosAndSimilarTo.json"));

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/ConceptSearch").param("word", "Douglas Weiner")
                        .param("pos", "noun").param("similar_to", "http://viaf.org/viaf/248802520")
                        .param("operator", SearchParamters.OP_AND).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(output)).andExpect(status().isOk());
    }

    @Test
    public void test_searchConcept_searchWithWordPosAndPaginationInJson() throws Exception {
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
    public void test_searchConcept_searchWithWordPosAndTypeIdInJson() throws Exception {

        final String output = IOUtil.toString(
                this.getClass().getClassLoader().getResourceAsStream("output/conceptWithWordPosAndTypeId.json"));

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/ConceptSearch").param("word", "Almira Hart Lincoln Phelps")
                        .param("pos", "noun").param("type_id", "986a7cc9-c0c1-4720-b344-853f08c136ab")
                        .param("operator", SearchParamters.OP_AND).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(output)).andExpect(status().isOk());

    }

    @Test
    public void test_searchConcept_searchWithWordPosAndTypeUriInJson() throws Exception {

        final String output = IOUtil.toString(
                this.getClass().getClassLoader().getResourceAsStream("output/conceptWithWordPosAndTypeUri.json"));

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/ConceptSearch").param("word", "Almira Hart Lincoln Phelps")
                        .param("pos", "noun")
                        .param("type_uri", "http://www.digitalhps.org/types/TYPE_986a7cc9-c0c1-4720-b344-853f08c136ab")
                        .param("operator", SearchParamters.OP_AND).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(output)).andExpect(status().isOk());

    }

    @Test
    public void test_searchConcept_searchWithWordPosAndDescriptionInJson() throws Exception {

        final String output = IOUtil.toString(
                this.getClass().getClassLoader().getResourceAsStream("output/conceptWithWordPosAndDescription.json"));

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/ConceptSearch").param("word", "Douglas Weiner")
                        .param("pos", "noun").param("description", "American 20th century environmentalist")
                        .param("operator", SearchParamters.OP_AND).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(output)).andExpect(status().isOk());

    }

    @Test
    public void test_searchConcept_searchWithWordAndPosInXml() throws Exception {

        final String output = IOUtil
                .toString(this.getClass().getClassLoader().getResourceAsStream("output/conceptWithWordAndPos.xml"));

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/ConceptSearch").param("word", "einstein").param("pos", "noun")
                        .param("operator", SearchParamters.OP_AND).accept(MediaType.APPLICATION_XML_VALUE))
                .andExpect(content().string(output)).andExpect(status().isOk());
    }

    @Test
    public void test_searchConcept_noResultsInXml() throws Exception {

        final String output = IOUtil
                .toString(this.getClass().getClassLoader().getResourceAsStream("output/noResults.xml"));

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/ConceptSearch").param("word", "Gustav Robert Kirchhoff")
                        .param("pos", "verb").param("concept_list", "VogonWeb Concepts")
                        .param("operator", SearchParamters.OP_AND).accept(MediaType.APPLICATION_XML_VALUE))
                .andExpect(content().string(output)).andExpect(status().isOk());

    }

    @Test
    public void test_searchConcept_searchWithWordAndPosAndEqualToInXml() throws Exception {
        final String output = IOUtil.toString(
                this.getClass().getClassLoader().getResourceAsStream("output/conceptWithWordPosAndEqualTo.xml"));

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/ConceptSearch").param("word", "Abbott Henderson Thayer")
                        .param("pos", "noun").param("equal_to", "http://viaf.org/viaf/55043769")
                        .param("operator", SearchParamters.OP_AND).accept(MediaType.APPLICATION_XML_VALUE))
                .andExpect(content().string(output)).andExpect(status().isOk());

    }

    @Test
    public void test_searchConcept_searchWithWordAndPosAndSimilarToInXml() throws Exception {

        final String output = IOUtil.toString(
                this.getClass().getClassLoader().getResourceAsStream("output/conceptWithWordPosAndSimilarTo.xml"));

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/ConceptSearch").param("word", "Douglas Weiner")
                        .param("pos", "noun").param("similar_to", "http://viaf.org/viaf/248802520")
                        .param("operator", SearchParamters.OP_AND).accept(MediaType.APPLICATION_XML_VALUE))
                .andExpect(content().string(output)).andExpect(status().isOk());
    }

    @Test
    public void test_searchConcept_searchWithWordPosAndPaginationInXml() throws Exception {
        final String output = IOUtil.toString(
                this.getClass().getClassLoader().getResourceAsStream("output/conceptWithWordPosAndPagination1.xml"));

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/ConceptSearch").param("word", "einstein").param("pos", "noun")
                        .param("number_of_records_per_page", "2").param("page", "1")
                        .param("operator", SearchParamters.OP_AND).accept(MediaType.APPLICATION_XML_VALUE))
                .andExpect(content().string(output)).andExpect(status().isOk());

        final String output2 = IOUtil.toString(
                this.getClass().getClassLoader().getResourceAsStream("output/conceptWithWordPosAndPagination2.xml"));

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/ConceptSearch").param("word", "einstein").param("pos", "noun")
                        .param("number_of_records_per_page", "2").param("page", "2")
                        .param("operator", SearchParamters.OP_AND).accept(MediaType.APPLICATION_XML_VALUE))
                .andExpect(content().string(output2)).andExpect(status().isOk());

    }

    @Test
    public void test_searchConcept_searchWithWordPosAndTypeIdInXml() throws Exception {

        final String output = IOUtil.toString(
                this.getClass().getClassLoader().getResourceAsStream("output/conceptWithWordPosAndTypeId.xml"));

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/ConceptSearch").param("word", "Almira Hart Lincoln Phelps")
                        .param("pos", "noun").param("type_id", "986a7cc9-c0c1-4720-b344-853f08c136ab")
                        .param("operator", SearchParamters.OP_AND).accept(MediaType.APPLICATION_XML_VALUE))
                .andExpect(content().string(output)).andExpect(status().isOk());

    }

    @Test
    public void test_searchConcept_searchWithWordPosAndTypeUriInXml() throws Exception {

        final String output = IOUtil.toString(
                this.getClass().getClassLoader().getResourceAsStream("output/conceptWithWordPosAndTypeUri.xml"));

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/ConceptSearch").param("word", "Almira Hart Lincoln Phelps")
                        .param("pos", "noun")
                        .param("type_uri", "http://www.digitalhps.org/types/TYPE_986a7cc9-c0c1-4720-b344-853f08c136ab")
                        .param("operator", SearchParamters.OP_AND).accept(MediaType.APPLICATION_XML_VALUE))
                .andExpect(content().string(output)).andExpect(status().isOk());

    }

    @Test
    public void test_searchConcept_searchWithWordPosAndDescriptionInXml() throws Exception {

        final String output = IOUtil.toString(
                this.getClass().getClassLoader().getResourceAsStream("output/conceptWithWordPosAndDescription.xml"));

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/ConceptSearch").param("word", "Douglas Weiner")
                        .param("pos", "noun").param("description", "American 20th century environmentalist")
                        .param("operator", SearchParamters.OP_AND).accept(MediaType.APPLICATION_XML_VALUE))
                .andExpect(content().string(output)).andExpect(status().isOk());

    }

}
