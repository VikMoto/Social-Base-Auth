package com.goit.dev12.securityoauth2user.configs;

import com.goit.dev12.securityoauth2user.configs.filters.MyAuthorizationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
public class SocialConfig {
  
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    
    http.addFilterAfter(new MyAuthorizationFilter(), BasicAuthenticationFilter.class)
        .authorizeHttpRequests((requests) -> requests
            .requestMatchers("/", "/home").permitAll()
            .anyRequest().authenticated()
        )
        //http://localhost:8080/login/oauth2/code/google
        .oauth2Login((login) -> login
            .redirectionEndpoint((endpoint) -> endpoint
                .baseUri("/login/oauth2/code/google/*")
            )
            .loginPage("/login").permitAll()
        )			
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
//FOR CUSTOM REGISTRATION
//  @Bean
//  public ReactiveClientRegistrationRepository clientRegistrationRepository() {
//    return new InMemoryReactiveClientRegistrationRepository(this.googleClientRegistration());
//  }
//
//  private ClientRegistration googleClientRegistration() {
//    return ClientRegistration.withRegistrationId("google")
//        .clientId("google-client-id")
//        .clientSecret("google-client-secret")
//        .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
//        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//        .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
//        .scope("openid", "profile", "email", "address", "phone")
//        .authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
//        .tokenUri("https://www.googleapis.com/oauth2/v4/token")
//        .userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
//        .userNameAttributeName(IdTokenClaimNames.SUB)
//        .jwkSetUri("https://www.googleapis.com/oauth2/v3/certs")
//        .clientName("Google")
//        .build();
//  }
@Bean
public GrantedAuthoritiesMapper userAuthoritiesMapper() {
  return (authorities) -> {
    Set<GrantedAuthority> mappedAuthorities = new HashSet<>();

    authorities.forEach(authority -> {
      if (OidcUserAuthority.class.isInstance(authority)) {
        OidcUserAuthority oidcUserAuthority = (OidcUserAuthority)authority;

        OidcIdToken idToken = oidcUserAuthority.getIdToken();
        OidcUserInfo userInfo = oidcUserAuthority.getUserInfo();

        // Map the claims found in idToken and/or userInfo
        // to one or more GrantedAuthority's and add it to mappedAuthorities

      } else if (OAuth2UserAuthority.class.isInstance(authority)) {
        OAuth2UserAuthority oauth2UserAuthority = (OAuth2UserAuthority)authority;

        Map<String, Object> userAttributes = oauth2UserAuthority.getAttributes();

        // Map the attributes found in userAttributes
        // to one or more GrantedAuthority's and add it to mappedAuthorities

      }
    });

    return mappedAuthorities;
  };
}
}
