package edu.asu.conceptpower.web;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.conceptpower.app.users.IUserManager;
import edu.asu.conceptpower.users.User;
import edu.asu.conceptpower.web.backing.UserBacking;

@Controller
public class PasswordEditController {

    @Autowired
    private IUserManager userManager;

    /**
     * This method provides user information to password edit page
     * 
     * @param model
     *            Generic model holder for servlet
     * @param id
     *            Represents user id
     * @return Returns a string value to redirect user to edit password page
     */
    @RequestMapping(value = "auth/user/editpassword/{id:.+}")
    public String prepareEditPassword(ModelMap model, @PathVariable String id) {
        User user = userManager.findUser(id);

        if (user == null)
            return "auth/user/notfound";

        model.addAttribute("username", user.getUsername());

        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new UserBacking());
        }
        return "auth/user/editpassword";
    }

    /**
     * This method stores the updated user password information
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
    @RequestMapping(value = "auth/user/editpassword/store", method = RequestMethod.POST)
    public String storePasswordChanges(ModelMap model, HttpServletRequest req, Principal principal,
            @Valid UserBacking user, BindingResult result) {

        User uUser = userManager.findUser(user.getUsername());

        if (uUser == null)
            return "auth/user/notfound";

        if (result.hasErrors()) {

            model.addAttribute("username", user.getUsername());
            model.addAttribute("org.springframework.validation.BindingResult.user", result);
            model.addAttribute("user", user);
            return "auth/user/editpassword";
        }

        uUser.setPw(user.getPassword());

        userManager.storeModifiedPassword(uUser);
        return "redirect:/auth/user/list";
    }

}
