package com.yuri.spring.angular.mongo.ws.service.email;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;

public class SmtpEmailService extends AbstractEmailService{

	@Autowired
	JavaMailSender javaMailSender;
	
	private static final Logger LOG = LoggerFactory.getLogger(SmtpEmailService.class);
	
	@Override
	public void senderHtmlEmail(MimeMessage message) {
		LOG.info("Enviando email");
		javaMailSender.send(message);
		LOG.info("Email enviado com sucesso");
	}

}
