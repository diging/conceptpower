package edu.asu.conceptpower.core;

public class ReviewRequest {

    private String comment;

    public ReviewRequest() {

    }

    enum Status {
        Opened, Resolved, Rejected;
    }

    private String requester;
    private String resolver;
    private String conceptLink;

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

}
