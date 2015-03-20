package edu.asu.conceptpower.web;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.conceptpower.service.IEmailService;
import edu.asu.conceptpower.users.IUserManager;
import edu.asu.conceptpower.users.Token;
import edu.asu.conceptpower.web.backing.EmailBackBean;

@Controller
public class ForgottenPasswordController {
	
	@Autowired
	private IUserManager userManager;
	
	@Autowired
	private IEmailService emailService;
	
	@Value("#{uiMessages['email.forgot.subject']}")
	private String subject;
	
	@Value("#{uiMessages['email.forgot.body']}")
	private String body;
	
	@Autowired
	@Qualifier("uiMessages")
	private Properties uiMessages;

	@RequestMapping(value = "/forgot", method = RequestMethod.GET)
	public String preparePage(Model model) {
		model.addAttribute("emailBackBean", new EmailBackBean());
		return "forgot";
	}
	
	@RequestMapping(value = "/emailSent", method = RequestMethod.POST)
	public String resetPassword(Model model, EmailBackBean emailBean) {
		
		System.out.println(uiMessages.getProperty("email.forgot.subject"));
		
		Token token = userManager.createToken();
		emailService.sendMail(emailBean.getEmail(), subject, body.replace("${link}", token.getToken()));
		
		
		model.addAttribute("email", subject + " " + emailBean.getEmail());
		return "emailSent";
	}
}
