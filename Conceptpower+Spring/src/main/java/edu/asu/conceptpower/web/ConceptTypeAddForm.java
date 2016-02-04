package edu.asu.conceptpower.web;

import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class ConceptTypeAddForm {

    private String typeName;
    // Blank value has been added to prevent null pointer exception in validator
    // while adding type
    private String oldTypeName = "";
    private String typeDescription;
    private String matches;
    private Map<String, String> types;
    private String superType;
    private String typeid;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeDescription() {
        return typeDescription;
    }

    public void setTypeDescription(String typeDescription) {
        this.typeDescription = typeDescription;
    }

    public String getMatches() {
        return matches;
    }

    public void setMatches(String matches) {
        this.matches = matches;
    }

    public Map<String, String> getTypes() {
        return types;
    }

    public void setTypes(Map<String, String> types) {
        this.types = types;
    }

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }

    public String getOldTypeName() {
        return oldTypeName;
    }

    public void setOldTypeName(String oldTypeName) {
        this.oldTypeName = oldTypeName;
    }

    public String getSuperType() {
        return superType;
    }

    public void setSuperType(String superType) {
        this.superType = superType;
    }
}
