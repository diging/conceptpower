package edu.asu.conceptpower.core;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;

import edu.asu.conceptpower.servlet.core.ChangeEvent;


public class ReviewRequest {

   
    private List<ChangeEvent> changeEvents = new ArrayList<ChangeEvent>();

    
    private String comment;
    private String requester;
    private String resolver;
    private String conceptLink;
    private String status;
    private boolean isDeleted;
    private String id;
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ReviewRequest() {
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getRequester() {
        return requester;
    }

    public void setRequester(String requester) {
        this.requester = requester;
    }

    public String getResolver() {
        return resolver;
    }

    public void setResolver(String resolver) {
        this.resolver = resolver;
    }

    public String getConceptLink() {
        return conceptLink;
    }

    public void setConceptLink(String conceptLink) {
        this.conceptLink = conceptLink;
    }

    public boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
    
    /**
     * This method add the changeevent to the end of the list. The first element
     * will always be a creator
     * 
     * @param event
     */
    public void addNewChangeEvent(ChangeEvent event) {

        if (changeEvents == null) {
            this.changeEvents = new ArrayList<ChangeEvent>();
        }
        // Appends to the end of the list
        this.changeEvents.add(event);
    }

}
