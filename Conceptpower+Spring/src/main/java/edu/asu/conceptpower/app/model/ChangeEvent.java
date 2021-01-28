package edu.asu.conceptpower.app.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This Model holds information about the change events for a specific concept
 * 
 * @author Keerthivasan Krishnamurthy, Digital Innovation Group
 * 
 */

@Entity
@Table(name="change_event")
public class ChangeEvent implements Serializable, Comparable<ChangeEvent> {

    private static final long serialVersionUID = 1L;
    
    public enum ChangeEventTypes {
        MODIFICATION, CREATION, DELETION
    }
    
    public ChangeEvent() {
    }

    public ChangeEvent(String userName, String date, ChangeEventTypes type) {
        this.userName = userName;
        this.date = date;
        this.type = type;
    }
    
    @Id
    @Column(name="change_event_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    
    @Column(name = "concept_entry_id")
    private String conceptEntryId;

    @Column(name="user_name")
    private String userName;

    @Column(name="date")
    private String date;

    @Column(name="type")
    @Enumerated(EnumType.STRING)
    private ChangeEventTypes type;

    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
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

    public ChangeEventTypes getType() {
        return type;
    }

    public void setType(ChangeEventTypes type) {
        this.type = type;
    }
    
    public String getConceptEntryId() {
        return this.conceptEntryId;
    }
    
    public void setConceptEntryId(String conceptEntryId) {
        this.conceptEntryId = conceptEntryId;
    }

    @Override
    public int compareTo(ChangeEvent changeEvent) {
        return date.compareTo(changeEvent.getDate());
    }
}