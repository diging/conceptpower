package edu.asu.conceptpower.servlet.core;

import java.util.Date;

public class ChangeEvent implements Comparable<ChangeEvent> {

    public enum ChangeEventTypes {
        MODIFICATION, CREATION, DELETION
    }

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
    private ChangeEventTypes type;

    public ChangeEvent() {
    }

    public ChangeEvent(String userName, Date date, ChangeEventTypes type) {
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

    @Override
    public int compareTo(ChangeEvent changeEvent) {
        return date.before(changeEvent.getDate()) ? 1 : 0;
    }

    public ChangeEventTypes getType() {
        return type;
    }

    public void setType(ChangeEventTypes type) {
        this.type = type;
    }
}