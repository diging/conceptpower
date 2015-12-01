package edu.asu.conceptpower.web;

import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class ConceptTypeAddForm {

	private String typeName;
	private String typeDescription;
	private String matches;
	private Map<String, String> types;
	private String selectedType;

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

	public String getSelectedType() {
		return selectedType;
	}

	public void setSelectedType(String selectedType) {
		this.selectedType = selectedType;
	}

}
