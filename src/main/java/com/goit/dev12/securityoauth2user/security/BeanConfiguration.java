package com.goit.dev12.securityoauth2user.security;


import com.goit.dev12.securityoauth2user.user.UserChatRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    private final UserChatRepository userChatRepository;

    public BeanConfiguration(UserChatRepository userChatRepository) {
        this.userChatRepository = userChatRepository;
    }

    @Bean
    public UserDetailsServiceImpl userDetailsService(){
        return new UserDetailsServiceImpl(userChatRepository);
    }

}
