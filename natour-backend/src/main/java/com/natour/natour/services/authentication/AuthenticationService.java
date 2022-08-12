package com.natour.natour.services.authentication;

import com.natour.natour.model.dto.AuthenticationResponseDto;
import com.natour.natour.model.dto.CredentialsDto;

public interface AuthenticationService {
    AuthenticationResponseDto authenticate(CredentialsDto credentials);
    AuthenticationResponseDto authenticateWithGoogle(String authenticationCode);
    AuthenticationResponseDto authenticateWithFacebook(String accessToken);
}
