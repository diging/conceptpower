package edu.asu.conceptpower.web;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.asu.conceptpower.app.migration.impl.MigrationManager;
import edu.asu.conceptpower.app.migration.impl.MigrationResult;

/**
 * 
 * @author Keerthivasan
 * 
 */

@Controller
public class MigrationController{
    
    @Autowired 
    @Qualifier("migrationService")
    private MigrationManager migrationManager;
    
    @RequestMapping(value = "/auth/migrate")
    public String landingPage(Model model) throws InterruptedException, ExecutionException {
        MigrationResult result = migrationManager.migrationStatus();
        
        if(result == null){
            model.addAttribute("isRunning", true);
        }else {
            model.addAttribute("isRunning", false);
        }
        
        return "/layouts/migration/migrate";
    }
    
    @RequestMapping(value = "/auth/migrate/startmigration")
    public String startMigration() {
        migrationManager.runMigrations();
        return "redirect:/auth/migrate";
    }
}