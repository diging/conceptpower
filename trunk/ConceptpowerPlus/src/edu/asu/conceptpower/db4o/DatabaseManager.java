package edu.asu.conceptpower.db4o;

import java.io.Serializable;

import com.db4o.ObjectContainer;
import com.db4o.ObjectServer;
import com.db4o.cs.Db4oClientServer;
import com.db4o.cs.config.ServerConfiguration;


public class DatabaseManager implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3325272288078647257L;
	private ObjectServer server;
	private boolean encrypt;

	public DatabaseManager(boolean encrypt) {
		this.encrypt = encrypt;
	}
	
	public void init(String path) {
		close();
		ServerConfiguration configuration = Db4oClientServer.newServerConfiguration();
		configuration.file().blockSize(80);
		server = Db4oClientServer.openServer(configuration, path, 0);  
	}
	
	public ObjectContainer getClient() {
		ObjectContainer container = server.openClient();
		return container;
	}
	
	private void close() {   
		if (server != null) {   
			server.close();   
	    }   
	    server = null;   
	}
	
	public void shutdown() {
		close();
	}
}

