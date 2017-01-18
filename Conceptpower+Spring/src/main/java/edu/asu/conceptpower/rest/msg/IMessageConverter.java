package edu.asu.conceptpower.rest.msg;

/**
 * This interface is implemented across all message converters in the
 * application. Currently we have XML and JSON converters. These message
 * converters are loaded from message registry.
 * 
 * @author karthik
 *
 */
public interface IMessageConverter {

    /**
     * This method returns the instance of concept message based on the message
     * converter.
     * 
     * @return
     */
    public IConceptMessage createConceptMessage();

    /**
     * This method returns the instance of concept type based on message
     * converter.
     * 
     * @return
     */
    public ITypeMessage createTypeMessage();

    /**
     * This method returns the message converter type.
     * 
     * @return
     */
    public String getMediaType();

}
