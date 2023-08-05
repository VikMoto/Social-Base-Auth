package com.goit.dev12.securityoauth2user.security;



import com.goit.dev12.securityoauth2user.user.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class DatabaseLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	@Autowired
	UserService userService;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
										Authentication authentication) throws ServletException, IOException {
		MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
		userService.updateAuthenticationType(userDetails.getUsername(), "DATABASE");
		super.onAuthenticationSuccess(request, response, authentication);
	}

}
