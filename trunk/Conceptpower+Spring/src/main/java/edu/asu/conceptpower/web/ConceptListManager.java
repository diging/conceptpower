package edu.asu.conceptpower.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.asu.conceptpower.core.ConceptList;
import edu.asu.conceptpower.core.ConceptManager;

@Controller
public class ConceptListManager {

	@Autowired
	private ConceptManager conceptManager;
	private List<ConceptList> conceptLists;

	@RequestMapping(value = "auth/concepts/ConceptList")
	public String showConceptList(HttpServletRequest req, ModelMap model) {

		conceptLists = conceptManager.getAllConceptLists();
		model.addAttribute("result", conceptLists);

		return "/auth/concepts/ConceptList";
	}
}
