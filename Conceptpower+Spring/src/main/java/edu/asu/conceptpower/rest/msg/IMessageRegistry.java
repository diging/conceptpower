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
     * Valid values for media types are defined by the registered message converters.
     * Currently, registered media types are for XML or JSON.
     * 
     * @param mediaType application/xml or application/json
     * @return Message converter according to provided media type or null.
     */
    public IMessageConverter getMessageFactory(String mediaType);

}
