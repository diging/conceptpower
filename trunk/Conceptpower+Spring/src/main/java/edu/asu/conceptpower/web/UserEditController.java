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

@Controller
public class UserEditController {

	@Autowired
	private IUserManager userManager;

	User user;

	@RequestMapping(value = "auth/user/edituser/{id:.+}")
	public String prepareEditUser(ModelMap map, @PathVariable String id) {
		user = userManager.findUser(id);

		if (user == null)
			return "auth/user/notfound";

		map.addAttribute("fullname", user.getName());
		map.addAttribute("username", user.getUser());
		map.addAttribute("isadmin", user.getIsAdmin());
		return "auth/user/edituser";
	}

	@RequestMapping(value = "auth/user/edituser/store", method = RequestMethod.POST)
	public String storeUserChanges(HttpServletRequest req, ModelMap model) {

		if (user == null)
			return "auth/user/notfound";

		user.setIsAdmin(req.getParameter("isadmin") != null ? true : false);
		user.setName(req.getParameter("fullname"));

		userManager.storeModifiedUser(user);
		return "redirect:/auth/user/list";
	}

	@RequestMapping(value = "auth/user/editpassword/{id:.+}")
	public String prepareEditPassword(ModelMap map, @PathVariable String id) {
		user = userManager.findUser(id);

		if (user == null)
			return "auth/user/notfound";

		map.addAttribute("username", user.getUser());
		return "auth/user/editpassword";
	}

	@RequestMapping(value = "auth/user/editpassword/store", method = RequestMethod.POST)
	public String storePasswordChanges(HttpServletRequest req, ModelMap model) {

		if (user == null)
			return "auth/user/notfound";

		user.setPw(req.getParameter("password"));

		userManager.storeModifiedPassword(user);
		return "redirect:/auth/user/list";
	}

	@RequestMapping(value = "auth/user/canceledit", method = RequestMethod.GET)
	public String cancelEdit(HttpServletRequest req, ModelMap model) {
		return "redirect:/auth/user/list";
	}
}
