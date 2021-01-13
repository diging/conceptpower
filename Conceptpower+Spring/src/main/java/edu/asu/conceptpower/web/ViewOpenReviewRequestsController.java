package edu.asu.conceptpower.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import edu.asu.conceptpower.app.exceptions.LuceneException;
import edu.asu.conceptpower.app.manager.IConceptManager;
import edu.asu.conceptpower.app.manager.IIndexService;
import edu.asu.conceptpower.app.model.ConceptEntry;
import edu.asu.conceptpower.app.model.ReviewStatus;
import edu.asu.conceptpower.app.wrapper.ConceptEntryWrapper;
import edu.asu.conceptpower.app.wrapper.IConceptWrapperCreator;

/**
 * This endpoint renders the UI of the open review requests
 * 
 * @author Keerthivasan Krishnamurthy
 * 
 */

@Controller
public class ViewOpenReviewRequestsController {
	@Autowired
	private IConceptManager conceptsManager;

	@Autowired
	private IConceptWrapperCreator wrapperCreator;

	@Autowired
	private IIndexService indexService;

	@Value("#{messages['INDEXER_RUNNING']}")
	private String indexerRunning;

	@Value("#{messages['INDEXERSTATUS']}")
	private String indexerStatus;

	@Value("${open_req_default_page_size}")
	private Integer defaultNumRecordsPerPage;
	
	@GetMapping(value = "/auth/request/all/open")
	public String viewOpenReviewRequests(ModelMap model, String page, String pageSize) {
		if(indexService.isIndexerRunning()) {
			model.addAttribute("show_error_alert", true);
			model.addAttribute("error_alert_msg", indexerRunning);

			return "layouts/concepts/openrequests";
		}
		
		int pageNo = page == null ? 0 : Integer.parseInt(page);
		int numRecordsPerPage = pageSize == null ? defaultNumRecordsPerPage : Integer.parseInt(pageSize);
		
		pageNo = pageNo < 0 ? 0 : pageNo;
		numRecordsPerPage = numRecordsPerPage < 0 ? defaultNumRecordsPerPage : numRecordsPerPage;  
		
		int totalPageCount = (int) Math.ceil((double)conceptsManager.getNumberOfConceptsByStatus(ReviewStatus.OPENED)/(double) numRecordsPerPage);
		
		List<ConceptEntry> openReviewConcepts = conceptsManager.getAllConceptsByStatusPaginated(ReviewStatus.OPENED, pageNo, numRecordsPerPage);
		List<ConceptEntryWrapper> foundConcepts = new ArrayList<>();
		
		try {
		    if(!openReviewConcepts.isEmpty()) {
		        foundConcepts = wrapperCreator
		                .createWrappers(openReviewConcepts.toArray(new ConceptEntry[openReviewConcepts.size()]));
		    }
		} catch (LuceneException e) {
			model.addAttribute(indexerStatus, e.getMessage());
			e.printStackTrace();
		}

		model.addAttribute("openRequests", foundConcepts);
		model.addAttribute("page",pageNo);
		model.addAttribute("count",totalPageCount);
		
		return "layouts/concepts/openrequests";
	}
}
