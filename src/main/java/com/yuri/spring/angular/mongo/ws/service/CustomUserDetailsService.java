package com.yuri.spring.angular.mongo.ws.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.yuri.spring.angular.mongo.ws.domain.Role;
import com.yuri.spring.angular.mongo.ws.domain.User;
import com.yuri.spring.angular.mongo.ws.repository.UserRepository;
import com.yuri.spring.angular.mongo.ws.service.exception.ObjectNotEnableException;
import com.yuri.spring.angular.mongo.ws.service.exception.ObjectNotFoundException;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = userRepository.findByEmail(username);
		if (!user.isPresent()) {
			throw new ObjectNotEnableException(String.format("UserNotExist"));
		}else if (!user.get().getEnabled()) {
			throw new  ObjectNotFoundException(String.format("UserNotEnabled")); 
		}
			
		return new UserReposirotyUserDetails(user.get());
	}
	
	private final List<GrantedAuthority> getGrantedAuthorities(final Collection<Role> roles){
		final List<GrantedAuthority> authorities = new ArrayList<>();
		for (Role role : roles) {
			authorities.add(new SimpleGrantedAuthority(role.getName()));
		}
		return authorities;
	}
	
	public final Collection<? extends GrantedAuthority> getAuthorities(final Collection<Role> roles){
		return getGrantedAuthorities(roles);
	}
	
	private static final class UserReposirotyUserDetails extends User implements UserDetails{

		private static final long serialVersionUID = 1L;

		public UserReposirotyUserDetails(User user) {
			super(user);
		}

		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
			return getRoles();
		}

		@Override
		public String getUsername() {
			return getEmail();
		}

		@Override
		public boolean isAccountNonExpired() {
			return true;
		}

		@Override
		public boolean isAccountNonLocked() {
			return true;
		}

		@Override
		public boolean isCredentialsNonExpired() {
			return true;
		}

		@Override
		public boolean isEnabled() {
			return true;
		}
		
	}

}
