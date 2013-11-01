package edu.asu.conceptpower.web;

import java.io.File;
import java.io.Serializable;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import edu.asu.conceptpower.db4o.DBNames;
import edu.asu.conceptpower.db4o.DatabaseProvider;

@ManagedBean(name="databaseController")
@ApplicationScoped
public class DatabaseController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4792614830047168522L;
	
	private DatabaseProvider databaseProvider;
	
	public DatabaseController() {
    	ExternalContext context = FacesContext.getCurrentInstance().getExternalContext(); 
		String dbFolder = context.getInitParameter(Configuration.DATABASE_PATH_PARAMETER).endsWith(File.separator) ? context.getInitParameter(Configuration.DATABASE_PATH_PARAMETER) : context.getInitParameter(Configuration.DATABASE_PATH_PARAMETER) + File.separator;
		
		String wordnetCachePath = dbFolder +
    		context.getInitParameter(Configuration.WORDNET_CACHE_NAME_PARAMETER);  
   
		databaseProvider = new DatabaseProvider();
		databaseProvider.addDatabaseManager(DBNames.WORDNET_CACHE, wordnetCachePath, false);
	    
	    // add user database
	    String usersFilepath = dbFolder +
    		context.getInitParameter(Configuration.USER_DATABASE_NAME_PARAMETER);  
	    databaseProvider.addDatabaseManager(DBNames.USER_DB, usersFilepath, true);
	    
	    // add dictionary
	    String dictionaryDBpath = dbFolder + context.getInitParameter(Configuration.DICTIONARY_FILE_NAME_PARAMETER);
	    databaseProvider.addDatabaseManager(DBNames.DICTIONARY_DB, dictionaryDBpath, false);
	   
	    // add types db
	    String typesDBpath = dbFolder + context.getInitParameter(Configuration.TYPES_DB_NAME_PARAMETER);
	    databaseProvider.addDatabaseManager(DBNames.TYPES_DB, typesDBpath, false);
	   
	}

	public void setDatabaseProvider(DatabaseProvider databaseProvider) {
		this.databaseProvider = databaseProvider;
	}

	public DatabaseProvider getDatabaseProvider() {
		return databaseProvider;
	}
	
	
}
