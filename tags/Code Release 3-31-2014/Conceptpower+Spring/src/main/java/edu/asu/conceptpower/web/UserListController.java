package edu.asu.conceptpower.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.asu.conceptpower.users.IUserManager;
import edu.asu.conceptpower.users.User;

@Controller
public class UserListController {

	@Autowired
	private IUserManager userManager;

	@RequestMapping(value = "auth/user/list")
	public String listUsers(ModelMap model) {
		User[] users = userManager.getAllUsers();
		model.addAttribute("users", users);

		return "auth/user/list";
	}

}
