package com.natour.natour.services.authentication.google;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;

import com.natour.natour.model.ApplicationUser;
import com.natour.natour.model.AuthenticationResponse;
import com.natour.natour.model.Token;
import com.natour.natour.services.authentication.GoogleAuthenticationService;
import com.natour.natour.services.authentication.jwt.TokenGeneratorService;
import com.natour.natour.services.authentication.jwt.TokenScope;
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
    private TokenGeneratorService tokenGeneratorService;

    @Autowired
    private ApplicationUserService applicationUserService;

    @Override
    public AuthenticationResponse loginGoogle(String authCode) {
        GoogleIdToken googleIdToken = googleAuthenticationCodeService.getIdToken(authCode);
        verifyGoogleIdToken(googleIdToken);

        log.info("Google ID Token is valid, authcode: " + authCode);

        Payload googlePayload = googleIdToken.getPayload();
        ApplicationUser user = saveUserIfNotExist(googlePayload);

        Token appToken = generateTokenApp(googlePayload);

        log.info("Login with google was successful: " + user);
        return new AuthenticationResponse(true, user, appToken);
    }

    private void verifyGoogleIdToken(GoogleIdToken googleIdToken) {
        if (!googleTokenValidatorService.validateToken(googleIdToken)) {
            log.warning("Google ID Token is invalid: " + googleIdToken);
            throw new IllegalArgumentException("Google ID Token is invalid.");
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
            googlePayload.getEmail(),
            googlePayload.get("sub").toString()
        );
    }

    private Token generateTokenApp(Payload googlePayload) {
        return tokenGeneratorService.generateToken(Map.of(
            "username", List.of(googlePayload.getEmail()),
            "password", List.of(googlePayload.get("sub").toString())
        ), TokenScope.USER);
    }
}
