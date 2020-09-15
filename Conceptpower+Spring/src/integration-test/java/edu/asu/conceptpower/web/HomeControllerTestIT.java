package edu.asu.conceptpower.web;

import org.junit.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import edu.asu.conceptpower.IntegrationTest;

public class HomeControllerTestIT extends IntegrationTest {
    
    public class CsrfParameters {
           public String token;
           public String parameterName;
    }
    
    @Test
    public void testForHomePage() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/").flashAttr("_csrf",new CsrfParameters()))
                .andExpect(MockMvcResultMatchers.view().name("home"));
    }
}
