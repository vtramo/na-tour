package com.natour.natour.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.natour.natour.model.AuthenticationResponse;
import com.natour.natour.model.Credentials;
import com.natour.natour.services.authentication.AuthenticationService;

@RestController
public class LoginController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping(
        path = "/login",
        consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public AuthenticationResponse login(Credentials credentials) {
        try {
            return authenticationService.authenticate(credentials);
        } catch (Exception e) {
            return new AuthenticationResponse(false);
        }
    }

    @PostMapping(
        path = "/login/google",
        consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public AuthenticationResponse loginWithGoogle(String authenticationCode) {
        try {
            return authenticationService.authenticateWithGoogle(authenticationCode);
        } catch (Exception e) {
            return new AuthenticationResponse(false);
        }
    }

    @PostMapping(
        path = "/login/facebook",
        consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
    )
    public AuthenticationResponse loginWithFacebook(String accessToken) {
        try {
            return authenticationService.authenticateWithFacebook(accessToken);
        } catch (Exception e) {
            e.printStackTrace();
            return new AuthenticationResponse(false);
        }
    }
}
