package com.yuri.spring.angular.mongo.ws.service.email;

import javax.mail.internet.MimeMessage;

import com.yuri.spring.angular.mongo.ws.domain.User;
import com.yuri.spring.angular.mongo.ws.domain.VerificationToken;

public interface EmailService {

	void senderHtmlEmail(MimeMessage message);
	
	void senderConfirmationHtmlEmail(User user, VerificationToken token,Boolean resetandoSenha);
}
