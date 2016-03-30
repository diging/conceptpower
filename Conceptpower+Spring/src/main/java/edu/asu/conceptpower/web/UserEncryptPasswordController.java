package edu.asu.conceptpower.web;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.asu.conceptpower.users.IUserManager;
import edu.asu.conceptpower.users.User;

@Controller
public class UserEncryptPasswordController {

	@Autowired
	private IUserManager userManager;
	
	@RequestMapping(value = "auth/user/encrypt/{username}")
	public String showEncryptPage(@PathVariable String username, Principal principal, Model model) {
		User user = userManager.findUser(username);
		
		model.addAttribute("userfullname", user.getFullname());
		model.addAttribute("username", username);
		model.addAttribute("isEncrypted", user.getIsEncrypted());
		
		return "auth/user/encrypt";
	}
	
	@RequestMapping(value = "auth/user/encryptpassword", method = RequestMethod.POST)
	public String encryptPassword(@RequestParam String username) {
		if (username.trim().isEmpty()) {
			// TODO: needs to be handled correctly
			// but should never be called like this 
			return "redirect:auth/user/list";
		}
		User user = userManager.findUser(username);
		if (user.getIsEncrypted()) {
			// TODO: needs to be handled correctly
			// but should never be called like this 
			return "redirect:auth/user/list";
		}
		userManager.updatePasswordEncryption(username);
		return "redirect:/auth/user/list";
	}
}
