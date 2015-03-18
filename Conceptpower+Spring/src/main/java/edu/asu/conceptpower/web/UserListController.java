package edu.asu.conceptpower.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.asu.conceptpower.users.IUserManager;
import edu.asu.conceptpower.users.User;

/**
 * This class provides methods required for user list operations.
 * 
 * @author Chetan Ambi
 * 
 */
@Controller
public class UserListController {

	@Autowired
	private IUserManager userManager;

	/**
	 * This method provides existing user list information for user list page
	 * 
	 * @param model
	 *            A generic model holder for Servlet
	 * @return String value which returns user to user list page
	 */
	@RequestMapping(value = "auth/user/list")
	public String listUsers(ModelMap model) {
		User[] users = userManager.getAllUsers();
		model.addAttribute("users", users);

		return "auth/user/list";
	}

}
