package edu.asu.conceptpower.core;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ChangeEvent implements Comparable{

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

	public ChangeEvent() {
	}

	public ChangeEvent(String userName, String date, String type) {
		this.userName = userName;
		this.date = date;
		this.type = type;
	}

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

	@Override
	public int compareTo(Object o) {
		ChangeEvent changeEvent = (ChangeEvent)o;
		//Wed Mar 30 10:59:42 MST 2016
		SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
		try {
			return sdf.parse(date).before(sdf.parse(changeEvent.getDate())) ? 1 : 0;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
}
