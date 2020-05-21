package edu.asu.conceptpower.app.core.model.impl;

import java.io.Serializable;

import edu.asu.conceptpower.app.core.model.IConceptList;

public class ConceptList implements IConceptList,Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 2325716955354296983L;
    
    private String conceptListName;
    
    private String description;
    
    private String id;
    
    public String getConceptListName() {
        return conceptListName;
    }

    public void setConceptListName(String conceptName) {
        this.conceptListName = conceptName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String getId() {
        return id;
    }
    
}