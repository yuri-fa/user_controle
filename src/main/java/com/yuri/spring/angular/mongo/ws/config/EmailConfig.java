package com.yuri.spring.angular.mongo.ws.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.yuri.spring.angular.mongo.ws.service.email.EmailService;
import com.yuri.spring.angular.mongo.ws.service.email.SmtpEmailService;

@Configuration
@PropertySource("classpath:application.properties")
public class EmailConfig {
	
	@Bean
	public EmailService emailService() {
		return new SmtpEmailService();
	}

}
