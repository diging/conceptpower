package edu.asu.conceptpower.app.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * This model helps to keep track of information about a specific concept list
 * 
 * @author Julia Damerow
 * 
 */
@Entity
@Table(name="concept_list")
public class ConceptList implements Serializable{

    private static final long serialVersionUID = 1L;
    
    @Id
    private String id;
    
    @Column(name="concept_list_name")
    private String conceptListName;
    
    @Column(name="description")
    private String description;
    
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