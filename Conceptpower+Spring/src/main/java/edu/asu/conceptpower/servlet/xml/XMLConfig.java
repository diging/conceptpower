package edu.asu.conceptpower.servlet.xml;

public class XMLConfig {

	private String uriPrefix;
	private String xmlNamespace;
	
	public String getXmlNamespace() {
		return xmlNamespace;
	}
	public void setXmlNamespace(String xmlNamespace) {
		this.xmlNamespace = xmlNamespace;
	}
	public String getUriPrefix() {
		return uriPrefix;
	}
	public void setUriPrefix(String uriPrefix) {
		this.uriPrefix = uriPrefix;
	}
}
