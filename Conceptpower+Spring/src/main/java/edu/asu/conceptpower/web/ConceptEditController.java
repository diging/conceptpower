package edu.asu.conceptpower.web;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jettison.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.asu.conceptpower.bean.ConceptEditBean;
import edu.asu.conceptpower.core.ChangeEvent;
import edu.asu.conceptpower.core.ChangeEventConstants;
import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptList;
import edu.asu.conceptpower.core.ConceptType;
import edu.asu.conceptpower.core.Constants;
import edu.asu.conceptpower.core.IConceptListManager;
import edu.asu.conceptpower.core.IConceptManager;
import edu.asu.conceptpower.core.IConceptTypeManger;
import edu.asu.conceptpower.users.IUserManager;

/**
 * This method provides all the required methods for editing a concept
 * 
 * @author Chetan
 * 
 */
@Controller
public class ConceptEditController {

	@Autowired
	private IConceptManager conceptManager;

	@Autowired
	private IConceptListManager conceptListManager;

	@Autowired
	private IUserManager usersManager;

	@Autowired
	private IConceptTypeManger conceptTypesManager;

	/**
	 * This method provides information of a concept to be edited for concept
	 * edit page
	 * 
	 * @param conceptid
	 *            ID of a concept to be edited
	 * @param model
	 *            A generic model holder for Servlet
	 * @return String value to redirect user to concept edit page
	 */
	@RequestMapping(value = "auth/conceptlist/editconcept/{conceptid}", method = RequestMethod.GET)
	public String prepareEditConcept(@PathVariable("conceptid") String conceptid,@RequestParam(value = "fromHomeScreen",required=false)String fromHomeScreen,
			@ModelAttribute("conceptEditBean") ConceptEditBean conceptEditBean, ModelMap model) {
	    
	    if(fromHomeScreen!=null){
	        conceptEditBean.setFromHomeScreen(true);
	    }
	    
		ConceptEntry concept = conceptManager.getConceptEntry(conceptid);
		ConceptType[] allTypes = conceptTypesManager.getAllTypes();
		List<ConceptList> allLists = conceptListManager.getAllConceptLists();
		conceptEditBean.setWord(concept.getWord());
		conceptEditBean.setSelectedPosValue(concept.getPos().toLowerCase());
		conceptEditBean.setConceptListValue(concept.getConceptList());
		conceptEditBean.setSelectedListName(concept.getConceptList());
		conceptEditBean.setConceptList(allLists);
		conceptEditBean.setDescription(concept.getDescription().trim());
		conceptEditBean.setSynonymsids(concept.getSynonymIds());
		conceptEditBean.setSelectedTypeId(concept.getTypeId());
		conceptEditBean.setTypes(allTypes);
		conceptEditBean.setSelectedTypeId(concept.getTypeId());
		conceptEditBean.setEquals(concept.getEqualTo());
		conceptEditBean.setSimilar(concept.getSimilarTo());
		conceptEditBean.setConceptId(concept.getId());
		conceptEditBean.setConceptEntryList(new ArrayList());
		model.addAttribute("conceptId", concept.getId());
		return "/auth/conceptlist/editconcept";
	}

	/**
	 * This method redirects user to a particular concept list page when the
	 * user cancels concept edit operation
	 * 
	 * @param conceptList
	 *            Concept list where user has to be redirected
	 * @return String value which redirect user to a particular concept list
	 *         page
	 */
    @RequestMapping(value = "auth/concepts/canceledit", method = RequestMethod.GET)
    public String cancelEdit(@RequestParam(value = "fromHomeScreen",required=false)String fromHomeScreen) {
        if (fromHomeScreen.equalsIgnoreCase("true")) {
            return "redirect:/login";
        }
        return "redirect:/auth/conceptlist";
    }

	/**
	 * This method stores the updated information of a concept
	 * 
	 * @param id
	 *            ID of a concept to be edited
	 * @param req
	 *            Holds HTTP request information
	 * @param principal
	 *            Holds logged in user information
	 * @return String value which redirects user to a particular concept list
	 *         page
	 */
	@RequestMapping(value = "auth/conceptlist/editconcept/edit/{id}", method = RequestMethod.POST)
	public String confirmEdit(@PathVariable("id") String id, HttpServletRequest req, Principal principal,
			@ModelAttribute("conceptEditBean") ConceptEditBean conceptEditBean) {

		ConceptEntry conceptEntry = conceptManager.getConceptEntry(id);
		conceptEntry.setWord(conceptEditBean.getWord());
		conceptEntry.setConceptList(conceptEditBean.getConceptListValue());
		conceptEntry.setPos(conceptEditBean.getSelectedPosValue());
		conceptEntry.setDescription(conceptEditBean.getDescription());
		conceptEntry.setEqualTo(conceptEditBean.getEquals());
		conceptEntry.setSimilarTo(conceptEditBean.getSimilar());
		conceptEntry.setTypeId(conceptEditBean.getSelectedTypeId());
		conceptEntry.setSynonymIds(conceptEditBean.getSynonymsids());

		String userId = usersManager.findUser(principal.getName()).getUsername();
		String modified = conceptEntry.getModified() != null ? conceptEntry.getModified() : "";
		if (!modified.trim().isEmpty())
			modified += ", ";
		
		ChangeEvent changeEvent = new ChangeEvent();
		changeEvent.setDate(new Date().toString());
		changeEvent.setUserName(userId);
		changeEvent.setType(ChangeEventConstants.MODIFICATION);
		List<ChangeEvent> changeEventList = new ArrayList<ChangeEvent>();
		conceptEntry.setChangeEvent(changeEventList);
		
		//conceptEntry.setModified(modified + userId + "@" + (new Date()).toString());

		conceptManager.storeModifiedConcept(conceptEntry);
		
        if (conceptEditBean.isFromHomeScreen()) {
            return "redirect:/home/conceptsearch?word=" + conceptEditBean.getWord() + "&pos="
                    + conceptEditBean.getSelectedPosValue();
        }

		return "redirect:/auth/" + conceptEditBean.getConceptListValue() + "/concepts";
	}

