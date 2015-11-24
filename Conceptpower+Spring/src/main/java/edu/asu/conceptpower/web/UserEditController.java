package edu.asu.conceptpower.web;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.asu.conceptpower.users.IUserManager;
import edu.asu.conceptpower.users.User;
import edu.asu.conceptpower.validation.UserValidator;
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
        return "auth/user/edituser";
    }

    /**
     * This method stores updated user changes
     * 
     * @param req
     *            holds HTTP request information
     * @return Returns a string value to redirect user to user list page
     */
    @RequestMapping(value = "auth/user/edituser/store", method = RequestMethod.POST)
    public String storeUserChanges(@ModelAttribute("user") @Valid UserBacking user, BindingResult result,
            RedirectAttributes reattr, Principal principal) {

        if (result.hasErrors()) {
            reattr.addFlashAttribute("org.springframework.validation.BindingResult.user", result);
            reattr.addFlashAttribute("user", user);
            return "redirect:/auth/user/edituser/" + user.getUsername();
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
     * 
     *           
     * @return Returns a string value to redirect user to user list page
     */
    @RequestMapping(value = "auth/user/editpassword/store", method = RequestMethod.POST)
    public String storePasswordChanges(HttpServletRequest req, Principal principal, @Valid UserBacking user,
            BindingResult result, RedirectAttributes reattr) {

        User uUser = userManager.findUser(user.getUsername());

        if (uUser == null)
            return "auth/user/notfound";

        if (result.hasErrors()) {

            reattr.addFlashAttribute("org.springframework.validation.BindingResult.user", result);
            reattr.addFlashAttribute("user", user);
            return "redirect:/auth/user/editpassword/" + user.getUsername();
        }

        uUser.setPw(user.getPassword());

        userManager.storeModifiedPassword(uUser);
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

    @RequestMapping(value = "auth/user/deleteuser/{id:.+}")
    public String prepareDeleteUser(ModelMap model, @PathVariable String id) {
        User user = userManager.findUser(id);

        if (user == null)
            return "auth/user/notfound";

        UserBacking userBacking = new UserBacking(user.getUsername(), user.getFullname());
        userBacking.setIsAdmin(user.getIsAdmin());

        model.addAttribute("user", userBacking);
        return "auth/user/deleteuser";
    }

    @RequestMapping(value = "auth/user/confirmdeleteuser/", method = RequestMethod.POST)
    public String confirmDeleteUser(UserBacking user, Principal principal) {

        User uUser = userManager.findUser(user.getUsername());

        if (uUser == null)
            return "auth/user/notfound";

        userManager.deleteUser(uUser.getUsername());
        return "redirect:/auth/user/list";
    }

    @RequestMapping(value = "auth/user/canceldelete", method = RequestMethod.GET)
    public String cancelDelete() {
        return "redirect:/auth/user/list";
    }
}

