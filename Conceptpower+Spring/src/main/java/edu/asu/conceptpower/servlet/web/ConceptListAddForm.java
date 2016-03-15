package edu.asu.conceptpower.servlet.web;

public class ConceptListAddForm {

	private String listName;
	private String description;
	private String oldListName;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getListName() {
		return listName;
	}

	public void setListName(String listName) {
		this.listName = listName;
	}

	public String getOldListName() {
		return oldListName;
	}

	public void setOldListName(String oldListName) {
		this.oldListName = oldListName;
	}
}
