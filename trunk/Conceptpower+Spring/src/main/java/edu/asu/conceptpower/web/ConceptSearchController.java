package edu.asu.conceptpower.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ConceptSearchController {

	@RequestMapping(value = "conceptpower/auth/searchitems", method = RequestMethod.GET)
	public String search() {

		System.out.println("coming");
		return null;
	}

}
