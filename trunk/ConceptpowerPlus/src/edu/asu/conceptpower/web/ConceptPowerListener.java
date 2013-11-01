package edu.asu.conceptpower.web;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.asu.conceptpower.db4o.DBNames;
import edu.asu.conceptpower.db4o.DatabaseProvider;
import edu.asu.conceptpower.wordnet.WordNetConfiguration;


/**
 * Application Lifecycle Listener implementation class ConceptPowerListener
 *
 */
public class ConceptPowerListener implements ServletContextListener {

	private DatabaseProvider provider;
	
    
	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0) {
    	ExternalContext context = FacesContext.getCurrentInstance().getExternalContext(); 
		String dbFolder = context.getInitParameter(Configuration.DATABASE_PATH_PARAMETER).endsWith(File.separator) ? context.getInitParameter(Configuration.DATABASE_PATH_PARAMETER) : context.getInitParameter(Configuration.DATABASE_PATH_PARAMETER) + File.separator;
	    
		String wordnetCachePath = dbFolder +
	    	context.getInitParameter(Configuration.WORDNET_CACHE_NAME_PARAMETER);  
	    
	    
	    
	    // set wordnetCache manager
	    provider = new DatabaseProvider();
	    provider.addDatabaseManager(DBNames.WORDNET_CACHE, wordnetCachePath, false);
	    
	    // add user database
	    String usersFilepath = dbFolder +
    		context.getInitParameter(Configuration.USER_DATABASE_NAME_PARAMETER);  
	    provider.addDatabaseManager(DBNames.USER_DB, usersFilepath, true);
	    
	    // add dictionary
	    String dictionaryDBpath = dbFolder + context.getInitParameter(Configuration.DICTIONARY_FILE_NAME_PARAMETER);
	    provider.addDatabaseManager(DBNames.DICTIONARY_DB, dictionaryDBpath, false);
	    
	    Map<String, String> userMap = new HashMap<String, String>();
	    // add admin user
	    String filename = Configuration.USER_FILE;
	    InputStream inp = context.getResourceAsStream(filename);
	    if (inp != null) {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = null;
			Document doc = null;
			try {
				db = dbf.newDocumentBuilder();
				doc = db.parse(inp);
				NodeList nodeLst = doc.getElementsByTagName("user");
				for (int s = 0; s < nodeLst.getLength(); s++) {
					Node user = nodeLst.item(s);
					NamedNodeMap map = user.getAttributes();
					Node name = map.getNamedItem("name");
					Node password = map.getNamedItem("password");
					userMap.put(name.getNodeValue(), password.getNodeValue());
				}
			} catch (ParserConfigurationException e) {
				context.log("Couldn't read XML file.", e);
			} catch (SAXException e) {
				context.log("Couldn't read XML file.", e);
			} catch (IOException e) {
				context.log("Couldn't read XML file.", e);
			}
	    }
	    context.getApplicationMap().put(Parameter.USER_MAP, userMap);
	    
	    // add to context
	    context.getApplicationMap().put(Parameter.DB_PROVIDER_CONTEXT_PARAMETER, provider);
	    context.log("Db4o startup on " + dbFolder);   
	     
	    // set wordnet configuration
	    String wordnetPath = context.getInitParameter(Configuration.WORDNET_PATH_PARAMETER);
	    WordNetConfiguration wordnetConfig = new WordNetConfiguration();
	    wordnetConfig.setWordnetPath(wordnetPath);
	    wordnetConfig.setDictFolder("dict");
	    context.getApplicationMap().put(Parameter.WORDNET_CONFIGURATION_CONTEXT_PARAMETER, wordnetConfig);
	    context.log("WordNet configutation set: " + wordnetPath);
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0) {
    	ServletContext context = arg0.getServletContext();   
	    context.removeAttribute(Parameter.DB_PROVIDER_CONTEXT_PARAMETER);   
	    provider.shutdown(); 
	    context.log("Db4o shutdown.");  
    }
	
}
