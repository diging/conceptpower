package edu.asu.conceptpower.app.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * This model helps to keep track of information about a specific concept
 * 
 * @author Keerthivasan Krishnamurthy
 * 
 */

@Entity
@Table(name="concept_entry")
public class ConceptEntry implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="concept_id")
    private String id;

    @Column(name="wordnet_id")
    private String wordnetId;
    
    @Column(name="word")
    private String word;

    @Column(name="description")
    private String description;

    @Column(name="pos")
    private String pos;

    @Column(name="concept_list")
    private String conceptList;

    @Column(name="type_id")
    private String typeId;

    @Column(name="equal_to")
    private String equalTo;

    @Column(name="similar_to")
    private String similarTo;

    @Column(name="synonym_ids")
    private String synonymIds;

    @Column(name="synset_ids")
    private String synsetIds;

    @Column(name="narrows")
    private String narrows;

    @Column(name="broadens")
    private String broadens;

    @Column(name="creator_id")
    private String creatorId;

    @Column(name="modified")
    private String modified;

    @Column(name="merged_ids")
    private String mergedIds;

    @Column(name="is_deleted")
    private boolean isDeleted;
    
    @Column(name="modified_user")
    private String modifiedUser;

    @OneToMany(mappedBy = "concept", cascade = CascadeType.ALL)
    private List<ChangeEvent> changeEvents = new ArrayList<>();
     
    @Column(name="alternative_ids")
    private String alternativeIds;

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getSynonymIds() {
        return synonymIds;
    }
 
    public void setSynonymIds(String synonymIds) {
        this.synonymIds = synonymIds;
    }
   
    public String getNarrows() {
        return narrows;
    }
       
    public void setNarrows(String narrows) {
        this.narrows = narrows;
    }
   
    public String getBroadens() {
        return broadens;
    }
   
    public void setBroadens(String broadens) {
        this.broadens = broadens;
    }
    
    public String getTypeId() {
        return typeId;
    }
    
    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }
    
    public String getEqualTo() {
        return equalTo;
    }
    
    public void setEqualTo(String equal) {
       this.equalTo = equal;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getWordnetId() {
        return wordnetId;
    }
    
    public void setWordnetId(String wordnetId) {
        this.wordnetId = wordnetId;
    }
    
    public String getWord() {
        return word;
    }
    
    public void setWord(String word) {
        this.word = word;
    }
       
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }   
    
    public String getPos() {
        return pos;
    }
   
    public void setPos(String pos) {
        this.pos = pos;
    }
    
    public String getConceptList() {
        return conceptList;
    }
  
    public void setConceptList(String conceptList) {
        this.conceptList = conceptList;
    }   
    
    public String getSimilarTo() {
        return similarTo;
    }
   
    public void setSimilarTo(String similar) {
        this.similarTo = similar;
    }
   
    public String getSynsetIds() {
        return synsetIds;
    }
       
    public void setSynsetIds(String synsetIds) {
        this.synsetIds = synsetIds;
    }
  
    public void setModified(String modified) {
        this.modified = modified;
    }
   
    public String getModified() {
        return modified;
    }
   
    public boolean isDeleted() {
        return isDeleted;
    }
   
    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
   
    public List<ChangeEvent> getChangeEvents() {
         return this.changeEvents;
    }
   
    public void setChangeEvents(ChangeEvent changeEvent) {
        if(this.changeEvents == null) {
            this.changeEvents = new ArrayList<>();
        }
        this.changeEvents.add(changeEvent);
    }
   
    public String getModifiedUser() {
        return modifiedUser;
    }
    
    public void setModifiedUser(String modifiedUser) {
        this.modifiedUser = modifiedUser;
    }
    
    public String getMergedIds() {
        return mergedIds;
    }
       
    public void setMergedIds(String mergedIds) {
        this.mergedIds = mergedIds;
    }
    
    public Set<String> getAlternativeIds() {
        if(this.alternativeIds != null) {
            String[] ids = this.alternativeIds.split(",");
            return new HashSet<>(Arrays.asList(ids));
        }
        return null;
    }
    
    public void setAlternativeIds(String alternativeIds) {
        if(alternativeIds == null || alternativeIds.isEmpty()) {
            this.alternativeIds = alternativeIds;
        }else {
            this.alternativeIds = this.alternativeIds+","+alternativeIds;
        }
        
    }
}