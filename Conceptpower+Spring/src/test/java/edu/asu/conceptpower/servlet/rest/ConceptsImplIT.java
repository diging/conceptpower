package edu.asu.conceptpower.servlet.rest;

import javax.servlet.ServletContext;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource(locations = "classpath:/test.properties")
@ContextHierarchy({ @ContextConfiguration(locations = { "classpath:/root-context-test.xml" }),
        @ContextConfiguration(locations = { "classpath:/servlet-context-test.xml" }),
        @ContextConfiguration(locations = { "classpath:/rest-context-test.xml" }) })
@WebAppConfiguration
public class ConceptsImplIT {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

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
                        .accept(MediaType.APPLICATION_XML_VALUE))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk());
        // TODO - Response values can be tested for desired outcomes.

    }

    @Test
    public void testForHomePage() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/")).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.view().name("welcome"));
    }

}
