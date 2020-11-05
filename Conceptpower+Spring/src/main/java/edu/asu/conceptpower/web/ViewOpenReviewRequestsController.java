package edu.asu.conceptpower.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import edu.asu.conceptpower.app.manager.RequestsManager;

/**
 * This endpoint renders the UI of the open review requests 
 * 
 * @author Keerthivasan Krishnamurthy
 * 
 */

@Controller
public class ViewOpenReviewRequestsController {
	@Autowired
    private RequestsManager requestsMgr;

	@GetMapping(value="/auth/request/all/open")
    public String viewOpenReviewRequests(ModelMap model) {
        model.addAttribute("openRequests", requestsMgr.getAllOpenReviews());
        return "layouts/concepts/openrequests";
    }
}
