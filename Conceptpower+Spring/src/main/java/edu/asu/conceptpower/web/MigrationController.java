package edu.asu.conceptpower.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 
 * @author Keerthivasan
 * 
 */

@Controller
public class MigrationController{
    
    @RequestMapping(value = "/auth/migrate")
    public String landingPage() {
        return "/layouts/migration/landingpage";
    }
}