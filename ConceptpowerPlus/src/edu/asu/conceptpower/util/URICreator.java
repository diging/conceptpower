package edu.asu.conceptpower.util;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptType;
import edu.asu.conceptpower.xml.XMLConstants;

public class URICreator {

	public static String getURI(ConceptEntry entry) {

		ExternalContext context = FacesContext.getCurrentInstance()
				.getExternalContext();
		String uriPrefix = context.getInitParameter("URIPrefix");

		if (entry.getId() != null && !entry.getId().isEmpty())
			return uriPrefix + entry.getId();

		else
			return uriPrefix + entry.getWordnetId();
	}

	public static String getTypeURI(ConceptType type) {

		return XMLConstants.TYPE_NAMESPACE + XMLConstants.TYPE_PREFIX
				+ type.getTypeId();
	}
}
