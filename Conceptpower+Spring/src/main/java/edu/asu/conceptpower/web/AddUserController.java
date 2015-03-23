package edu.asu.conceptpower.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.asu.conceptpower.users.IUserManager;
import edu.asu.conceptpower.users.User;

/**
 * This class provides methods for creating a user
 * 
 * @author Chetan and Julia Damerow
 * 
 */
@Controller
public class AddUserController {

	@Autowired
	IUserManager usersManager;

	/**
	 * This method creates a new user for given user information
	 * 
	 * @param req
	 *            Holds HTTP request information
	 * @return String to redirect user to user list page
	 */
	@RequestMapping(value = "auth/user/createuser")
	public String createUser(HttpServletRequest req) {

		User user = new User(req.getParameter("username"),
				req.getParameter("password"));
		user.setName(req.getParameter("fullname"));
		user.setEmail(req.getParameter("email"));
		usersManager.addUser(user);
		return "redirect:/auth/user/list";
	}

	/**
	 * This method provides string to redirect user to add user page
	 * 
	 * @return String to redirect user to add user page
	 */
	@RequestMapping(value = "auth/user/add")
	public String addUser() {
		return "auth/user/add";
	}

}
