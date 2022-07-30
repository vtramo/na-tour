package com.natour.natour.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.natour.natour.model.AuthenticationResponse;
import com.natour.natour.model.Credentials;
import com.natour.natour.services.authentication.AuthenticationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/login/")
@Tag(
    name = "Login Controller", 
    description = "This REST controller provide services to log in the NaTour application"
)
public class LoginController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping(consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    @ResponseStatus(code = HttpStatus.OK)
    @Operation(summary = "Provides a JWT Token for the supplied credentials")
    public AuthenticationResponse login(Credentials credentials) {
        try {
            return authenticationService.authenticate(credentials);
        } catch (Exception e) {
            return new AuthenticationResponse(false);
        }
    }

    @PostMapping(
        path = "google",
        consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    @ResponseStatus(code = HttpStatus.OK)
    @Operation(summary = "Provides a JWT Token for the supplied Google's authentication code")
    public AuthenticationResponse loginWithGoogle(String authenticationCode) {
        try {
            return authenticationService.authenticateWithGoogle(authenticationCode);
        } catch (Exception e) {
            return new AuthenticationResponse(false);
        }
    }

    @PostMapping(
        path = "facebook",
        consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
    )
    @ResponseStatus(code = HttpStatus.OK)
    @Operation(summary = "Provides a JWT Token for the supplied Facebook's access token")
    public AuthenticationResponse loginWithFacebook(String accessToken) {
        try {
            return authenticationService.authenticateWithFacebook(accessToken);
        } catch (Exception e) {
            e.printStackTrace();
            return new AuthenticationResponse(false);
        }
    }
}
