package edu.asu.conceptpower.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@ManagedBean(name="userSuperAdminController")
@ApplicationScoped
public class UserSuperAdminController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1289716729536274489L;
	private Map<String, String> admins;
	
	public UserSuperAdminController() {
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext(); 
		
		admins = new HashMap<String, String>();
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
					admins.put(name.getNodeValue(), password.getNodeValue());
				}
			} catch (ParserConfigurationException e) {
				context.log("Couldn't read XML file.", e);
			} catch (SAXException e) {
				context.log("Couldn't read XML file.", e);
			} catch (IOException e) {
				context.log("Couldn't read XML file.", e);
			}
	    }
	}

	public void setAdmins(Map<String, String> admins) {
		this.admins = admins;
	}

	public Map<String, String> getAdmins() {
		return admins;
	}
	
	
}
