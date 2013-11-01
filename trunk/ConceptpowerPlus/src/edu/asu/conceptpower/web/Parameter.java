package edu.asu.conceptpower.web;

public interface Parameter {

	public final static String DB_PROVIDER_CONTEXT_PARAMETER = "DBProvider";
	public final static String WORDNET_CONFIGURATION_CONTEXT_PARAMETER = "WordNetConfiguration";
	public final static String USER_MAP = "users";
	
	// input for word finding
	public final static String INPUT_FORM_WORD_PARAMETER = "findWord";
	public final static String INPUT_FORM_POS_PARAMETER = "wordPOS";
	
	// return word finding results
	public final static String RETURN_DICT_ENTRY = "foundDictEntry";
	
	// login
	public final static String IS_LOGGED_IN = "loggedIn";
	public final static String IS_ADMIN = "isAdmin";
	public final static String USERNAME = "name";
	public final static String PASSWORD = "pw";
	
	// dictionaries
	public final static String DICT_NAME = "name";
	public final static String DICT_DESCRIPTION = "description";
	public final static String DICT_EXISTS_ERROR = "dictExistsError";
	
	// entries
	public final static String USER_ERROR = "error";
	
	// concept lists
	public final static String SELECTED_LIST = "selectedList";
	public final static String SELECTED_CONCEPT = "selectedConcept";
	public final static String DELETE_CONCEPT = "deleteConcept";
	public final static String SELECTED_TYPE = "selectedType";
}
