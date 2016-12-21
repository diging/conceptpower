package edu.asu.conceptpower.rest.msg;

public interface IMessageRegistry {

    /**
     * This method returns the message converter object based on the media type.
     * 
     * @param mediaType
     * @return
     */
    public IMessageConverter getMessageFactory(String mediaType);

}
