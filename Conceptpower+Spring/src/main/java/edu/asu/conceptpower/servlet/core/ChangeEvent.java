package edu.asu.conceptpower.servlet.core;

public class ChangeEvent {

	/**
	 * Stores the username of the creator or modified userName
	 */
	private String userName;

	/**
	 * Stores the time of change
	 */
	private String date;

	/**
	 * Stores the type as Creation, Modification, Deletion
	 */
	private String type;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
