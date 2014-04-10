package edu.asu.conceptpower.web;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.conceptpower.profile.IServiceRegistry;

/**
 * This class handles requests for the application home page.
 */
@Controller
public class HomeController {

	@Autowired
	private IServiceRegistry serviceRegistry;

	/**
	 * This method simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {

		return "welcome";
	}

}
