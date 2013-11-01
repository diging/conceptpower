package edu.asu.conceptpower.db4o;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class DatabaseProvider implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9033862601599780669L;
	
	private Map<String, DatabaseManager> managers;
	
	public DatabaseProvider() {
		managers = new HashMap<String, DatabaseManager>();
	}
	
	public void addDatabaseManager(String id, String filepath, boolean encrypt) {
		DatabaseManager manager = new DatabaseManager(encrypt);
		manager.init(filepath);
		managers.put(id, manager);
	}
	
	public void shutdown() {
		for (DatabaseManager m : managers.values())
			m.shutdown();
	}
	
	public DatabaseManager getDatabaseManager(String id) {
		return managers.get(id);
	}
}
