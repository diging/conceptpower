package edu.asu.conceptpower.web;

import org.junit.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import edu.asu.conceptpower.IntegrationTest;

public class HomeControllerTestIT extends IntegrationTest {

    @Test
    public void testForHomePage() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.view().name("welcome"));
    }
}
