package edu.asu.conceptpower.web;

import javax.el.ELContext;
import javax.faces.context.FacesContext;

public class DatabaseBean {

	protected DatabaseController getDatabaseController() {
		ELContext context = FacesContext.getCurrentInstance().getELContext(); 
		
		return (DatabaseController) context.getELResolver().getValue(context, null, "databaseController");
	}
	
}
