package com.natour.natour.security.authproviders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.natour.natour.service.GoogleTokenValidatorService;

import lombok.extern.java.Log;

@Log
@Order(2)
@Component
public class GoogleAuthenticationProvider implements AuthenticationProvider {

    private static final Authentication CREDENTIALS_NOT_VALID = null;
    private static final String SUB = "sub";

    @Autowired
    private GoogleTokenValidatorService googleValidatorTokenService;

    private String idGoogleToken, idGoogleClient;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        idGoogleToken = authentication.getName();
        idGoogleClient = authentication.getCredentials().toString();

        return credentialsAreValid()
            ? createAuthenticationToken()
            : CREDENTIALS_NOT_VALID;
    }

    private boolean credentialsAreValid() {
        GoogleIdToken token = googleValidatorTokenService.validateToken(idGoogleToken);

        if (token == GoogleTokenValidatorService.TOKEN_NOT_VALID) 
            log.info("Token not valid");

        return token != GoogleTokenValidatorService.TOKEN_NOT_VALID &&
               token.getPayload().get(SUB).equals(idGoogleClient);
    }

    private Authentication createAuthenticationToken() {
        return new UsernamePasswordAuthenticationToken(idGoogleToken, idGoogleClient);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
