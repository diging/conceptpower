package edu.asu.conceptpower.app.core.model.impl;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import edu.asu.conceptpower.servlet.core.ChangeEvent.ChangeEventTypes;


@Entity
@Table(name="change_event")
public class ChangeEvent implements Serializable {

    private static final long serialVersionUID = 1635835636441631877L;
    
    @Id
    @Column(name="change_event_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    
    @ManyToOne
    @JoinColumn(name = "concept_entry_id", referencedColumnName="concept_id")
    private ConceptEntry concept;

    @Column(name="user_name")
    private String userName;

    @Column(name="date")
    private Date date;

    @Column(name="type")
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ChangeEventTypes getType() {
        return type;
    }

    public void setType(ChangeEventTypes type) {
        this.type = type;
    }
    
    public ConceptEntry getConcept() {
        return concept;
    }
    
    public void setConcept(ConceptEntry c) {
        this.concept = c;
    }
}