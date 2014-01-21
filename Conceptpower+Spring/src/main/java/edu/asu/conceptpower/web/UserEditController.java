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

	@RequestMapping(value = "auth/user/edit/{id}")
	public String prepareEditUser(ModelMap map, @PathVariable String id) {
		user = userManager.findUser(id);

		if (user == null)
			return "auth/user/notfound";

		map.addAttribute("fullname", user.getName());
		map.addAttribute("username", user.getUser());
		map.addAttribute("isadmin", user.getIsAdmin());
		map.addAttribute("password", user.getPw());
		map.addAttribute("repassword", user.getPw());
		return "auth/user/edit";
	}

	@RequestMapping(value = "auth/user/edit/store", method = RequestMethod.POST)
	public String storeChanges(HttpServletRequest req, ModelMap model) {

		if (user == null)
			return "auth/user/notfound";

		user.setIsAdmin(req.getParameter("isadmin") != null ? true : false);
		user.setName(req.getParameter("fullname"));
		user.setPw(req.getParameter("password"));

		userManager.storeModifiedUser(user);
		return "redirect:/auth/user/list";
	}
}
