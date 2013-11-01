package edu.asu.conceptpower.web.util;
import javax.faces.context.FacesContext;

/**
 * 
 * @author Bauke Scholtz
 *
 */
public class FacesUtil {

	// Getters -----------------------------------------------------------------------------------

    public static Object getSessionMapValue(String key) {
        return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(key);
    }

    // Setters -----------------------------------------------------------------------------------

    public static void setSessionMapValue(String key, Object value) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(key, value);
    }

}