package edu.asu.conceptpower.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.el.ELContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptManager;
import edu.asu.conceptpower.core.ConceptType;
import edu.asu.conceptpower.core.ConceptTypesManager;
import edu.asu.conceptpower.db4o.DBNames;
import edu.asu.conceptpower.db4o.DatabaseManager;
import edu.asu.conceptpower.web.DatabaseController;
import edu.asu.conceptpower.web.WordNetConfController;
import edu.asu.conceptpower.xml.XMLConceptMessage;

@Path("/ConceptSearch")
public class ConceptSearch extends AbstractLookup {

	@javax.ws.rs.core.Context
	ServletContext context;

	@GET
	@Produces(MediaType.TEXT_XML)
	public String searchConcept(@Context HttpServletRequest request,
			@Context UriInfo uriInfo, @Context HttpServletResponse response) {
		MultivaluedMap<String, String> queryParams = uriInfo
				.getQueryParameters();

		FacesContext facesContext = getFacesContext(request, response);
		ELContext context = facesContext.getELContext();

		DatabaseController provider = (DatabaseController) context
				.getELResolver().getValue(context, null, "databaseController");
		WordNetConfController configurationController = (WordNetConfController) context
				.getELResolver().getValue(context, null,
						"wordNetConfController");

		DatabaseManager manager = provider.getDatabaseProvider()
				.getDatabaseManager(DBNames.WORDNET_CACHE);

		ConceptManager dictManager;
		try {
			dictManager = new ConceptManager(manager, provider
					.getDatabaseProvider().getDatabaseManager(
							DBNames.DICTIONARY_DB),
					configurationController.getWordNetConfiguration());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		Map<String, String> searchFields = new HashMap<String, String>();
		String operator = SearchParamters.OP_OR;
		for (String key : queryParams.keySet()) {
			if (key.trim().equals(SearchParamters.OPERATOR) && !queryParams.getFirst(key).trim().isEmpty())
				operator = queryParams.getFirst(key).trim();
			else {
				for (String value : queryParams.get(key))
					searchFields.put(key.trim(), value);
			}
		}
		
		ConceptEntry[] searchResults = null;
		
		if (operator.equals(SearchParamters.OP_AND))
			searchResults = dictManager.searchForConceptsConnectedByAnd(searchFields);
		else {
			searchResults = dictManager.searchForConceptsConnectedByOr(searchFields);
		}

		ConceptTypesManager typeManager = null;
		try {
			typeManager = new ConceptTypesManager(provider
					.getDatabaseProvider().getDatabaseManager(DBNames.TYPES_DB)
					.getClient());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		XMLConceptMessage msg = new XMLConceptMessage();

		for (ConceptEntry entry : searchResults) {
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
