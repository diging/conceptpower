package edu.asu.conceptpower.web;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.conceptpower.service.IEmailService;
import edu.asu.conceptpower.users.IUserManager;
import edu.asu.conceptpower.users.Token;
import edu.asu.conceptpower.users.User;
import edu.asu.conceptpower.web.backing.EmailBackBean;
import edu.asu.conceptpower.web.backing.UserBacking;

/**
 * 
 * @author jdamerow
 *
 */
@Controller
public class ForgottenPasswordController {

    @Autowired
    private IUserManager userManager;

    @Autowired
    private IEmailService emailService;

    @Value("#{messages['email.forgot.subject']}")
    private String subject;

    @Value("#{messages['email.forgot.body']}")
    private String body;

    @Value("#{config['password.recovery.expiration.hours']}")
    private int expirationHours;

    @RequestMapping(value = "/forgot", method = RequestMethod.GET)
    public String preparePage(Model model) {
        model.addAttribute("emailBackBean", new EmailBackBean());
        return "forgot";
    }

    @RequestMapping(value = "/emailSent", method = RequestMethod.POST)
    public String resetPassword(Model model, @Valid EmailBackBean emailBean, BindingResult result,
            HttpServletRequest request) {

        if (result.hasErrors()) {
            model.addAttribute("emailBackBean", emailBean);
            return "forgot";
        }

        String email = emailBean.getEmail();
        User user = userManager.findUserByEmail(email);

        if (user != null) {
            Token token = userManager.createToken(user);
            StringBuffer requestURL = request.getRequestURL();
            String url = requestURL.substring(0, requestURL.length() - request.getServletPath().length());
            emailService.sendMail(email, subject, body.replace("${name}", user.getFullName()).replace("${link}",
                    url + "/beginReset/" + token.getToken()));
        }

        model.addAttribute("email", email);
        return "emailSent";
    }

    @RequestMapping(value = "/beginReset/{token}")
    public String resetWithToken(Model model, @PathVariable("token") String token) {
        EmailBackBean emailBean = new EmailBackBean();
        emailBean.setToken(token);
        model.addAttribute("emailBackBean", emailBean);

        return "beginReset";
    }

    @RequestMapping(value = "/reset")
    public String doReset(Model model, @Valid EmailBackBean emailBean, BindingResult result) {
        if (result.hasErrors()) {
            model.addAttribute("emailBackBean", emailBean);
            return "beginReset";
        }

        Token token = userManager.findToken(emailBean.getToken());

        if (token == null) {
            model.addAttribute("errormsg", "Sorry, but Conceptpower is unable to verify the user.");
            return "resetError";
        }
        if (!token.getUser().getEmail().equals(emailBean.getEmail().trim())) {
            model.addAttribute("errormsg", "Sorry, but Conceptpower is unable to verify the user.");
            return "resetError";
        }

        DateTime creationDate = new DateTime(token.getCreationDate());
        DateTime currentDate = new DateTime();
        if (!currentDate.isAfter(creationDate) || !creationDate.plusHours(expirationHours).isAfter(currentDate)) {
            userManager.deleteToken(token.getToken());
            model.addAttribute("errormsg", "Sorry, but your reset link has expired.");
            return "resetError";
        }

        UserBacking userBacking = new UserBacking(token.getUser().getUsername(), token.getUser().getFullName());
        userBacking.setToken(token.getToken());
        model.addAttribute("userBacking", userBacking);
        return "reset";
    }

    @RequestMapping(value = "/resetComplete")
    public String resetComplete(Model model, @Valid UserBacking user, BindingResult result) {

        if (result.hasErrors()) {
            user.setPassword("");
            user.setRetypedPassword("");
            model.addAttribute("userBacking", user);
            return "reset";
        }

        User uUser = userManager.findUser(user.getUsername());

        if (uUser == null) {
            model.addAttribute("errormsg", "Sorry, we could not complete the reset process.");
            return "resetError";
        }

        uUser.setPw(user.getPassword());

        userManager.deleteToken(user.getToken());
        userManager.storeModifiedPassword(uUser);
        return "resetComplete";
    }
}
