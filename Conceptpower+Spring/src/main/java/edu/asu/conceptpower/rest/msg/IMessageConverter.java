package edu.asu.conceptpower.rest.msg;

public interface IMessageConverter {


    public IConceptMessage createConceptMessage();

    public ITypeMessage createTypeMessage();

    public String getMediaType();

}
