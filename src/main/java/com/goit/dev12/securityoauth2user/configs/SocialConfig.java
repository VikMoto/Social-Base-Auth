package com.goit.dev12.securityoauth2user.configs;

import com.goit.dev12.securityoauth2user.security.CustomAuthenticationProvider;
import com.goit.dev12.securityoauth2user.security.DatabaseLoginSuccessHandler;
import com.goit.dev12.securityoauth2user.security.UserDetailsServiceImpl;
import com.goit.dev12.securityoauth2user.security.oauth.OAuthLoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
public class SocialConfig {
    @Autowired
    private CustomAuthenticationProvider authProvider;

    private final OAuthLoginSuccessHandler oauthLoginSuccessHandler;
    private final UserDetailsServiceImpl userDetailService;
    private final DatabaseLoginSuccessHandler databaseLoginSuccessHandler;
    private final BCryptPasswordEncoder passwordEncoder;

    public SocialConfig(OAuthLoginSuccessHandler oauthLoginSuccessHandler,
                        UserDetailsServiceImpl userDetailService,
                        DatabaseLoginSuccessHandler databaseLoginSuccessHandler,
                        BCryptPasswordEncoder passwordEncoder) {
        this.oauthLoginSuccessHandler = oauthLoginSuccessHandler;
        this.userDetailService = userDetailService;
        this.databaseLoginSuccessHandler = databaseLoginSuccessHandler;
        this.passwordEncoder = passwordEncoder;
    }


//    @Bean
//    public CustomAuthenticationProvider authenticationProvider(){
//        return new CustomAuthenticationProvider(userDetailService, passwordEncoder);
//    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(authProvider);
        return authenticationManagerBuilder.build();
    }


//    @Bean
//	public DaoAuthenticationProvider authenticationProvider() {
//		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//		authProvider.setUserDetailsService(userDetailService);
//		authProvider.setPasswordEncoder(passwordEncoder);
//
//		return authProvider;
//	}

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
            .formLogin(formLogin -> formLogin
                            .loginPage("/login")
//						.failureUrl("/authentication/login?failed") // default is /login?error
//						.loginProcessingUrl("/authentication/login/process") // default is /login
                            .usernameParameter("username")
                            .passwordParameter("password")
                            .defaultSuccessUrl("/home")
                            .permitAll()
            )
            .logout((logout) -> logout.permitAll())
            .csrf(csrf -> csrf.disable());
    return http.build();
  }


}
