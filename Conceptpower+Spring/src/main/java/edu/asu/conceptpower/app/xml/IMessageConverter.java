package edu.asu.conceptpower.app.xml;

public interface IMessageConverter {


    public IConceptMessage createConceptMessage();

    public ITypeMessage createTypeMessage();

    public String getMediaType();

}
