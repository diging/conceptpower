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
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import edu.asu.conceptpower.app.constants.LuceneFieldNames;
import edu.asu.conceptpower.app.constants.SearchFieldNames;
import edu.asu.conceptpower.app.reflect.LuceneField;
import edu.asu.conceptpower.app.reflect.SearchField;

/**
 * This model helps to keep track of information about a specific concept
 * 
 * @author Julia Damerow
 * 
 */

@Entity
@Table(name="concept_entry")
public class ConceptEntry implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="concept_id")
    @LuceneField(lucenefieldName = LuceneFieldNames.ID, isTokenized = false, isMultiple = false, isSortAllowed = true)
    private String id;

    @Column(name="wordnet_id")
    @SearchField(fieldName = SearchFieldNames.WORDNETID)
    @LuceneField(lucenefieldName = LuceneFieldNames.WORDNETID, isTokenized = false, isMultiple = true, isSortAllowed = true)
    private String wordnetId;
    
    @Column(name="word")
    @SearchField(fieldName = SearchFieldNames.WORD)
    @LuceneField(lucenefieldName = LuceneFieldNames.WORD, isTokenized = true, isMultiple = false, isShortPhraseSearchable = true, isWildCardSearchEnabled = true, isSortAllowed = true)
    private String word;

    @Column(name="description")
    @SearchField(fieldName = SearchFieldNames.DESCRIPTION)
    @LuceneField(lucenefieldName = LuceneFieldNames.DESCRIPTION, isTokenized = true, isMultiple = false, isSortAllowed = true)
    private String description;

    @Column(name="pos")
    @SearchField(fieldName = SearchFieldNames.POS)
    @LuceneField(lucenefieldName = LuceneFieldNames.POS, isTokenized = true, isMultiple = false, isSortAllowed = true)
    private String pos;

    @Column(name="concept_list")
    @SearchField(fieldName = SearchFieldNames.CONCEPT_LIST)
    @LuceneField(lucenefieldName = LuceneFieldNames.CONCEPT_LIST, isTokenized = false, isMultiple = false, isSortAllowed = true)
    private String conceptList;

    @Column(name="type_id")
    @SearchField(fieldName = SearchFieldNames.TYPE_ID)
    @LuceneField(lucenefieldName = LuceneFieldNames.TYPE_ID, isTokenized = false, isMultiple = false, isSortAllowed = true)
    private String typeId;

    @Column(name="equal_to")
    @SearchField(fieldName = SearchFieldNames.EQUAL_TO)
    @LuceneField(lucenefieldName = LuceneFieldNames.EQUALS_TO, isTokenized = false, isMultiple = true)
    private String equalTo;

    @Column(name="similar_to")
    @SearchField(fieldName = SearchFieldNames.SIMILAR_TO)
    @LuceneField(lucenefieldName = LuceneFieldNames.SIMILAR_TO, isTokenized = false, isMultiple = true)
    private String similarTo;

    @Column(name="synonym_ids")
    @SearchField(fieldName=SearchFieldNames.SYNONYM_ID)
    @LuceneField(lucenefieldName = LuceneFieldNames.SYNONYMID, isTokenized = false, isMultiple = true)
    private String synonymIds;

    @Column(name="synset_ids")
    private String synsetIds;

    @Column(name="narrows")
    private String narrows;

    @Column(name="broadens")
    private String broadens;

    @Column(name="creator_id")
    @SearchField(fieldName = SearchFieldNames.CREATOR)
    @LuceneField(lucenefieldName = LuceneFieldNames.CREATOR, isTokenized = false, isMultiple = false)
    private String creatorId;

    @Column(name="modified")
    @SearchField(fieldName = SearchFieldNames.MODIFIED)
    @LuceneField(lucenefieldName = LuceneFieldNames.MODIFIED, isTokenized = false, isMultiple = false)
    private String modified;

    @Column(name="merged_ids")
    @SearchField(fieldName = SearchFieldNames.MERGED_IDS)
    @LuceneField(lucenefieldName = LuceneFieldNames.MERGED_IDS, isTokenized = false, isMultiple = true)
    private String mergedIds;

    @Column(name="is_deleted")
    private boolean isDeleted;
    
    @Column(name="modified_user")
    private String modifiedUser;

    @OneToMany( cascade = CascadeType.ALL, fetch= FetchType.EAGER)
    @JoinColumn(name = "concept_entry_id", referencedColumnName="concept_id")
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
   
    public void addChangeEvent(ChangeEvent changeEvent) {
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
        return new HashSet<>();
    }
    
    public void addAlternativeId(String alternativeId) {
        
        if(alternativeId == null) return;
        
        if(this.alternativeIds == null || this.alternativeIds.isEmpty()) {
            this.alternativeIds = alternativeId;
        }else {
            /*This loop is to handle alternativeId parameter with one or more alternativeIds*/
            for(String altId : alternativeId.split(",")) {
                altId = altId.trim();
                this.alternativeIds = (altId.length() > 0 && checkDuplicate(altId)) ? this.alternativeIds : this.alternativeIds+","+altId;
            }
        }
    }
    
    private boolean checkDuplicate(String alternativeId) {
        /*Split the string and check if the given alternativeId is duplicate or not*/
        Set<String> ids = new HashSet<>(Arrays.asList(this.alternativeIds.split(",")));
        
        return ids.contains(alternativeId);
    }
}