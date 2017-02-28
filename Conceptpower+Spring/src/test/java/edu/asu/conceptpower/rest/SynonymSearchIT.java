package edu.asu.conceptpower.rest;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import edu.asu.conceptpower.IntegrationTest;

public class SynonymSearchIT extends IntegrationTest {

    @Test
    public void getSynonymByIdTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/SynonymSearch").accept(MediaType.APPLICATION_JSON_VALUE)
                .with(new RequestPostProcessor() {
                    public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                        request.setParameter("id", "WID-02382039-N-02-Indian_pony");
                        return request;
                    }
                })).andDo(MockMvcResultHandlers.print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.conceptEntries[0].lemma", is("pony synonym")))
                .andExpect(jsonPath("$.conceptEntries[0].pos", is("verb")))
                .andExpect(jsonPath("$.conceptEntries[1].id", is("WID-02382039-N-01-cayuse")))
                .andExpect(jsonPath("$.conceptEntries[1].lemma", is("cayuse")))
                .andExpect(jsonPath("$.conceptEntries[1].pos", is("NOUN")))
                .andExpect(jsonPath("$.conceptEntries[1].synonym_ids", is("WID-02382039-N-02-Indian_pony,")))
                .andExpect(jsonPath("$.conceptEntries[0].id", is("CONfm3IOy7p4tDk")));
    }

}
