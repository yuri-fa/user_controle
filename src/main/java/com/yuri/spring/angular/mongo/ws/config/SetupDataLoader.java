package com.yuri.spring.angular.mongo.ws.config;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

import com.yuri.spring.angular.mongo.ws.domain.Role;
import com.yuri.spring.angular.mongo.ws.domain.User;
import com.yuri.spring.angular.mongo.ws.repository.RoleRepository;
import com.yuri.spring.angular.mongo.ws.repository.UserRepository;

@Configuration
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent>{
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		userRepository.deleteAll();
		roleRepository.deleteAll();
		User user1 = new User("yuri","alcantara","yurialcantara");
		User user2 = new User("dalviane","viana","dalvianeviana");
		Role admin = new Role("ROLE_ADMIN");
		Role user = new Role("ROLE_USER");
		roleRepository.save(admin);
		roleRepository.save(user);
		user1.setRoles(Arrays.asList(admin));
		user2.setRoles(Arrays.asList(user));
		createUserIfNotFound(user1);
		createUserIfNotFound(user2);
	}
	
	public User createUserIfNotFound(final User user) {
		Optional<User> obj = userRepository.findByEmail(user.getEmail());
		if (obj.isPresent()) {
			return obj.get();
		}
		return userRepository.save(user);
	}
	
	public Role createRoleIfNotFound(Role role) {
		Optional<Role> obj = roleRepository.findByName(role.getName());
		if (obj.isPresent()) {
			return obj.get();
		}
		return roleRepository.save(role);
	}
	

}
