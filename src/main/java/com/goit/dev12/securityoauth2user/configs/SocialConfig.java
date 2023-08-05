package com.goit.dev12.securityoauth2user.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
public class SocialConfig {
  
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    
    http
        .authorizeHttpRequests((requests) -> requests
            .requestMatchers("/", "/home").permitAll()
            .anyRequest().authenticated()
        )
        .oauth2Login((login) -> login
            .loginPage("/login").permitAll()
        )
        .formLogin(formLogin -> formLogin.loginPage("/login"))
        .logout((logout) -> logout.permitAll())
        .csrf(csrf -> csrf.disable());
    return http.build();
  }

  @Bean
  public UserDetailsService userDetailsService() {
    UserDetails user =
        User.withDefaultPasswordEncoder()
            .username("user")
            .password("password")
            .roles("USER")
            .build();
    return new InMemoryUserDetailsManager(user);
  }

}
