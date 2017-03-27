package edu.asu.conceptpower.rest;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ConceptSearchParameters {

    private String type_uri;
    private String operator;
    private Integer page;
    private String equal_to;
    private String similar_to;
    private String description;
    private String word;
    private String pos;
    private String concept_list;
    private String type_id;
    private String creator;
    private String modified_by;
    private Integer number_of_records_per_page;

    public String getType_uri() {
        return type_uri;
    }

    public void setType_uri(String type_uri) {
        this.type_uri = type_uri;
    }

    public String getOperator() {
        if (this.operator == null) {
            return SearchParamters.OP_AND;
        }
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        if (page != null) {
            this.page = page;
        }
    }

    public String getSimilar_to() {
        if (this.similar_to != null) {
            List<String> similarList = Arrays.asList(this.similar_to.split(","));
            return similarList.stream().map(i -> removeTrailingBackSlash(i)).collect(Collectors.joining(","));
        }
        return similar_to;
    }

    public void setSimilar_to(String similar_to) {
        this.similar_to = similar_to;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * This method returns the search term for the field 
     * <code>word</word>. The method automatically lowercases the
     * search term.
     * 
     * @return
     */
    public String getWord() {
        if (word != null) {
            return word.toLowerCase();
        }
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    /**
     * This method return the search term for the field <code>pos</code>
     * (part of speech). It automatically lowercases the search term.
     * @return
     */
    public String getPos() {
        if (pos != null) {
            return pos.toLowerCase();
        }
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public String getConcept_list() {
        return concept_list;
    }

    public void setConcept_list(String concept_list) {
        this.concept_list = concept_list;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getModified_by() {
        return modified_by;
    }

    public void setModified_by(String modified_by) {
        this.modified_by = modified_by;
    }

    public Integer getNumber_of_records_per_page() {
        return number_of_records_per_page;
    }

    public void setNumber_of_records_per_page(Integer number_of_records_per_page) {
        if (number_of_records_per_page != null) {
            this.number_of_records_per_page = number_of_records_per_page;
        }
    }

    public String getEqual_to() {
        if (this.equal_to != null) {
            List<String> equalsList = Arrays.asList(this.equal_to.split(","));
            return equalsList.stream().map(i -> removeTrailingBackSlash(i)).collect(Collectors.joining(","));
        }
        return equal_to;
    }

    public void setEqual_to(String equal_to) {
        this.equal_to = equal_to;
    }

    private String removeTrailingBackSlash(String val) {
        if (val.endsWith("/")) {
            return val.substring(0, val.length() - 1);
        }
        return val;
    }
}
