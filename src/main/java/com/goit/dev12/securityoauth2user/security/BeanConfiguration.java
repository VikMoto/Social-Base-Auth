package com.goit.dev12.securityoauth2user.security;


import com.goit.dev12.securityoauth2user.user.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    private final UserRepository userRepository;

    public BeanConfiguration(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public UserDetailsServiceImpl userDetailsService(){
        return new UserDetailsServiceImpl(userRepository);
    }

}
