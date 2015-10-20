package edu.asu.conceptpower.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.IConceptManager;
import edu.asu.conceptpower.wrapper.ConceptEntryWrapper;
import edu.asu.conceptpower.wrapper.impl.ConceptEntryWrapperCreator;

/**
 * This class provides all the required methods for deleting a concept
 * 
 * @author Chetan
 * 
 */
@Controller
public class ConceptDeleteController {

	@Autowired
	private IConceptManager conceptManager;

	@Autowired
	private ConceptEntryWrapperCreator wrapperCreator;

	/**
	 * This method provides details of a concept to be deleted for concept
	 * delete page
	 * 
	 * @param conceptid
	 *            ID of a concept to be deleted
	 * @param model
	 *            A generic model holder for Servlet
	 * @return String value to redirect user to concept delete page
	 */
	@RequestMapping(value = "auth/conceptlist/deleteconcept/{conceptid}", method = RequestMethod.GET)
	public String prepareDeleteConcept(
			@PathVariable("conceptid") String conceptid, ModelMap model) {
		ConceptEntry concept = conceptManager.getConceptEntry(conceptid);
		model.addAttribute("word", concept.getWord());
		model.addAttribute("description", concept.getDescription());
		model.addAttribute("id", concept.getId());
		model.addAttribute("wordnetId", concept.getWordnetId());
		model.addAttribute("pos", concept.getPos());
		model.addAttribute("conceptList", concept.getConceptList());
		model.addAttribute("type", concept.getTypeId());
		model.addAttribute("equal", concept.getEqualTo());
		model.addAttribute("similar", concept.getSimilarTo());
		model.addAttribute("user", concept.getModified());
		model.addAttribute("modified", concept.getModified());

		return "/auth/conceptlist/deleteconcept";
	}

	/**
	 * This method returns user to a particular concept list page after the user
	 * cancels concept delete operation
	 * 
	 * @param conceptList
	 *            concept list name where user has to redirected
	 * @param model
	 *            A generic model holder for Servlet
	 * @return String value to redirect user to a particular concept list
	 */
	@RequestMapping(value = "auth/concepts/canceldelete/{conceptList}", method = RequestMethod.GET)
	public String cancelDelete(@PathVariable("conceptList") String conceptList,
			ModelMap model) {
		List<ConceptEntry> founds = conceptManager
				.getConceptListEntries(conceptList);

		List<ConceptEntryWrapper> foundConcepts = wrapperCreator
				.createWrappers(founds != null ? founds
						.toArray(new ConceptEntry[founds.size()])
						: new ConceptEntry[0]);

		model.addAttribute("result", foundConcepts);
		return "/auth/conceptlist/concepts";
	}

	/**
	 * This method deletes a concept
	 * 
	 * @param id
	 *            Concept ID to be deleted
	 * @param model
	 *            A generic model holder for Servlet
	 * @return String value to redirect user to a particular concept list page
	 */
	@RequestMapping(value = "auth/conceptlist/deleteconceptconfirm/{id}", method = RequestMethod.GET)
	public String confirmlDelete(@PathVariable("id") String id, ModelMap model) {
		ConceptEntry concept = conceptManager.getConceptEntry(id);

		concept.setDeleted(true);
		// set modified and user details
		conceptManager.storeModifiedConcept(concept);

		List<ConceptEntry> founds = conceptManager
				.getConceptListEntries(concept.getConceptList());

		List<ConceptEntryWrapper> foundConcepts = wrapperCreator
				.createWrappers(founds != null ? founds
						.toArray(new ConceptEntry[founds.size()])
						: new ConceptEntry[0]);

		model.addAttribute("result", foundConcepts);
		return "/auth/conceptlist/concepts";
	}

}
