package com.natour.natour.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.natour.natour.model.AuthenticationResponse;
import com.natour.natour.model.Credentials;
import com.natour.natour.services.authentication.AuthenticationService;
import com.natour.natour.services.authentication.GoogleAuthenticationService;

@RestController
public class LoginController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private GoogleAuthenticationService googleAuthenticationService;

    @PostMapping(
        path = "/login",
        consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public AuthenticationResponse login(Credentials credentials) {
        try {
            return authenticationService.authenticate(credentials);
        } catch (Exception e) {
            return new AuthenticationResponse(false, null, null);
        }
    }

    @PostMapping(
        path = "/login/google",
        consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public AuthenticationResponse loginWithGoogle(String authCode) {
        try {
            return googleAuthenticationService.loginGoogle(authCode);
        } catch (Exception e) {
            return new AuthenticationResponse(false, null, null);
        }
    }

    @PostMapping(
        path = "/login/facebook",
        consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
    )
    public AuthenticationResponse loginWithFacebook(String authCode) {
        try {
            return new AuthenticationResponse(true, null, null); 
        } catch (Exception e) {
            return new AuthenticationResponse(false, null, null);
        }
    }
}
