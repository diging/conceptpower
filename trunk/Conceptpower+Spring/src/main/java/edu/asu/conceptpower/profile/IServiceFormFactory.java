package edu.asu.conceptpower.profile;

import edu.asu.conceptpower.profile.impl.ServiceForm;

/**
 * this interface has method which creates new object of class ServiceForm
 * 
 * @author rohit
 * 
 */
public interface IServiceFormFactory {

	/**
	 * creates the new object of ServiceForm class
	 * 
	 * @return object of ServiceForm class
	 */
	public ServiceForm getServiceFormObject();

}
