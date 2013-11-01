package edu.asu.conceptpower.web.validators;

import javax.el.ELContext;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import edu.asu.conceptpower.db4o.DBNames;
import edu.asu.conceptpower.db4o.DatabaseManager;
import edu.asu.conceptpower.users.User;
import edu.asu.conceptpower.users.UsersManager;
import edu.asu.conceptpower.web.DatabaseController;

public class UniqueUsernameValidator implements Validator {

	public void validate(FacesContext arg0, UIComponent arg1, Object arg2)
			throws ValidatorException {
		if (arg2 == null || arg2.toString().isEmpty()) {
			FacesMessage msg = new FacesMessage("User creation failed",
					"No username given.");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(msg);
		}

		DatabaseManager manager = getDatabaseController().getDatabaseProvider()
				.getDatabaseManager(DBNames.USER_DB);

		UsersManager usersManager = new UsersManager(manager);
		User found = usersManager.findUser(arg2.toString());

		if (found != null) {
			FacesMessage msg = new FacesMessage("User creation failed",
					"Username already in use.");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(msg);
		}
	}

	protected DatabaseController getDatabaseController() {
		ELContext context = FacesContext.getCurrentInstance().getELContext();

		DatabaseController provider = (DatabaseController) context
				.getELResolver().getValue(context, null, "databaseController");
		return provider;
	}

}
