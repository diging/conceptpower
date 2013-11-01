package edu.asu.conceptpower.web;

import java.io.Serializable;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import edu.asu.conceptpower.wordnet.WordNetConfiguration;

@ManagedBean
@ApplicationScoped
public class WordNetConfController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8012115558834296200L;
	
	private WordNetConfiguration configuration;
	
	public WordNetConfController() {
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext(); 
		String wordnetPath = context.getInitParameter(Configuration.WORDNET_PATH_PARAMETER);
		
		configuration = new WordNetConfiguration();
		configuration.setWordnetPath(wordnetPath);
		configuration.setDictFolder("dict");
	}
	
	public WordNetConfiguration getWordNetConfiguration() {
		return configuration;
	}
}
