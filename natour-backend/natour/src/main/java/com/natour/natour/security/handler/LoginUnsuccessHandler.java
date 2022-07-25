package com.natour.natour.security.handler;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

public class LoginUnsuccessHandler implements AuthenticationFailureHandler {
    
	@Override
	public void onAuthenticationFailure(
        HttpServletRequest request, 
        HttpServletResponse response,
        AuthenticationException exception
    ) throws IOException, ServletException {
        
        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();
        writer.write("false");
        writer.close();
	}
}
