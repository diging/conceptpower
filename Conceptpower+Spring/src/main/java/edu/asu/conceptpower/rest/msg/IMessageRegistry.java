package edu.asu.conceptpower.rest.msg;

/**
 * This interface provide methods for obtaining message factory objects.
 * 
 * @author karthik
 *
 */
public interface IMessageRegistry {

    /**
     * This method returns the message converter object based on the media type.
     * Valid values for media types are accept headers of XML and JSON.
     * 
     * @param mediaType
     * @return
     */
    public IMessageConverter getMessageFactory(String mediaType);

}
