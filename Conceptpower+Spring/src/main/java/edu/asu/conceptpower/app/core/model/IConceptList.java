package edu.asu.conceptpower.app.core.model;


public interface IConceptList{
    
    public abstract String getConceptListName();

    public abstract void setConceptListName(String conceptName);
    
    public abstract String getDescription();

    public abstract void setDescription(String description);

    public abstract void setId(String id);
    
    public abstract String getId();
    
}