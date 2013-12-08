package edu.asu.conceptpower.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ConceptAddViewController {

	@RequestMapping(value = "auth/concepts/ConceptAddView")
	public String search(HttpServletRequest req, ModelMap model) {

		return "/auth/concepts/ConceptAddView";
	}

}
