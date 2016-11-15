package edu.asu.conceptpower.servlet.xml;

public interface IMessageConverter {


    public IConceptMessage createConceptMessage();

    public ITypeMessage createTypeMessage();

    public String getMediaType();

}
