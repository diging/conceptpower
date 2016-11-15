package edu.asu.conceptpower.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import edu.asu.conceptpower.app.service.impl.EmailService;

public class EmailServiceTest {

	@InjectMocks
    private EmailService emailService;

	@Mock
	private MailSender mailSender;

	@Mock
	private SimpleMailMessage preConfiguredMessage;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void sendMailTest() {
		emailService.sendMail("toAddress","subject","body");
		SimpleMailMessage message = new SimpleMailMessage(preConfiguredMessage);
		message.setTo("toAddress");
		message.setSubject("subject");
		message.setText("body");
		Mockito.verify(mailSender).send(message);
	}
	
	
}
