package com.yuri.spring.angular.mongo.ws.service;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.yuri.spring.angular.mongo.ws.domain.User;
import com.yuri.spring.angular.mongo.ws.domain.VerificationToken;
import com.yuri.spring.angular.mongo.ws.dto.UserDTO;
import com.yuri.spring.angular.mongo.ws.repository.RoleRepository;
import com.yuri.spring.angular.mongo.ws.repository.UserRepository;
import com.yuri.spring.angular.mongo.ws.repository.VerificationTokenRepository;
import com.yuri.spring.angular.mongo.ws.service.email.EmailService;
import com.yuri.spring.angular.mongo.ws.service.exception.ObjectAlreadExistException;
import com.yuri.spring.angular.mongo.ws.service.exception.ObjectNotFoundException;

@Service
public class UserService {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RoleRepository roleRespository;
	
	@Autowired
	VerificationTokenRepository verificationTokenRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder; 
	
	@Autowired
	EmailService emailService;
	
	public List<User> findAll(){
		return userRepository.findAll();
	}
	
	public User findById(String id) {
		Optional<User> user = userRepository.findById(id);
		return user.orElseThrow(() -> new ObjectNotFoundException("Usuário não encontrado"));
	}
	
	public User create(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}
	
	public User fromDTO(UserDTO userDTO) {
		return new User(userDTO);
	}
	
	public User updateUser(User user) {
		Optional<User> updateUser = userRepository.findById(user.getId());
		return updateUser.map(userTemp -> userRepository.save(new User(user.getId(),user.getFirstName(),user.getLastName(),user.getEmail(),user.getPassword(),user.getEnabled())))
				.orElseThrow(() -> new ObjectNotFoundException("Usuário não encontrado"));
	}

	public void deleteUser(String id) {
		userRepository.deleteById(id);
	}
	
	public User registrationUser(User user) {
		Optional<User> userOpt = userRepository.findByEmail(user.getEmail());
		if (userOpt.isPresent()) {
			throw new ObjectAlreadExistException(String.format("Já existe uma conta para este endereço de email "));
		}
		user.setRoles(Arrays.asList(roleRespository.findByName("ROLE_USER").get()));
		user.setEnabled(false);
		user = create(user);
		emailService.senderConfirmationHtmlEmail(user, null,false);
		return user;
	}
	
	public void createVerificationTokenForUser(User user,String token) {
		final VerificationToken vToken = new VerificationToken(token,user );
		verificationTokenRepository.save(vToken);
	}
	
	public String validadeVerificationToken(String token) {
		final Optional<VerificationToken> tokenOpt = verificationTokenRepository.findByToken(token);
		if (!tokenOpt.isPresent()) {
			return "invalidToken";
		}
		User user = tokenOpt.get().getUser();
		String result = validadeTimeToken(tokenOpt.get());
		if (result != null) {
			return result;
		}
		user.setEnabled(true);
		userRepository.save(user);
		return null;
	}
	
	public User findByEmail(String email){
		Optional<User> user = userRepository.findByEmail(email);
		return user.orElseThrow(() -> new ObjectNotFoundException(String.format("Usuário não encontrado")));
	}
	
	public void generateNewVerificationToken(String email,Boolean resetandoSenha) {
		User user = findByEmail(email);
		Optional<VerificationToken> tokenOpt = verificationTokenRepository.findByUser(user);
		VerificationToken newToken = null;
		if (tokenOpt.isPresent()) {
			newToken = tokenOpt.get();
			newToken.updateToken(UUID.randomUUID().toString());
		}else {
			newToken = new VerificationToken(UUID.randomUUID().toString(), user);
		}
		VerificationToken updateToken = verificationTokenRepository.save(newToken);
		emailService.senderConfirmationHtmlEmail(user, updateToken,resetandoSenha);
	}

	public String validadePasswordResetToken(String id, String token) {
		Optional<VerificationToken> vToken = verificationTokenRepository.findByToken(token);
		if (!vToken.isPresent() || !vToken.get().getId().equals(id)) {
			return "invalidToken"; 
		}
		
		return validadeTimeToken(vToken.get());
	}
	
	
	private String validadeTimeToken(VerificationToken vToken) {
		Calendar calendar = Calendar.getInstance();
		if (vToken.getExpiryDate().getTime() - calendar.getTime().getTime() <= 0) {
			return "expired";
		}
		return null;
	}

	public VerificationToken getVerificationToken(String token) {
		return verificationTokenRepository.findByToken(token).orElseThrow(() -> new ObjectNotFoundException("Token não encontrado"));
	}

	public void changePassword(User user, String password) {
		user.setPassword(passwordEncoder.encode(password));
		userRepository.save(user);
	}
}
