package com.chemaxon.compliancechecker.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import com.chemaxon.compliancechecker.service.NotificationService;

@Service
public class EmailNotificationService implements NotificationService {

	private String to;
	private String cc;
	private String subject;
	private String text;
	
	private MailSender mailSender;
	
	@Autowired
	public EmailNotificationService(MailSender mailSender) {
		this.mailSender = mailSender;
	}

	@Override
	public void sendNotification() {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to.split(","));
		message.setCc(cc != null ? cc.split(",") : null);
		message.setSubject(subject);
		message.setText(text);
		mailSender.send(message);
	}

	@Value("${newdb.mail.notification.to}")
	public void setTo(String to) {
		this.to = to;
	}

	@Value("${newdb.mail.notification.cc:#{null}}")
	public void setCc(String cc) {
		this.cc = cc;
	}

	@Value("${newdb.mail.notification.subject}")
	public void setSubject(String subject) {
		this.subject = subject;
	}

	@Value("${newdb.mail.notification.text}")
	public void setText(String text) {
		this.text = text;
	}
}
