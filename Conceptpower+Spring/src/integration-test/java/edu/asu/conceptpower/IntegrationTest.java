package edu.asu.conceptpower;

import java.security.Principal;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource(locations = "classpath:/test.properties")
@ContextHierarchy({ @ContextConfiguration(locations = { "classpath:/root-context-test.xml" }),
        @ContextConfiguration(locations = { "classpath:/servlet-context-test.xml",
                "classpath:/rest-context-test.xml" }) })
@WebAppConfiguration
public abstract class IntegrationTest {

    private static boolean isSetupDone = false;

    @Autowired
    protected WebApplicationContext wac;

    protected MockMvc mockMvc;

    protected Principal principal = new Principal() {
        public String getName() {
            return "admin";
        }
    };

    @Before
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        if (!isSetupDone) {
            this.mockMvc.perform(MockMvcRequestBuilders.post("/auth/indexConcepts").principal(principal));
            MvcResult mr = null;
            do {
                mr = this.mockMvc.perform(MockMvcRequestBuilders.post("/auth/getIndexerStatus")).andReturn();
            } while (mr.getResponse().getStatus() != HttpStatus.OK.value());
            isSetupDone = true;
        }
    }
}
