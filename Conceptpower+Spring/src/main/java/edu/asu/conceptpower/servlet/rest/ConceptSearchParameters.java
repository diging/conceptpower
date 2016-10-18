package edu.asu.conceptpower.servlet.rest;

public class ConceptSearchParameters {

    private String type_uri;
    private String operator;
    private Integer page;
    private String equals_to;
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

    public String getEquals_to() {
        return equals_to;
    }

    public void setEquals_to(String equals_to) {
        this.equals_to = equals_to;
    }

    public String getSimilar_to() {
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

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getPos() {
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
}
