package edu.asu.conceptpower.rest;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.asu.conceptpower.core.ConceptType;
import edu.asu.conceptpower.db4o.TypeDatabaseClient;
import edu.asu.conceptpower.xml.XMLConstants;
import edu.asu.conceptpower.xml.XMLTypeMessage;

@Controller
public class TypeIdLookup {

	@Autowired
	private TypeDatabaseClient typeManager;

	@Autowired
	XMLTypeMessage msg;

	@RequestMapping(value = "rest/Type", method = RequestMethod.GET, produces = "application/xml")
	public @ResponseBody
	String getTypeById(HttpServletRequest req, ModelMap model) {

		// get type id if URI is given
		String id = req.getParameter("id");
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

		ConceptType type = typeManager.getType(typeId);

		if (type != null) {
			ConceptType superType = null;
			if (type.getSupertypeId() != null
					&& !type.getSupertypeId().trim().isEmpty())
				superType = typeManager.getType(type.getSupertypeId().trim());
			msg.appendEntry(type, superType);
		}

		return msg.getXML();
	}
}
