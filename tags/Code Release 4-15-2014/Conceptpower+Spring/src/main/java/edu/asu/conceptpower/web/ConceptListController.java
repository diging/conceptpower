package edu.asu.conceptpower.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptList;
import edu.asu.conceptpower.core.ConceptManager;
import edu.asu.conceptpower.db4o.TypeDatabaseClient;
import edu.asu.conceptpower.util.URICreator;
import edu.asu.conceptpower.wrapper.ConceptEntryWrapper;
import edu.asu.conceptpower.wrapper.ConceptEntryWrapperCreator;

@Controller
public class ConceptListController {

	@Autowired
	private ConceptManager conceptManager;

	private List<ConceptList> conceptLists;

	@Autowired
	private TypeDatabaseClient typeDatabaseClient;

	@Autowired
	URICreator URICreator;

	@Autowired
	ConceptEntryWrapperCreator wrapperCreator;

	/**
	 * This method provides information of all the existing concept lists for
	 * showing concept list page
	 * 
	 * @param model
	 *            A generic model holder for Servlet
	 * @return String value to redirect user to all concpet list page
	 */
	@RequestMapping(value = "auth/conceptlist")
	public String prepareShowConceptList(ModelMap model) {

		conceptLists = conceptManager.getAllConceptLists();
		model.addAttribute("result", conceptLists);

		return "/auth/conceptlist";
	}

	/**
	 * This method provides infomaiton of concepts for a given concept list
	 * 
	 * @param list
	 * @param model
	 *            A generic model holder for Servlet
	 * @return Return sting value to redirect user to a particular concept list
	 *         page
	 */
	@RequestMapping(value = "auth/{listid}/concepts", method = RequestMethod.GET)
	public String getConceptsOfConceptList(@PathVariable("listid") String list,
			ModelMap model) {

		List<ConceptEntry> founds = conceptManager.getConceptListEntries(list);

		List<ConceptEntryWrapper> foundConcepts = wrapperCreator
				.createWrappers(founds != null ? founds
						.toArray(new ConceptEntry[founds.size()])
						: new ConceptEntry[0]);

		model.addAttribute("result", foundConcepts);
		return "/auth/conceptlist/concepts";
	}

	/**
	 * This method provides details of a concept for given concept ID
	 * 
	 * @param conceptid
	 *            ID of a concept
	 * @return Map containing concept details
	 */
	@RequestMapping(method = RequestMethod.GET, value = "conceptDetail")
	public @ResponseBody
	Map<String, String> getConceptDetails(
			@RequestParam("conceptid") String conceptid) {
		ConceptEntryWrapper conceptEntry = new ConceptEntryWrapper(
				conceptManager.getConceptEntry(conceptid));
		Map<String, String> details = new HashMap<String, String>();

		details.put("name", conceptEntry.getEntry().getWord());
		details.put("id", conceptEntry.getEntry().getId());
		details.put("uri", URICreator.getURI(conceptEntry.getEntry()));
		details.put("wordnetid", conceptEntry.getEntry().getWordnetId());
		details.put("pos", conceptEntry.getEntry().getPos());
		details.put("conceptlist", conceptEntry.getEntry().getConceptList());

		details.put(
				"type",
				conceptEntry.getEntry().getTypeId() == null ? ""
						: typeDatabaseClient.getType(
								conceptEntry.getEntry().getTypeId())
								.getTypeName());
		details.put("equalto",
				conceptEntry.getEntry().getEqualTo() == null ? ""
						: conceptEntry.getEntry().getEqualTo());
		details.put("similarto",
				conceptEntry.getEntry().getSimilarTo() == null ? ""
						: conceptEntry.getEntry().getSimilarTo());
		details.put("creator",
				conceptEntry.getEntry().getCreatorId() == null ? ""
						: conceptEntry.getEntry().getCreatorId());

		return details;
	}
}