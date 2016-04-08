package edu.asu.conceptpower.servlet.web.backing;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component("conceptAddBean")
public class ConceptAddBean {

	private String name;
	private String pos;
	private Map<String, String> lists = new HashMap<String, String>();
	private String selectedList;
	private String description;
	private Map<String, String> types = new HashMap<String, String>();
	private String selectedTypes;
	private String equals;
	private String similar;
	private String synonymsids;
	private Map<String, String> serviceNameIdMap = new HashMap<String, String>();
	private String selectedServiceNameIdMap;
	private String term;
	private String synonymDescription;
	private String synonymId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPos() {
		return pos;
	}

	public void setPos(String pos) {
		this.pos = pos;
	}

	public Map<String, String> getLists() {
		return lists;
	}

	public void setLists(Map<String, String> lists) {
		this.lists = lists;
	}

	public String getSelectedList() {
		return selectedList;
	}

	public void setSelectedList(String selectedList) {
		this.selectedList = selectedList;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSelectedTypes() {
		return selectedTypes;
	}

	public void setSelectedTypes(String selectedTypes) {
		this.selectedTypes = selectedTypes;
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

	public String getSynonymsids() {
		return synonymsids;
	}

	public void setSynonymsids(String synonymsids) {
		this.synonymsids = synonymsids;
	}

	public Map<String, String> getTypes() {
		return types;
	}

	public void setTypes(Map<String, String> types) {
		this.types = types;
	}

	public Map<String, String> getServiceNameIdMap() {
		return serviceNameIdMap;
	}

	public void setServiceNameIdMap(Map<String, String> serviceNameIdMap) {
		this.serviceNameIdMap = serviceNameIdMap;
	}

	public String getSelectedServiceNameIdMap() {
		return selectedServiceNameIdMap;
	}

	public void setSelectedServiceNameIdMap(String selectedServiceNameIdMap) {
		this.selectedServiceNameIdMap = selectedServiceNameIdMap;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public String getSynonymDescription() {
		return synonymDescription;
	}

	public void setSynonymDescription(String synonymDescription) {
		this.synonymDescription = synonymDescription;
	}

	public String getSynonymId() {
		return synonymId;
	}

	public void setSynonymId(String synonymId) {
		this.synonymId = synonymId;
	}

}
