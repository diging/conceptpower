package edu.asu.conceptpower.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.conceptpower.users.IUserManager;
import edu.asu.conceptpower.users.User;

/**
 * This class provides all the required method for editing a user
 * 
 * @author Chetan
 * 
 */
@Controller
public class UserEditController {

	@Autowired
	private IUserManager userManager;

	User user;

	/**
	 * This method provides user information to user editing page
	 * 
	 * @param model
	 *            Generic model holder for servlet
	 * @param id
	 *            Represents id of a use to be edited
	 * @return Returns a string value to redirect user to edit user page
	 */
	@RequestMapping(value = "auth/user/edituser/{id:.+}")
	public String prepareEditUser(ModelMap model, @PathVariable String id) {
		user = userManager.findUser(id);

		if (user == null)
			return "auth/user/notfound";

		model.addAttribute("fullname", user.getName());
		model.addAttribute("username", user.getUser());
		model.addAttribute("isadmin", user.getIsAdmin());
		return "auth/user/edituser";
	}

	/**
	 * This method stores updated user changes
	 * 
	 * @param req
	 *            holds HTTP request information
	 * @return Returns a string value to redirect user to user list page
	 */
	@RequestMapping(value = "auth/user/edituser/store", method = RequestMethod.POST)
	public String storeUserChanges(HttpServletRequest req) {

		if (user == null)
			return "auth/user/notfound";

		user.setIsAdmin(req.getParameter("isadmin") != null ? true : false);
		user.setName(req.getParameter("fullname"));

		userManager.storeModifiedUser(user);
		return "redirect:/auth/user/list";
	}

	/**
	 * This method provides user information to password edit page
	 * 
	 * @param model
	 *            Generic model holder for servlet
	 * @param id
	 *            Represents user id
	 * @return Returns a string value to redirect user to edit password page
	 */
	@RequestMapping(value = "auth/user/editpassword/{id:.+}")
	public String prepareEditPassword(ModelMap model, @PathVariable String id) {
		user = userManager.findUser(id);

		if (user == null)
			return "auth/user/notfound";

		model.addAttribute("username", user.getUser());
		return "auth/user/editpassword";
	}

	/**
	 * This method stores the updated user password information
	 * 
	 * @param req
	 *            holds HTTP request information
	 * @return Returns a string value to redirect user to user list page
	 */
	@RequestMapping(value = "auth/user/editpassword/store", method = RequestMethod.POST)
	public String storePasswordChanges(HttpServletRequest req) {

		if (user == null)
			return "auth/user/notfound";

		user.setPw(req.getParameter("password"));

		userManager.storeModifiedPassword(user);
		return "redirect:/auth/user/list";
	}

	/**
	 * This method returns the control to user list when user cancels user edit
	 * or password edit operation
	 * 
	 * @return Returns a string value to redirect user to user list page
	 */
	@RequestMapping(value = "auth/user/canceledit", method = RequestMethod.GET)
	public String cancelEdit() {
		return "redirect:/auth/user/list";
	}
}
