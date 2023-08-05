package com.goit.dev12.securityoauth2user.security;



import com.goit.dev12.securityoauth2user.user.AuthenticationType;
import com.goit.dev12.securityoauth2user.user.Role;
import com.goit.dev12.securityoauth2user.user.User;
import com.goit.dev12.securityoauth2user.user.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Set;

public class UserDetailsServiceImpl implements UserDetailsService {


	private final UserRepository userRepository;

	public UserDetailsServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) 
			throws UsernameNotFoundException {
		User user = userRepository.getUserByUsername(username);
		
		if (user == null) {
			throw new UsernameNotFoundException("Could not find user");
		}
		
		return new MyUserDetails(user);
	}

    public void processOAuthPostLogin(String username) {
		User existUser = userRepository.getUserByUsername(username);

		if (existUser == null) {
			User newUser = new User();
			newUser.setUsername(username);
			newUser.setEmail(username);
			newUser.setAuthType(AuthenticationType.GOOGLE);
			newUser.setRoles(Set.of(Role.USER));
			newUser.setEnabled(true);

			userRepository.save(newUser);

			System.out.println("Created new user: " + username);
		}
    }
}
