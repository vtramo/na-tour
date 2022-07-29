package com.natour.natour.services.authentication;

import com.natour.natour.model.AuthenticationResponse;

public interface GoogleAuthenticationService {
    AuthenticationResponse loginGoogle(String authCode);
}
