package edu.asu.conceptpower.app.core.model;

public interface IConceptType{
    
    public abstract String getTypeId();
    
    public abstract void setTypeId(String typeId);
    
    public abstract String getTypeName();
    
    public abstract void setTypeName(String typeName);
    
    public abstract String getDescription();
    
    public abstract void setDescription(String description);
    
    public abstract String getMatches();
    
    public abstract void setMatches(String matches);

    public abstract void setCreatorId(String creatorId);

    public abstract String getCreatorId();

    public abstract void setModified(String modified);

    public abstract String getModified();

    public abstract void setSupertypeId(String supertypeId);

    public abstract String getSupertypeId();
}