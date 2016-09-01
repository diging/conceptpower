package edu.asu.conceptpower.servlet.core;

import java.util.Date;

public class ChangeEvent implements Comparable<ChangeEvent> {

    /**
     * Stores the username of the creator or modified userName
     */
    private String userName;

    /**
     * Stores the time of change
     */
    private Date date;

    /**
     * Stores the type as Creation, Modification, Deletion
     */
    private String type;

    public ChangeEvent() {
    }

    public ChangeEvent(String userName, Date date, String type) {
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int compareTo(ChangeEvent o) {
        ChangeEvent changeEvent = (ChangeEvent) o;
        return date.before(changeEvent.getDate()) ? 1 : 0;
    }
}