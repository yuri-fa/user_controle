package com.yuri.spring.angular.mongo.ws.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.yuri.spring.angular.mongo.ws.domain.Role;

public interface RoleRepository extends MongoRepository<Role,String> {
	
	Optional<Role> findByName(String name);
}
