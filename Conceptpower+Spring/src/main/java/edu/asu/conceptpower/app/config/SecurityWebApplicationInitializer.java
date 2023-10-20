package edu.asu.conceptpower.app.config;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
 * @author Digital Innovation Group
 *
 */
public class SecurityWebApplicationInitializer extends AbstractSecurityWebApplicationInitializer {

    @Override
    protected boolean enableHttpSessionEventPublisher() {
        return true;
    }

}
