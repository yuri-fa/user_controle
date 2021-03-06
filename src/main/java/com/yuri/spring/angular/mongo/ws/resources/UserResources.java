package com.yuri.spring.angular.mongo.ws.resources;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yuri.spring.angular.mongo.ws.domain.Role;
import com.yuri.spring.angular.mongo.ws.domain.User;
import com.yuri.spring.angular.mongo.ws.dto.UserDTO;
import com.yuri.spring.angular.mongo.ws.service.UserService;

//Definindo a classe como rest
@RestController
//definindo a url do serviço
@RequestMapping("/api")
public class UserResources {

	@Autowired
	private UserService userService;
	
	@Autowired
	DefaultTokenServices defaultTokenService = new DefaultTokenServices();
	
	TokenStore tokenStore = new InMemoryTokenStore();
	
	@GetMapping("/users")
	public ResponseEntity<List<UserDTO>> findAll(){
		List<User> listTemp = userService.findAll();
		List<UserDTO> userDTOList = listTemp.stream().map(user -> new UserDTO(user)).collect(Collectors.toList());
		return ResponseEntity.ok().body(userDTOList);
	}
	
	@GetMapping("/users/{id}")
	public ResponseEntity<UserDTO> findById(@PathVariable String id) {
		User user = userService.findById(id);
		return ResponseEntity.ok().body(new UserDTO(user));
	}
	
	@PostMapping("/users")
	public ResponseEntity<UserDTO> create(@RequestBody UserDTO userDTO){
		User user = userService.fromDTO(userDTO);
		return ResponseEntity.ok().body(new UserDTO(userService.create(user)));
		
	}
	
	@PutMapping("/users/{id}")
	public ResponseEntity<UserDTO> update(@PathVariable String id, @RequestBody UserDTO userDTO){
		User user = userService.fromDTO(userDTO);
		user.setId(id);
		return ResponseEntity.ok().body(new UserDTO(userService.updateUser(user)));
		
	}
	
	@DeleteMapping("/users/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable String id){
		userService.deleteUser(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/users/{id}/roles")
	public ResponseEntity<List<Role>> findRole(@PathVariable String id){
		User user = userService.findById(id);
		return ResponseEntity.ok().body(user.getRoles());
	}
	
	@GetMapping("/users/main")
	public ResponseEntity<UserDTO> getUserMain(Principal principal) {
		User user = userService.findByEmail(principal.getName());
		UserDTO userDto = new UserDTO(user);
		userDto.setPassword("");
		return ResponseEntity.ok().body(userDto);
	}
	
	@GetMapping("/logout")
	public ResponseEntity<Void> logout(HttpServletRequest request){
		String authHeader = request.getHeader("Authrization");
		if (authHeader != null) {
			String tokenValeu = authHeader.replace("Bearer", "").trim();
			OAuth2AccessToken oAuth = defaultTokenService.readAccessToken(tokenValeu);
			tokenStore.removeAccessToken(oAuth);
			defaultTokenService.revokeToken(String.valueOf(oAuth));
		}
		return ResponseEntity.noContent().build(); 
	}
}