	/**
	 * This method provides the existing concept words for a given synonym
	 * 
	 * @param synonymname
	 *            Synonym concept name for which the existing concepts has to be
	 *            fetched
	 * @return The list of existing concepts
	 */
	@RequestMapping(method = RequestMethod.GET, value = "conceptEditSynonymView")
	public ResponseEntity<String> searchConcept(@RequestParam("synonymname") String synonymname) {
		ConceptEntry[] entries = conceptManager.getConceptListEntriesForWord(synonymname.trim());
		List<ConceptEntry> synonyms = Arrays.asList(entries);
		return new ResponseEntity<String>(buildJSON(synonyms, true, false), HttpStatus.OK);
	}

	/**
	 * This method provides the list of existing synonyms for a concept
	 * 
	 * @param conceptid
	 *            Holds the ID of a concept
	 * @param model
	 *            A generic model holder for Servlet
	 * @return List of existing synonyms
	 * @throws JSONException
	 */
	@RequestMapping(method = RequestMethod.GET, value = "getConceptEditSynonyms")
	public ResponseEntity<String> getSynonyms(@RequestParam("conceptid") String conceptid, ModelMap model) {
		ConceptEntry concept = conceptManager.getConceptEntry(conceptid);
		List<ConceptEntry> synonyms = new ArrayList<ConceptEntry>();
		String synonymIds = concept.getSynonymIds();

		// Inside getConceptEntry . In fillConceptEntry the below logic is
		// performed
		if (synonymIds != null) {
			String[] ids = synonymIds.trim().split(Constants.SYNONYM_SEPARATOR);
			if (ids != null) {
				for (String id : ids) {
					if (id == null || id.isEmpty())
						continue;
					ConceptEntry synonym = conceptManager.getConceptEntry(id);
					if (synonym != null)
						synonyms.add(synonym);
				}
			}
		}
		return new ResponseEntity<String>(buildJSON(synonyms, false, true), HttpStatus.OK);
	}

	/**
	 * This method fetches the synonym details based on the synonym id selected
	 * in the Add synonym table.
	 * 
	 * @param synonymid
	 *            Holds the ID of a synonym
	 * @param model
	 *            A generic model holder for Servlet
	 * @return synonym details
	 */
	@RequestMapping(method = RequestMethod.GET, value = "getConceptAddSynonyms")
	public ResponseEntity<String> getSynonymRows(@RequestParam("synonymid") String synonymid, ModelMap model) {

		ConceptEntry synonym = conceptManager.getConceptEntry(synonymid);

		List<ConceptEntry> synonymList = new ArrayList<ConceptEntry>();
		synonymList.add(synonym);
		return new ResponseEntity<String>(buildJSON(synonymList, false, true), HttpStatus.OK);
	}

	private String buildJSON(List<ConceptEntry> synonyms, boolean posRequired, boolean synonymRequired) {

		int i = 0;
		StringBuffer jsonStringBuilder = new StringBuffer("{");
		jsonStringBuilder.append("\"Total\"");
		jsonStringBuilder.append(":");
		jsonStringBuilder.append(synonyms.size());
		jsonStringBuilder.append(",");
		jsonStringBuilder.append("\"synonyms\":");
		jsonStringBuilder.append("[");
		for (ConceptEntry syn : synonyms) {
			// Appending for next element in JSON
			if (i != 0) {
				jsonStringBuilder.append(",");
			}

			jsonStringBuilder.append("{");
			jsonStringBuilder.append("\"Id\":\"" + syn.getId() + "\"");
			jsonStringBuilder.append(",");
			jsonStringBuilder.append("\"Word\":\"" + syn.getWord() + "\"");
			jsonStringBuilder.append(",");
			jsonStringBuilder.append("\"Description\":\"" + syn.getDescription().replaceAll("\"", "'") + "\"");
			jsonStringBuilder.append(",");
			String pos = syn.getPos().replaceAll("\"", "'");
			jsonStringBuilder.append("\"Pos\":\"" + pos + "\"");
			jsonStringBuilder.append(",");
			jsonStringBuilder.append("\"SynonymObject\":\"" + syn + "\"");
			jsonStringBuilder.append("}");
			i++;
		}
		jsonStringBuilder.append("]");
		jsonStringBuilder.append("}");
		return jsonStringBuilder.toString();
	}
}
