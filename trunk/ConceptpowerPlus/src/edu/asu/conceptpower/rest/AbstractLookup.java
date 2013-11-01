package edu.asu.conceptpower.rest;

import javax.faces.FactoryFinder;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AbstractLookup {

	protected FacesContext getFacesContext(HttpServletRequest request, HttpServletResponse response) {
	    FacesContext facesContext = FacesContext.getCurrentInstance();
	    if (facesContext == null) {
	
	        FacesContextFactory contextFactory  = (FacesContextFactory)FactoryFinder.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
	        LifecycleFactory lifecycleFactory = (LifecycleFactory)FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY); 
	        Lifecycle lifecycle = lifecycleFactory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
	
	        facesContext = contextFactory.getFacesContext(request.getSession().getServletContext(), request, response, lifecycle);
	
	        // Set using our inner class
	       // InnerFacesContext.setFacesContextAsCurrentInstance(facesContext);
	
	        // set a new viewRoot, otherwise context.getViewRoot returns null
	        UIViewRoot view = facesContext.getApplication().getViewHandler().createView(facesContext, "");
	        facesContext.setViewRoot(view);                
	    }
	    return facesContext;
	}

}