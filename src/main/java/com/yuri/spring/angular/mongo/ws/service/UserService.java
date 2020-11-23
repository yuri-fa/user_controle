package com.yuri.spring.angular.mongo.ws.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuri.spring.angular.mongo.ws.domain.User;
import com.yuri.spring.angular.mongo.ws.dto.UserDTO;
import com.yuri.spring.angular.mongo.ws.repository.UserRepository;
import com.yuri.spring.angular.mongo.ws.service.exception.ObjectNotFoundException;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	public List<User> findAll(){
		return userRepository.findAll();
	}
	
	public User findById(String id) {
		Optional<User> user = userRepository.findById(id);
		return user.orElseThrow(() -> new ObjectNotFoundException("Usuário não encontrado"));
	}
	
	public User create(User user) {
		return userRepository.save(user);
	}
	
	public User fromDTO(UserDTO userDTO) {
		return new User(userDTO);
	}
	
	public User updateUser(User user) {
		Optional<User> updateUser = userRepository.findById(user.getId());
		return updateUser.map(userTemp -> userRepository.save(new User(user.getId(),user.getFirstName(),user.getLastName(),user.getEmail())))
				.orElseThrow(() -> new ObjectNotFoundException("Usuário não encontrado"));
	}

	public void deleteUser(String id) {
		userRepository.deleteById(id);
	}
}
