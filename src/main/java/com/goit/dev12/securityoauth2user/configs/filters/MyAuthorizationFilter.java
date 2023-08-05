package com.goit.dev12.securityoauth2user.configs.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.stream.Collectors;

@Slf4j
public class MyAuthorizationFilter extends GenericFilterBean {

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
try{
      log.info("post {}",request.getReader().lines().collect(Collectors.joining(System.lineSeparator())));
}catch (Exception e){
  
}
      log.info("req {}", request);
    chain.doFilter(request, response);
  }
}
