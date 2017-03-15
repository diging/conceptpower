package edu.asu.conceptpower;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
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

import edu.asu.conceptpower.servlet.users.ConceptpowerGrantedAuthority;

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
        setSecurityContext();
        if (!isSetupDone) {
            this.mockMvc.perform(MockMvcRequestBuilders.post("/auth/indexConcepts").principal(principal));
            MvcResult mr = this.mockMvc.perform(MockMvcRequestBuilders.post("/auth/getIndexerStatus")).andReturn();
            isSetupDone = true;
        }
    }

    private void setSecurityContext() {
        Collection<ConceptpowerGrantedAuthority> authorities = new ArrayList<>();
        ConceptpowerGrantedAuthority grantedAuthority = new ConceptpowerGrantedAuthority("admin");
        grantedAuthority.setAuthority(ConceptpowerGrantedAuthority.ROLE_ADMIN);
        authorities.add(grantedAuthority);

        AbstractAuthenticationToken auth = new AnonymousAuthenticationToken("admin", principal, authorities);
        SecurityContext context = new SecurityContextImpl();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);
    }

}
