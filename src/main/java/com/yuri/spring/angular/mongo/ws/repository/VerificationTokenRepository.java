package com.yuri.spring.angular.mongo.ws.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.yuri.spring.angular.mongo.ws.domain.User;
import com.yuri.spring.angular.mongo.ws.domain.VerificationToken;

public interface VerificationTokenRepository extends MongoRepository<VerificationToken, String>{

	Optional<VerificationToken> findByToken(String token);
	
	Optional<VerificationToken> findByUser(User user);
	
}
