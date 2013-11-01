package edu.asu.conceptpower.rest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.el.ELContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptManager;
import edu.asu.conceptpower.core.ConceptType;
import edu.asu.conceptpower.core.ConceptTypesManager;
import edu.asu.conceptpower.db4o.DBNames;
import edu.asu.conceptpower.db4o.DatabaseManager;
import edu.asu.conceptpower.web.DatabaseController;
import edu.asu.conceptpower.web.WordNetConfController;
import edu.asu.conceptpower.xml.XMLConceptMessage;

@Path("/ConceptLookup/{word}/{pos}")
public class ConceptLookup extends AbstractLookup {

	@javax.ws.rs.core.Context 
	ServletContext context;
	
	

	@GET
	@Produces(MediaType.TEXT_XML)
	public String getWordNetEntry(@PathParam("word") String word, @PathParam("pos") String pos, @Context HttpServletRequest request, @Context HttpServletResponse response) {
//		DatabaseProvider provider = (DatabaseProvider) context.getAttribute(Parameter.DB_PROVIDER_CONTEXT_PARAMETER);
//		DatabaseManager manager = provider.getDatabaseManager(DBNames.WORDNET_CACHE);
//		WordNetConfiguration wnConfig = (WordNetConfiguration) context.getAttribute(Parameter.WORDNET_CONFIGURATION_CONTEXT_PARAMETER);
		
		FacesContext facesContext = getFacesContext(request, response);
		ELContext context = facesContext.getELContext(); 
		
		DatabaseController provider = (DatabaseController) context.getELResolver().getValue(context, null, "databaseController");
		WordNetConfController configurationController = (WordNetConfController) context.getELResolver().getValue(context, null, "wordNetConfController");
		
		DatabaseManager manager = provider.getDatabaseProvider()
				.getDatabaseManager(DBNames.WORDNET_CACHE);
		
		ConceptManager dictManager;
		try {
			dictManager = new ConceptManager(manager,
					provider.getDatabaseProvider().getDatabaseManager(DBNames.DICTIONARY_DB),
					configurationController.getWordNetConfiguration());
		} catch (IOException e) {
//			log("While initialzing DictionaryManager:",
//					e);
			return null;
		}
		
		ConceptTypesManager typeManager = null;
		try {
			typeManager = new ConceptTypesManager(provider.getDatabaseProvider().getDatabaseManager(DBNames.TYPES_DB).getClient());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		ConceptManager dictManager;
//		try {
//			dictManager = new ConceptManager(manager, provider.getDatabaseManager(DBNames.DICTIONARY_DB), wnConfig);
//		} catch (IOException e) {
//			context.log("While initialzing DictionaryManager:", e);
//			return e.getMessage();
//		}
	    
	    ConceptEntry[] entries = dictManager.getConceptListEntriesForWord(word, pos);
	    Map<ConceptEntry, ConceptType> entryMap = new HashMap<ConceptEntry, ConceptType>();
	    
	    for (ConceptEntry entry : entries) {
	    	ConceptType type = null;
		    if (typeManager != null && entry.getTypeId() != null && !entry.getTypeId().trim().isEmpty()) {
		    	type = typeManager.getType(entry.getTypeId());
		    }
		    entryMap.put(entry, type);
	    }
	   
	    XMLConceptMessage returnMsg = new XMLConceptMessage();
	    if (entries != null) {
	    	returnMsg.appendEntries(entryMap);
	    }
	   
	    typeManager.close();
	    dictManager.close();
	    return returnMsg.getXML();
	}
}
