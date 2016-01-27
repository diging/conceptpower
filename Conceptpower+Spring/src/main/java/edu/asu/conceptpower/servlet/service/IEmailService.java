package edu.asu.conceptpower.servlet.service;

public interface IEmailService {

	/**
	 * This method will send compose and send the message
	 * */
	public abstract void sendMail(String to, String subject, String body);

}