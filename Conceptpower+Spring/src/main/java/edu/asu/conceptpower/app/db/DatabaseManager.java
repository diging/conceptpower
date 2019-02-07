package edu.asu.conceptpower.app.db;

import java.io.Serializable;

import com.db4o.ObjectContainer;
import com.db4o.ObjectServer;
import com.db4o.ObjectSet;
import com.db4o.cs.Db4oClientServer;
import com.db4o.cs.config.ServerConfiguration;

import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ReviewRequest;

public class DatabaseManager implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -3325272288078647257L;
    private ObjectServer server;
    private String databasePath;

    public void init() {
        close();
        ServerConfiguration configuration = Db4oClientServer.newServerConfiguration();
        configuration.file().blockSize(80);
        configuration.common().objectClass(ConceptEntry.class).objectField("wordnetId").indexed(true);
        configuration.common().objectClass(ConceptEntry.class).objectField("id").indexed(true);
        // Added to make sure list has been added to the database
        // Ref: http://www.resolvinghere.com/sof/12343387.shtml
        configuration.common().objectClass(ConceptEntry.class).updateDepth(2);
        server = Db4oClientServer.openServer(configuration, databasePath, 0);

    }

    public ObjectContainer getClient() {
        ObjectContainer container = server.openClient();
        return container;
    }

    public String getDatabasePath() {
        return databasePath;
    }

    public void setDatabasePath(String databasePath) {
        this.databasePath = databasePath;
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
    
  /*  public ReviewRequest getEntry(String id,Boolean reviewFlag) {
        ReviewRequest exampleEntry = new ReviewRequest();
        exampleEntry.setWordNetId(id);
        exampleEntry.setReviewFlag(reviewFlag);
        ObjectSet<ReviewRequest> results = getClient().queryByExample(exampleEntry);

        if (results.size()>0)
            return results.get(results.size()-1);

        return null;
    }*/
    
public ReviewRequest getEntry(String wordId,String wordNetId,Boolean reviewFlag) {
        
        ReviewRequest exampleEntry = new ReviewRequest();
        if(wordNetId!= null && wordNetId.length()!=0) {
            exampleEntry.setWordNetId(wordNetId);
        }
        else {
            exampleEntry.setWordId(wordId);
        }
        exampleEntry.setReviewFlag(reviewFlag);
        
        ObjectSet<ReviewRequest> results = getClient().queryByExample(exampleEntry);

        if (results.size()>0)
            return results.get(results.size()-1);

        return null;
    }
    
}
