package edu.asu.conceptpower.servlet.json;

import edu.asu.conceptpower.root.URIHelper;

public class JsonMessageFactory {

    private URIHelper uriCreator;

    public JsonMessageFactory(URIHelper uriCreator) {
        this.uriCreator = uriCreator;
    }

    public JsonConceptMessage createJsonConceptMessage() {
        return new JsonConceptMessage(uriCreator);
    }

}
