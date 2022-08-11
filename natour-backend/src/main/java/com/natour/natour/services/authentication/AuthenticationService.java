package com.natour.natour.services.authentication;

import com.natour.natour.model.dto.AuthenticationResponse;
import com.natour.natour.model.dto.Credentials;

public interface AuthenticationService {
    AuthenticationResponse authenticate(Credentials credentials);
    AuthenticationResponse authenticateWithGoogle(String authenticationCode);
    AuthenticationResponse authenticateWithFacebook(String accessToken);
}
