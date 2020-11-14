package com.yuri.spring.angular.mongo.ws.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.yuri.spring.angular.mongo.ws.domain.User;

@Repository
public interface UserRepository extends MongoRepository<User,String>{

	Optional<User> findByEmail (String email);
	
}
