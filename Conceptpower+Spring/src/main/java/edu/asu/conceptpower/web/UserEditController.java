package edu.asu.conceptpower.web;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.conceptpower.app.users.IUserManager;
import edu.asu.conceptpower.app.validation.EmailValidator;
import edu.asu.conceptpower.users.User;
import edu.asu.conceptpower.web.backing.UserBacking;

/**
 * This class provides required methods for editing a user
 * 
 * @author Chetan and Julia Damerow
 * 
 */
@Controller
public class UserEditController {

    @Autowired
    private IUserManager userManager;

    @Autowired
    private EmailValidator eValidator;

    @InitBinder
    private void initBinder(WebDataBinder binder) {
        binder.setValidator(eValidator);
    }

    /**
     * This method provides user information to user editing page
     * 
     * @param model
     *            Generic model holder for servlet
     * @param id
     *            Represents id of a use to be edited
     * @return Returns a string value to redirect user to edit user page
     */
    @RequestMapping(value = "auth/user/edituser/{id:.+}")
    public String prepareEditUser(ModelMap model, @PathVariable String id) {

        User user = userManager.findUser(id);

        if (user == null)
            return "auth/user/notfound";

        UserBacking userBacking = new UserBacking(user.getUsername(), user.getFullname());
        userBacking.setIsAdmin(user.getIsAdmin());
        userBacking.setEmail(user.getEmail());

        if (!model.containsAttribute("user")) {
            model.addAttribute("user", userBacking);
        }
        return "layouts/user/edituser";
    }

    /**
     * This method stores updated user changes
     * 
     * @param req
     *            holds HTTP request information
     * @param user
     *            holds the user entered values in the backing bean
     * @param reattr
     *            variable to bind error messages on redirect
     * @param result
     *            variable to bind the error values to the page
     * 
     * @return Returns a string value to redirect user to user list page
     */
    @RequestMapping(value = "auth/user/edituser/store", method = RequestMethod.POST)
    public String storeUserChanges(ModelMap model, @ModelAttribute("user") @Validated UserBacking user,
            BindingResult result, Principal principal) {

        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            model.addAttribute("org.springframework.validation.BindingResult.user", result);
            model.addAttribute("user", user);
            return "layouts/user/edituser";
        }

        User uUser = userManager.findUser(user.getUsername());

        if (uUser == null)
            return "auth/user/notfound";

        uUser.setIsAdmin(user.getIsAdmin());
        uUser.setFullname(user.getFullname());
        uUser.setEmail(user.getEmail());

        userManager.storeModifiedUser(uUser);
        return "redirect:/auth/user/list";
    }

    /**
     * This method returns the control to user list when user cancels user edit
     * or password edit operation
     * 
     * @return Returns a string value to redirect user to user list page
     */
    @RequestMapping(value = "auth/user/canceledit", method = RequestMethod.GET)
    public String cancelEdit() {
        return "redirect:/auth/user/list";
    }
}