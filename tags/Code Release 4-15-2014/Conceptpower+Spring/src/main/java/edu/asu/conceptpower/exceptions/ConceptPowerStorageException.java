package edu.asu.conceptpower.exceptions;

/**
 * This is an exception thrown when there is a storage problem.
 * @author Julia Damerow
 * @author Lohith Dwaraka
 */
public class ConceptPowerStorageException extends Exception {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3850218568287768164L;
	
	/**
	 * default storage exception
	 */
	public ConceptPowerStorageException() {
		super();
	}
	
	/**
	 * Custom message in the exception
	 * @param customMsg
	 */
	public ConceptPowerStorageException(String customMsg) {
		super(customMsg);
	}


	
	public ConceptPowerStorageException(Exception e)
	{
		super(e);
	}


	public ConceptPowerStorageException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}
	
	

}
