package com.natour.natour.services.authentication.google.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;

import com.natour.natour.model.ApplicationUser;
import com.natour.natour.services.authentication.google.GoogleAuthenticationService;
import com.natour.natour.services.authentication.google.token.GoogleAuthCodeService;
import com.natour.natour.services.authentication.google.token.GoogleTokenValidatorService;
import com.natour.natour.services.user.ApplicationUserService;

import lombok.extern.java.Log;

@Log
@Service
public class GoogleAuthenticationServiceImpl implements GoogleAuthenticationService {
    
    @Autowired
    private GoogleAuthCodeService googleAuthenticationCodeService;

    @Autowired
    private GoogleTokenValidatorService googleTokenValidatorService;

    @Autowired
    private ApplicationUserService applicationUserService;

    @Override
    public ApplicationUser authenticate(String authCode) {
        GoogleIdToken googleIdToken = googleAuthenticationCodeService.getIdToken(authCode);
        verifyGoogleIdToken(googleIdToken);

        Payload googlePayload = googleIdToken.getPayload();
        return saveUserIfNotExist(googlePayload);
    }

    private void verifyGoogleIdToken(GoogleIdToken googleIdToken) {
        if (!googleTokenValidatorService.validateToken(googleIdToken)) {
            log.warning("Google ID Token is invalid: " + googleIdToken);
            throw new RuntimeException();
        }
    }

    private ApplicationUser saveUserIfNotExist(Payload googlePayload) {
        String email = googlePayload.getEmail();
        ApplicationUser user = applicationUserService.findByUsername(email);
        if (user == null) {
            user = createApplicationUser(googlePayload);
            user = applicationUserService.save(user);
        
            log.info("A new Google User has been created: " + user);
        }
        return user;
    }

    private ApplicationUser createApplicationUser(Payload googlePayload) {
        return new ApplicationUser(
            null,
            googlePayload.getEmail(),
            googlePayload.get("name").toString(),
            googlePayload.get("given_name").toString(),
            googlePayload.getEmail(),
            googlePayload.get("sub").toString()
        );
    }
}
