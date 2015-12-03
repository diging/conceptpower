package edu.asu.conceptpower.web;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.conceptpower.users.IUserManager;
import edu.asu.conceptpower.users.User;
import edu.asu.conceptpower.web.backing.UserBacking;

@Controller
public class UserDeleteController {

    @Autowired
    private IUserManager userManager;

    /**
     * @param model
     * @param id
     * @return
     */
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

    /**
     * @param user
     * @param principal
     * @return
     */
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
