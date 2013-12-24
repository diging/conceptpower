package edu.asu.conceptpower.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.asu.conceptpower.users.User;
import edu.asu.conceptpower.users.impl.UsersManager;

@Controller
public class AddUserView {

	@Autowired
	UsersManager usersManager;

	@RequestMapping(value = "auth/user/createuser")
	public String createUser(HttpServletRequest req, ModelMap model) {

		User user = new User(req.getParameter("username"),
				req.getParameter("password"));
		user.setName(req.getParameter("fullname"));
		usersManager.addUser(user);
		return "redirect:/auth/user/list";
	}

	@RequestMapping(value = "auth/user/add")
	public String addUser(HttpServletRequest req, ModelMap model) {
		return "auth/user/add";
	}

}
