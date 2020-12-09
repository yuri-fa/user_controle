package com.yuri.spring.angular.mongo.ws.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yuri.spring.angular.mongo.ws.domain.User;
import com.yuri.spring.angular.mongo.ws.domain.VerificationToken;
import com.yuri.spring.angular.mongo.ws.dto.UserDTO;
import com.yuri.spring.angular.mongo.ws.resources.util.GenericResponse;
import com.yuri.spring.angular.mongo.ws.service.UserService;

@RestController
@RequestMapping("/api/public")
public class RegistrationResource {
	
	@Autowired
	UserService userService;

	@PostMapping("/registration/users")
	public ResponseEntity<Void> registationUser(@RequestBody UserDTO userDTO){
		User user = userService.fromDTO(userDTO);
		userService.registrationUser(user);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/registrationConfirm/users")
	public ResponseEntity<GenericResponse> confirmationUser(@RequestParam("token") String token){
		Object result = userService.validadeVerificationToken(token);
		if (result == null) {
			return ResponseEntity.ok().body(new GenericResponse("Sucess"));
		}
		return ResponseEntity.status(HttpStatus.SEE_OTHER).body(new GenericResponse(result.toString()));
	}
	
	@GetMapping("/resendRegistrationToken/users")
	public ResponseEntity<Void> resendRegistrationToken(@RequestParam("email") String email){
		userService.generateNewVerificationToken(email,false);
		return ResponseEntity.noContent().build();
	}
	
	@PostMapping("/resetPassword/users")
	public ResponseEntity<Void> resetSenha(@RequestParam("email") String email){
		userService.generateNewVerificationToken(email,true);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/changePassword/users")
	public ResponseEntity<GenericResponse> resendRegistrationToken(@RequestParam("id") String id,@RequestParam("token") String token){
		final String result = userService.validadePasswordResetToken(id,token);
		if (result != null) {
			return ResponseEntity.ok().body(new GenericResponse("sucess"));
		}
		return ResponseEntity.status(HttpStatus.SEE_OTHER).body(new GenericResponse(result));
	}
	
	@PostMapping("savePassword/user")
	public ResponseEntity<GenericResponse> savePassword(@RequestParam("token") String token, @RequestParam("password") String password) {
		String result = userService.validadeVerificationToken(token);
		if (result != null) {
			return ResponseEntity.status(HttpStatus.SEE_OTHER).body(new GenericResponse(result.toString()));
		}
		VerificationToken vToken = userService.getVerificationToken(token);
		if (vToken != null) {
			userService.changePassword(vToken.getUser(),password);
		}
		return ResponseEntity.noContent().build();
	}
}
