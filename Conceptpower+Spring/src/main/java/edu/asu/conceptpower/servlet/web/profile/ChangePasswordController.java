package edu.asu.conceptpower.servlet.web.profile;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.conceptpower.servlet.users.IUserManager;
import edu.asu.conceptpower.servlet.web.profile.backing.PasswordBean;
import edu.asu.conceptpower.users.User;

@Controller
public class ChangePasswordController {
    
    @Autowired
    private IUserManager userManager;

    @RequestMapping(value = "/auth/profile/password/change", method = RequestMethod.GET)
    public String showChangePassword(Model model, Principal principal) {
        model.addAttribute("passwordBean", new PasswordBean());
        
        return "auth/profile/password";
    }
    
    @RequestMapping(value = "/auth/profile/password/change", method = RequestMethod.POST)
    public String changePassword(@Valid @ModelAttribute PasswordBean passwordBean, BindingResult results, Model model, Principal principal) {
        if (results.hasErrors()) {
            passwordBean.setOldPassword("");
            return "auth/profile/password";
        }
        
        String oldPasswordEncrypt = BCrypt.hashpw(passwordBean.getOldPassword(), BCrypt.gensalt());
        User user = userManager.getUser(principal.getName(), oldPasswordEncrypt);
        if (user != null) {
            user.setPw(passwordBean.getPassword());
            userManager.storeModifiedPassword(user);
            return "redirect:/auth/profile";
        }
        
        model.addAttribute("show_error_alert", true);
        model.addAttribute("error_alert_msg", "Your password could not be changed. The old password you've entered was incorrect.");
        model.addAttribute("passwordBean", new PasswordBean());
        return "auth/profile/password";
    }
}
