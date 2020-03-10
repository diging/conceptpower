package edu.asu.conceptpower.web.profile;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.asu.conceptpower.app.users.IUserManager;
import edu.asu.conceptpower.users.User;

@Controller
public class ProfileController {
    
    @Autowired
    private IUserManager userManager;
    
    @RequestMapping(value = "/auth/profile")
    public String showProfile(Model model, Principal principal) {
        User user = userManager.findUser(principal.getName());
        if (user == null) {
            return "auth/user/notfound";
        }
        
        model.addAttribute("currentUser", user);
        return "/layouts/profile/profile";
    }
}
