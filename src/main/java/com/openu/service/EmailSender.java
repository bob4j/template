package com.openu.service;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.openu.util.Constants;

@Service
public class EmailSender {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private static final String EMAIL_FROM = Constants.EMAIL_FROM;

	@Value("${email.username}")
	private String username;

	@Value("${email.password}")
	private String password;

	/**
	 * Sends email
	 *
	 * @param to
	 * @param subject
	 * @param text
	 *            - can contain HTML
	 */
	public void send(String to, String subject, String text) {
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "25");
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username, EMAIL_FROM));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			message.setSubject(subject);
			message.setContent(text, "text/html");
			Transport.send(message);
		} catch (Exception e) {
			logger.error("failed to send email", e);
		}
	}

}
