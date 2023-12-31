package com.goit.dev12.securityoauth2user.configs;//package com.example.webauth.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class PasswordEncoderConfig {
//        @Bean
//        PasswordEncoder passwordEncoder() {
//        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//    }

    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {
            return  new BCryptPasswordEncoder();
    }
}
