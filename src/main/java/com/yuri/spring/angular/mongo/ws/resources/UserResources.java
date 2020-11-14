package com.yuri.spring.angular.mongo.ws.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yuri.spring.angular.mongo.ws.domain.User;
import com.yuri.spring.angular.mongo.ws.service.UserService;

//Definindo a classe como rest
@RestController
//definindo a url do serviço
@RequestMapping("/api")
public class UserResources {

	@Autowired
	private UserService userService;
	
	@GetMapping("/users")
	public ResponseEntity<List<User>> findAll(){
		List<User> listTemp = userService.findAll();
		return ResponseEntity.ok().body(userService.findAll());
	}
	
}
