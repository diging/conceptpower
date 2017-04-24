package edu.asu.conceptpower.web.backing;

public class ConceptWrapperAddBean {

    private String word;
    private String selectedConceptList;
    private String description;
    private String synonymids;
    private String selectedType;
    private String equals;
    private String similar;
    private String wrapperids;

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

    public String getWrapperids() {
        return wrapperids;
    }

    public void setWrapperids(String wrapperids) {
        this.wrapperids = wrapperids;
    }

    public String getEquals() {
        return equals;
    }

    public void setEquals(String equals) {
        this.equals = equals;
    }

    public String getSimilar() {
        return similar;
    }

    public void setSimilar(String similar) {
        this.similar = similar;
    }

    public String getSelectedConceptList() {
        return selectedConceptList;
    }

    public void setSelectedConceptList(String selectedConceptList) {
        this.selectedConceptList = selectedConceptList;
    }

    public String getSelectedType() {
        return selectedType;
    }

    public void setSelectedType(String selectedType) {
        this.selectedType = selectedType;
    }

    public String getSynonymids() {
        return synonymids;
    }

    public void setSynonymids(String synonymids) {
        this.synonymids = synonymids;
    }

}
