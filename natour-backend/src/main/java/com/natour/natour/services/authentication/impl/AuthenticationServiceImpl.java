package com.natour.natour.services.authentication.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.natour.natour.model.ApplicationUser;
import com.natour.natour.model.AuthenticationResponse;
import com.natour.natour.model.Credentials;
import com.natour.natour.model.Token;
import com.natour.natour.services.authentication.AuthenticationService;
import com.natour.natour.services.authentication.application.ApplicationAuthenticationService;
import com.natour.natour.services.authentication.facebook.FacebookAuthenticationService;
import com.natour.natour.services.authentication.google.GoogleAuthenticationService;
import com.natour.natour.services.authentication.jwt.TokenGeneratorService;
import com.natour.natour.services.authentication.jwt.TokenScope;

import lombok.extern.java.Log;

@Log
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private TokenGeneratorService tokenGeneratorService;

    @Autowired
    private ApplicationAuthenticationService applicationAuthenticationService;

    @Autowired
    private GoogleAuthenticationService googleAuthenticationService;

    @Autowired
    private FacebookAuthenticationService facebookAuthenticationService;

    @Override
    public AuthenticationResponse authenticate(Credentials credentials) {
        ApplicationUser user = applicationAuthenticationService.authenticate(credentials);
        
        Token token = generateApplicationUserToken(
            credentials.getUsername(),
            credentials.getPassword()
        );

        log.info("Login was successful: " + user);
        return new AuthenticationResponse(true, user, token);
    }

    @Override
    public AuthenticationResponse authenticateWithGoogle(String authenticationCode) {
        ApplicationUser user = googleAuthenticationService.authenticate(authenticationCode);

        Token token = generateApplicationUserToken(
            user.getUsername(),
            user.getPassword()
        );

        log.info("Login with google was successful: " + user);
        return new AuthenticationResponse(true, user, token);
    }

    @Override
    public AuthenticationResponse authenticateWithFacebook(String accessToken) {
        ApplicationUser user = facebookAuthenticationService.authenticate(accessToken);

        Token token = generateApplicationUserToken(
            user.getUsername(),
            user.getPassword()
        );

        log.info("Login with facebook was successfull: " + user);
        return new AuthenticationResponse(true, user, token);
    }
    
    private Token generateApplicationUserToken(String username, String password) {
        return tokenGeneratorService.generateToken(Map.of(
            "username", List.of(username),
            "password", List.of(password)
        ), TokenScope.USER);
    }
}
