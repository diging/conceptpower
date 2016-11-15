package edu.asu.conceptpower.app.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConceptSuperTypeJson {

    @JsonProperty("type_id")
    private String typeId;

    @JsonProperty("type_uri")
    private String typeUri;

    @JsonProperty("type_name")
    private String typeName;

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTypeUri() {
        return typeUri;
    }

    public void setTypeUri(String typeUri) {
        this.typeUri = typeUri;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
