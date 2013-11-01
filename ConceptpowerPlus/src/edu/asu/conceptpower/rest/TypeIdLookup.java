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

import edu.asu.conceptpower.core.ConceptType;
import edu.asu.conceptpower.core.ConceptTypesManager;
import edu.asu.conceptpower.db4o.DBNames;
import edu.asu.conceptpower.web.DatabaseController;
import edu.asu.conceptpower.xml.XMLConstants;
import edu.asu.conceptpower.xml.XMLTypeMessage;

@Path("/Type")
public class TypeIdLookup extends AbstractLookup {

	@javax.ws.rs.core.Context
	ServletContext context;

	@GET
	@Produces(MediaType.TEXT_XML)
	public String getTypeById(@QueryParam("id") String id,
			@Context HttpServletRequest request,
			@Context HttpServletResponse response) {

		FacesContext facesContext = getFacesContext(request, response);
		ELContext context = facesContext.getELContext();

		DatabaseController provider = (DatabaseController) context
				.getELResolver().getValue(context, null, "databaseController");
		
		// get type id if URI is given

		String[] pathParts = id.split("/");
		int lastIndex = pathParts.length - 1;
		String typeId = null;
		if (lastIndex > -1)
			typeId = pathParts[lastIndex];

		if (typeId == null) {
			return "no type id";
		}
		
		if (typeId.startsWith(XMLConstants.TYPE_PREFIX)) {
			typeId = typeId.substring(XMLConstants.TYPE_PREFIX.length());
		}
		
		ConceptTypesManager typeManager = null;
		try {
			typeManager = new ConceptTypesManager(provider.getDatabaseProvider().getDatabaseManager(DBNames.TYPES_DB).getClient());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ConceptType type = typeManager.getType(typeId);
		
		
		XMLTypeMessage msg = new XMLTypeMessage();
		if (type != null) {
			ConceptType superType = null;
			if (type.getSupertypeId() != null && !type.getSupertypeId().trim().isEmpty())
				superType = typeManager.getType(type.getSupertypeId().trim());
			msg.appendEntry(type, superType);
		}
		
		typeManager.close();
		return msg.getXML();
	}
}
