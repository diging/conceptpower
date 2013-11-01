package edu.asu.conceptpower.rest;

import java.io.IOException;

import javax.el.ELContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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

@Path("/SynonymSearch")
public class SynonymSearch extends AbstractLookup {

	@javax.ws.rs.core.Context 
	ServletContext context;

	@GET
	@Produces(MediaType.TEXT_XML)
	public String getSynonymsForId(@QueryParam("id") String id, @Context HttpServletRequest request, @Context HttpServletResponse response) {
//		DatabaseProvider provider = (DatabaseProvider) context.getAttribute(Parameter.DB_PROVIDER_CONTEXT_PARAMETER);
//		DatabaseManager manager = provider.getDatabaseManager(DBNames.WORDNET_CACHE);
//		WordNetConfiguration wnConfig = (WordNetConfiguration) context.getAttribute(Parameter.WORDNET_CONFIGURATION_CONTEXT_PARAMETER);
		
		FacesContext facesContext = getFacesContext(request, response);
		ELContext context = facesContext.getELContext(); 
		
		DatabaseController provider = (DatabaseController) context.getELResolver().getValue(context, null, "databaseController");
		WordNetConfController configurationController = (WordNetConfController) context.getELResolver().getValue(context, null, "wordNetConfController");
		
		DatabaseManager manager = provider.getDatabaseProvider()
				.getDatabaseManager(DBNames.WORDNET_CACHE);
		
		
		// construct the URL to the Wordnet dictionary directory
	    
		String[] pathParts = id.split("/");
		int lastIndex = pathParts.length - 1;
		String wordnetId = null;
		if (lastIndex > -1)
			wordnetId = pathParts[lastIndex];
		
		
		if (wordnetId == null) {
			return "no word net id";
		}
	 
		ConceptManager dictManager;
		try {
			dictManager = new ConceptManager(manager,
					provider.getDatabaseProvider().getDatabaseManager(DBNames.DICTIONARY_DB),
					configurationController.getWordNetConfiguration());
		} catch (IOException e) {
			return null;
		}
		
		ConceptTypesManager typeManager = null;
		try {
			typeManager = new ConceptTypesManager(provider.getDatabaseProvider().getDatabaseManager(DBNames.TYPES_DB).getClient());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//context.log("Finding entry for " + wordnetId);
	    ConceptEntry[] synonyms = dictManager.getSynonymsForConcept(wordnetId);
	   
	    XMLConceptMessage msg = new XMLConceptMessage();

		for (ConceptEntry entry : synonyms) {
			ConceptType type = null;
			if (typeManager != null && entry.getTypeId() != null
					&& !entry.getTypeId().trim().isEmpty()) {
				type = typeManager.getType(entry.getTypeId());
			}
			msg.appendEntry(entry, type);
		}

		dictManager.close();
		typeManager.close();

		return msg.getXML();
	}
}
