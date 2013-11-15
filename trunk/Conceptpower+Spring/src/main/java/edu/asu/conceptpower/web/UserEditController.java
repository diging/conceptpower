package edu.asu.conceptpower.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.conceptpower.users.IUserManager;
import edu.asu.conceptpower.users.User;

@Controller
public class UserEditController {
	
	@Autowired
	private IUserManager userManager;

	@RequestMapping(value = "auth/user/edit/{id}")
	public String prepareEditUser(ModelMap map, @PathVariable String id) {
		User user = userManager.findUser(id);
		
		if (user == null)
			return "auth/user/notfound";
		
		map.addAttribute("user", user);
		return "auth/user/edit";
	}

	@RequestMapping(value = "auth/user/edit/store", method = RequestMethod.POST)
	public String storeChanges(@ModelAttribute User user, ModelMap map) {
		User storedUser = userManager.findUser(user.getUser());
		
		if (storedUser == null)
			return "auth/user/notfound";
		
		storedUser.setIsAdmin(user.getIsAdmin());
		storedUser.setName(user.getName());
		
		userManager.storeModifiedUser(storedUser);
		return "redirect:/auth/user/list";
	}
}
