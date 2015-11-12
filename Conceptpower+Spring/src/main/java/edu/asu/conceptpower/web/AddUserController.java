package edu.asu.conceptpower.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.asu.conceptpower.users.IUserManager;
import edu.asu.conceptpower.users.User;
import edu.asu.conceptpower.validation.UserValidator;

/**
 * This class provides methods for creating a user
 * 
 * @author Chetan and Julia Damerow
 * 
 */
@Controller
public class AddUserController {

    @Autowired
    private IUserManager usersManager;
    @Autowired
    private UserValidator uValidator;

    @InitBinder
    private void initBinder(WebDataBinder binder) {
        binder.setValidator(uValidator);
    }

    /**
     * This method creates a new user for given user information
     * 
     * @param req
     *            Holds HTTP request information
     * @param model
     *            Holds generic information about the model
     * @param user
     *            Holds the User bean values retrieved from the add page
     * @param result
     *            Binding Result Object for binding errors if any
     * @return String to redirect user to user list page
     * 
     */
    @RequestMapping(value = "auth/user/createuser")
    public String createUser(HttpServletRequest req, ModelMap model, @ModelAttribute("user") @Validated User user,
            BindingResult result) {
        if (result.hasErrors()) {
            return "auth/user/add";
        }

        usersManager.addUser(user);
        return "redirect:/auth/user/list";
    }

    /**
     * This method provides string to redirect user to add user page
     * 
     * @param model
     *            Holds generic information about the model
     * 
     * 
     * @return String to redirect user to add user page
     */
    @RequestMapping(value = "auth/user/add")
    public String prepareUserPage(ModelMap model) {
        model.addAttribute("user", new User());
        return "auth/user/add";
    }

}
