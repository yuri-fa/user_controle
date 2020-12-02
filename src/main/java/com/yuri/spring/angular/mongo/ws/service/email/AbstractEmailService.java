package com.yuri.spring.angular.mongo.ws.service.email;

import java.util.Date;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.yuri.spring.angular.mongo.ws.domain.User;
import com.yuri.spring.angular.mongo.ws.domain.VerificationToken;
import com.yuri.spring.angular.mongo.ws.service.UserService;
import com.yuri.spring.angular.mongo.ws.service.exception.ObjectNotFoundException;

public abstract class AbstractEmailService implements EmailService {

//	@Value( value = "$(default.sender)")
	private String sender;
//	@Value(value = "$(default.url)")
	private String url; 
	
	@PostConstruct
	public void init() {
		sender = env.getProperty("default.sender");
		url = env.getProperty("default.url");
		System.out.println(sender +" : "+ url);
	}

	@Autowired
	private Environment env;
	
	@Autowired
	TemplateEngine templateEngine;
	
	@Autowired
	JavaMailSender  javaMailSender;
	
	@Autowired
	UserService userService;

	@Override
	public void senderConfirmationHtmlEmail(User user, VerificationToken vToken) {
		try {
			MimeMessage mimeMessage = prapareMimeMessageFromUser(user, vToken);
			senderHtmlEmail(mimeMessage);
		}catch(MessagingException me) {
			throw new ObjectNotFoundException("Erro ao tenta enviar o email");
		}
	}
	
	protected MimeMessage prapareMimeMessageFromUser(User user,VerificationToken vToken) throws MessagingException{
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true);
		mimeMessageHelper.setTo(user.getEmail());
		mimeMessageHelper.setFrom(this.sender);
		System.out.println(this.sender);
		mimeMessageHelper.setSubject("Email de confirmação");
		mimeMessageHelper.setSentDate(new Date(System.currentTimeMillis()));
		mimeMessageHelper.setText(htmlFromTemplateUser(user,vToken),true);
		return mimeMessage;
	}
	
	protected String htmlFromTemplateUser(User user,VerificationToken vToken) {
		String token = UUID.randomUUID().toString();
		if (vToken == null) {
			userService.createVerificationTokenForUser(user, token);
		}else {
			token = vToken.getToken();
		}
		String confirmationUrl = url+ "/api/public/registrationConfirm/users?token="+token;
		Context context = new Context();
		context.setVariable("user", user);
		context.setVariable("confirmationUrl", confirmationUrl);
		return templateEngine.process("emails/registerUser", context);
	}
	
}
