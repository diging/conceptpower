package edu.asu.conceptpower.app.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ConceptList {


    /**
     * 
     */
    private static final long serialVersionUID = 2619043575898005700L;

    private String conceptListName;
    
    private String description;
    
    @Id
    private String id;
    
    /**
     * Return the name of a concept list.
     * @return
     */
    public String getConceptListName() {
        return conceptListName;
    }

    public void setConceptListName(String conceptName) {
        this.conceptListName = conceptName;
    }

    /**
     * Returns a description of a concept list.
     * @return
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the id of a concept list.
     */
    public String getId() {
        return id;
    }

}
