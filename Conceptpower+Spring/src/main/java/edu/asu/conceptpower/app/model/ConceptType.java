package edu.asu.conceptpower.app.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * This model helps to keep track of information about a specific concept type
 * 
 * @author Keerthivasan Krishnamurthy, Digital Innovation Group
 * 
 */

@Entity
@Table(name="concept_type")
public class ConceptType implements Serializable{

    private static final long serialVersionUID = 1L;
    
    @Id 
    @Column(name="type_id")
    private String typeId;
    
    @Column(name="type_name")
    private String typeName;
    
    @Column(name="description")
    private String description;
    
    @Column(name="matches")
    private String matches;
    
    @Column(name="creator_id")
    private String creatorId;
    
    @Column(name="modified")
    private String modified;
    
    @Column(name="supertype_id")
    private String supertypeId;
    
    public ConceptType() {
        
    }
    
    public ConceptType(String id) {
        this.typeId = id;
    }
    
    public String getTypeId() {
        return typeId;
    }
    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }
    public String getTypeName() {
        return typeName;
    }
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getMatches() {
        return matches;
    }
    public void setMatches(String matches) {
        this.matches = matches;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getModified() {
        return modified;
    }

    public void setSupertypeId(String supertypeId) {
        this.supertypeId = supertypeId;
    }

    public String getSupertypeId() {
        return supertypeId;
    }
    
}