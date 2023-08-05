package com.goit.dev12.securityoauth2user.configs;

import com.goit.dev12.securityoauth2user.security.CustomAuthenticationProvider;
import com.goit.dev12.securityoauth2user.security.DatabaseLoginSuccessHandler;
import com.goit.dev12.securityoauth2user.security.UserDetailsServiceImpl;
import com.goit.dev12.securityoauth2user.security.oauth.CustomOAuth2User;
import com.goit.dev12.securityoauth2user.security.oauth.CustomOAuth2UserService;
import com.goit.dev12.securityoauth2user.security.oauth.OAuthLoginSuccessHandler;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
public class SocialConfig {
    @Autowired
    private CustomAuthenticationProvider authProvider;

    private final OAuthLoginSuccessHandler oauthLoginSuccessHandler;
    private final CustomOAuth2UserService oauth2UserService;
    private final UserDetailsServiceImpl userDetailService;
    private final DatabaseLoginSuccessHandler databaseLoginSuccessHandler;
    private final BCryptPasswordEncoder passwordEncoder;

    public SocialConfig(OAuthLoginSuccessHandler oauthLoginSuccessHandler,
                        CustomOAuth2UserService oauth2UserService,
                        UserDetailsServiceImpl userDetailService,
                        DatabaseLoginSuccessHandler databaseLoginSuccessHandler,
                        BCryptPasswordEncoder passwordEncoder) {
        this.oauthLoginSuccessHandler = oauthLoginSuccessHandler;
        this.oauth2UserService = oauth2UserService;
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
                    .userInfoEndpoint(userInfoEndpoint->
						userInfoEndpoint.userService(oauth2UserService))
                    .successHandler(new AuthenticationSuccessHandler() {
                        @Override
                        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
                            AuthenticationSuccessHandler.super.onAuthenticationSuccess(request, response, chain, authentication);
                        }
                        @Override
                        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                                            Authentication authentication) throws IOException, ServletException {
                            System.out.println("AuthenticationSuccessHandler invoked");
                            System.out.println("Authentication name: " + authentication.getName());
                            CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();
                            userDetailService.processOAuthPostLogin(oauthUser.getEmail());
                            response.sendRedirect("/home");
                        }
                    }
            ))
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
